package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PontoDAO {

    private Connection conn;

    public PontoDAO(Connection conn) {
        this.conn = conn;
    }

    // ── Constantes de status ──────────────────────────────────────────
    public static final String STATUS_ABERTO   = "ABERTO";
    public static final String STATUS_FECHADO  = "FECHADO";

    /**
     * Verifica se já existe um ponto com o mesmo horário (HH:mm) e data de hoje
     * para o funcionário. Impede duplicatas no mesmo minuto.
     */
    public boolean existePontoNoMesmoHorario(int idFuncionario) throws SQLException {
        String horario = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
        String data    = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String sql = "SELECT COUNT(*) FROM ponto WHERE idfuncionario = ? AND dataponto = ? AND horario = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, idFuncionario);
        stmt.setString(2, data);
        stmt.setString(3, horario);
        ResultSet rs = stmt.executeQuery();
        rs.next();
        int count = rs.getInt(1);
        rs.close();
        stmt.close();
        return count > 0;
    }

    /**
     * Retorna o ponto com STATUS = 'ABERTO' de hoje para o funcionário,
     * ou null se não houver nenhum.
     */
    public PontoRow buscarPontoAberto(int idFuncionario) throws SQLException {
        String data = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String sql  = "SELECT TOP 1 idponto, horario, dataponto, status "
                    + "FROM ponto WHERE idfuncionario = ? AND dataponto = ? AND status = ? "
                    + "ORDER BY horario ASC";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, idFuncionario);
        stmt.setString(2, data);
        stmt.setString(3, STATUS_ABERTO);
        ResultSet rs = stmt.executeQuery();
        PontoRow row = null;
        if (rs.next()) {
            row = new PontoRow(
                rs.getInt("idponto"),
                rs.getString("horario"),
                rs.getString("dataponto"),
                rs.getString("status")
            );
        }
        rs.close();
        stmt.close();
        return row;
    }

    /**
     * Registra um novo ponto ABERTO para o funcionário.
     * Lança IllegalStateException se já existir ponto no mesmo horário (HH:mm).
     */
    public void registrarPonto(int idFuncionario) throws SQLException {
        if (existePontoNoMesmoHorario(idFuncionario)) {
            throw new IllegalStateException(
                "Já existe um ponto registrado neste horário. Aguarde um minuto e tente novamente.");
        }

        String horario = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
        String data    = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        String sql = "INSERT INTO ponto (horario, dataponto, idfuncionario, status) VALUES (?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, horario);
        stmt.setString(2, data);
        stmt.setInt(3, idFuncionario);
        stmt.setString(4, STATUS_ABERTO);
        stmt.executeUpdate();
        stmt.close();
    }

    /**
     * Fecha um ponto aberto, registrando o horário de saída atual.
     * Lança IllegalStateException se já existir ponto no mesmo horário.
     */
    public void fecharPonto(int idPonto, int idFuncionario) throws SQLException {
        if (existePontoNoMesmoHorario(idFuncionario)) {
            throw new IllegalStateException(
                "Já existe um ponto registrado neste horário. Aguarde um minuto e tente novamente.");
        }

        String horarioFechamento = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
        String sql = "UPDATE ponto SET status = ?, horariofechamento = ? WHERE idponto = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, STATUS_FECHADO);
        stmt.setString(2, horarioFechamento);
        stmt.setInt(3, idPonto);
        stmt.executeUpdate();
        stmt.close();
    }

    /**
     * Retorna todos os pontos de um funcionário, do mais recente ao mais antigo.
     */
    public List<PontoRow> listarPorFuncionario(int idFuncionario) throws SQLException {
        String sql = "SELECT idponto, horario, ISNULL(horariofechamento,'') as horariofechamento, "
                   + "dataponto, status FROM ponto "
                   + "WHERE idfuncionario = ? "
                   + "ORDER BY dataponto DESC, horario DESC";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, idFuncionario);
        ResultSet rs = stmt.executeQuery();

        List<PontoRow> lista = new ArrayList<>();
        while (rs.next()) {
            lista.add(new PontoRow(
                rs.getInt("idponto"),
                rs.getString("horario"),
                rs.getString("dataponto"),
                rs.getString("status"),
                rs.getString("horariofechamento")
            ));
        }
        rs.close();
        stmt.close();
        return lista;
    }

    /** Conta pontos do dia atual para um funcionário. */
    public int contarPontosHoje(int idFuncionario) throws SQLException {
        String hoje = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String sql  = "SELECT COUNT(*) FROM ponto WHERE idfuncionario = ? AND dataponto = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, idFuncionario);
        stmt.setString(2, hoje);
        ResultSet rs = stmt.executeQuery();
        rs.next();
        int count = rs.getInt(1);
        rs.close();
        stmt.close();
        return count;
    }

    /** Soma as horas trabalhadas do mês atual para um funcionário. */
    public String somarHorasMes(int idFuncionario) throws SQLException {
        String sql = "SELECT ISNULL(SUM( " +
                     "  CASE WHEN CAST(horariofechamento as TIME) < CAST(horario as TIME) " +
                     "  THEN DATEDIFF(MINUTE, CAST(horario as TIME), CAST(horariofechamento as TIME)) + 1440 " +
                     "  ELSE DATEDIFF(MINUTE, CAST(horario as TIME), CAST(horariofechamento as TIME)) END " +
                     "), 0) " +
                     "FROM ponto WHERE idfuncionario = ? AND status = 'FECHADO' " +
                     "AND MONTH(dataponto) = MONTH(GETDATE()) AND YEAR(dataponto) = YEAR(GETDATE())";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, idFuncionario);
        ResultSet rs = stmt.executeQuery();
        rs.next();
        int totalMinutos = rs.getInt(1);
        rs.close();
        stmt.close();
        
        long h = totalMinutos / 60;
        long m = totalMinutos % 60;
        return String.format("%02dh %02dm", h, m);
    }

    // ── Row DTO ───────────────────────────────────────────────────────
    public static class PontoRow {
        private final int    id;
        private final String horario;
        private final String data;
        private final String status;
        private final String horarioFechamento;

        /** Construtor completo */
        public PontoRow(int id, String horario, String data, String status, String horarioFechamento) {
            this.id                = id;
            this.horario           = horario;
            this.data              = data;
            this.status            = status != null ? status : STATUS_ABERTO;
            this.horarioFechamento = horarioFechamento != null ? horarioFechamento : "";
        }

        /** Construtor sem horário de fechamento (compatibilidade) */
        public PontoRow(int id, String horario, String data, String status) {
            this(id, horario, data, status, "");
        }

        public int    getId()                { return id; }
        public String getHorario()           { return horario; }
        public String getData()              { return data; }
        public String getStatus()            { return status; }
        public String getHorarioFechamento() { return horarioFechamento; }
        public boolean isAberto()            { return STATUS_ABERTO.equalsIgnoreCase(status); }

        public String getTotalHoras() {
            if (horarioFechamento == null || horarioFechamento.isBlank() || horario == null || horario.isBlank() || horarioFechamento.equals("--:--")) {
                return "--:--";
            }
            try {
                java.time.LocalTime start = java.time.LocalTime.parse(horario);
                java.time.LocalTime end = java.time.LocalTime.parse(horarioFechamento);
                long minutes = java.time.Duration.between(start, end).toMinutes();
                if (minutes < 0) {
                    minutes += 24 * 60; // if shifted to next day
                }
                long h = minutes / 60;
                long m = minutes % 60;
                return String.format("%02dh %02dm", h, m);
            } catch (Exception e) {
                return "--:--";
            }
        }
    }
}
