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

    /**
     * Registra um ponto para o funcionário.
     * HORARIO é varchar(5) → "HH:mm"
     * DATAPONTO usa o DEFAULT do banco (getdate()), mas enviamos a data atual para ser explícito.
     * IDENDERECO usa o endereço do funcionário.
     */
    public void registrarPonto(int idFuncionario, int idEndereco) throws SQLException {
        String horario = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
        String data    = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        String sql = "INSERT INTO ponto (horario, dataponto, idendereco, idfuncionario) VALUES (?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, horario);
        stmt.setString(2, data);
        stmt.setInt(3, idEndereco);
        stmt.setInt(4, idFuncionario);
        stmt.executeUpdate();
        stmt.close();
    }

    /**
     * Retorna todos os pontos de um funcionário, do mais recente ao mais antigo.
     */
    public List<PontoRow> listarPorFuncionario(int idFuncionario) throws SQLException {
        String sql = "SELECT idponto, horario, dataponto FROM ponto "
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
                rs.getString("dataponto")
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

    /** Conta pontos do mês atual para um funcionário. */
    public int contarPontosMes(int idFuncionario) throws SQLException {
        String sql = "SELECT COUNT(*) FROM ponto WHERE idfuncionario = ? "
                   + "AND MONTH(dataponto) = MONTH(GETDATE()) AND YEAR(dataponto) = YEAR(GETDATE())";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, idFuncionario);
        ResultSet rs = stmt.executeQuery();
        rs.next();
        int count = rs.getInt(1);
        rs.close();
        stmt.close();
        return count;
    }

    // ── Row DTO ──────────────────────────────────────────────────────────
    public static class PontoRow {
        private final int    id;
        private final String horario;
        private final String data;

        public PontoRow(int id, String horario, String data) {
            this.id      = id;
            this.horario = horario;
            this.data    = data;
        }

        public int    getId()     { return id; }
        public String getHorario() { return horario; }
        public String getData()    { return data; }
    }
}
