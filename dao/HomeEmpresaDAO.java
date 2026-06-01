package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HomeEmpresaDAO {

    private Connection conn;

    public HomeEmpresaDAO(Connection conn) {
        this.conn = conn;
    }

    /** Linha de funcionário para exibição na tabela */
    public static class FuncionarioRow {
        public int    id;
        public String nome;
        public String cpf;
        public String cargo;
        public String telefone;
        public String email;

        public FuncionarioRow(int id, String nome, String cpf,
                              String cargo, String telefone, String email) {
            this.id       = id;
            this.nome     = nome;
            this.cpf      = cpf;
            this.cargo    = cargo;
            this.telefone = telefone;
            this.email    = email;
        }

        public int    getId()       { return id; }
        public String getNome()     { return nome; }
        public String getCpf()      { return cpf; }
        public String getCargo()    { return cargo; }
        public String getTelefone() { return telefone; }
        public String getEmail()    { return email; }
    }

    /** Linha de ponto para exibição na tabela */
    public static class PontoRow {
        public int    id;
        public String nomeFuncionario;
        public String horario;
        public String horarioFechamento;
        public String dataPonto;
        public String status;

        public PontoRow(int id, String nomeFuncionario, String horario, String horarioFechamento, String dataPonto, String status) {
            this.id                = id;
            this.nomeFuncionario   = nomeFuncionario;
            this.horario           = horario;
            this.horarioFechamento = horarioFechamento;
            this.dataPonto         = dataPonto;
            this.status            = status;
        }

        public int    getId()                { return id; }
        public String getNomeFuncionario()   { return nomeFuncionario; }
        public String getHorario()           { return horario; }
        public String getHorarioFechamento() { return horarioFechamento; }
        public String getDataPonto()         { return dataPonto; }
        public String getStatus()            { return status; }

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

    /** Lista todos os funcionários de uma empresa */
    public List<FuncionarioRow> listarFuncionarios(int idEmpresa) throws SQLException {
        List<FuncionarioRow> lista = new ArrayList<>();
        String sql = "SELECT idfuncionario, nomefuncionario, cpffuncionario, cargo, telefone, emailfuncionario " +
                     "FROM funcionario WHERE idempresa = ? ORDER BY nomefuncionario";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, idEmpresa);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            lista.add(new FuncionarioRow(
                rs.getInt("idfuncionario"),
                rs.getString("nomefuncionario"),
                rs.getString("cpffuncionario"),
                rs.getString("cargo"),
                rs.getString("telefone"),
                rs.getString("emailfuncionario")
            ));
        }
        rs.close();
        stmt.close();
        return lista;
    }

    /**
     * Lista funcionários de uma empresa com filtros opcionais por nome, CPF e cargo.
     * Os parâmetros podem ser nulos ou vazios para ignorar o filtro.
     */
    public List<FuncionarioRow> listarFuncionariosFiltrados(int idEmpresa,
                                                             String nome,
                                                             String cpf,
                                                             String cargo) throws SQLException {
        List<FuncionarioRow> lista = new ArrayList<>();

        StringBuilder sql = new StringBuilder(
            "SELECT idfuncionario, nomefuncionario, cpffuncionario, cargo, telefone, emailfuncionario " +
            "FROM funcionario WHERE idempresa = ?"
        );
        if (nome  != null && !nome.isBlank())  sql.append(" AND nomefuncionario LIKE ?");
        if (cpf   != null && !cpf.isBlank())   sql.append(" AND cpffuncionario  LIKE ?");
        if (cargo != null && !cargo.isBlank())  sql.append(" AND cargo           LIKE ?");
        sql.append(" ORDER BY nomefuncionario");

        PreparedStatement stmt = conn.prepareStatement(sql.toString());
        int idx = 1;
        stmt.setInt(idx++, idEmpresa);
        if (nome  != null && !nome.isBlank())  stmt.setString(idx++, "%" + nome  + "%");
        if (cpf   != null && !cpf.isBlank())   stmt.setString(idx++, "%" + cpf   + "%");
        if (cargo != null && !cargo.isBlank())  stmt.setString(idx++, "%" + cargo + "%");

        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            lista.add(new FuncionarioRow(
                rs.getInt("idfuncionario"),
                rs.getString("nomefuncionario"),
                rs.getString("cpffuncionario"),
                rs.getString("cargo"),
                rs.getString("telefone"),
                rs.getString("emailfuncionario")
            ));
        }
        rs.close();
        stmt.close();
        return lista;
    }

    public List<PontoRow> listarPontos(int idEmpresa) throws SQLException {
        List<PontoRow> lista = new ArrayList<>();
        String sql = "SELECT p.idponto, f.nomefuncionario, p.horario, p.horariofechamento, p.status, CONVERT(varchar,p.dataponto,103) as dataponto " +
                     "FROM ponto p " +
                     "INNER JOIN funcionario f ON f.idfuncionario = p.idfuncionario " +
                     "WHERE f.idempresa = ? " +
                     "ORDER BY p.dataponto DESC, p.horario DESC";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, idEmpresa);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            lista.add(new PontoRow(
                rs.getInt("idponto"),
                rs.getString("nomefuncionario"),
                rs.getString("horario"),
                rs.getString("horariofechamento") != null ? rs.getString("horariofechamento") : "--:--",
                rs.getString("dataponto"),
                rs.getString("status")
            ));
        }
        rs.close();
        stmt.close();
        return lista;
    }

    /**
     * Lista pontos de uma empresa com filtros opcionais por nome do funcionário, data e horário.
     * Os parâmetros podem ser nulos ou vazios para ignorar o filtro.
     */
    public List<PontoRow> listarPontosFiltrados(int idEmpresa,
                                                 String nomeFuncionario,
                                                 String data,
                                                 String hora) throws SQLException {
        List<PontoRow> lista = new ArrayList<>();

        StringBuilder sql = new StringBuilder(
            "SELECT p.idponto, f.nomefuncionario, p.horario, p.horariofechamento, p.status, CONVERT(varchar,p.dataponto,103) as dataponto " +
            "FROM ponto p " +
            "INNER JOIN funcionario f ON f.idfuncionario = p.idfuncionario " +
            "WHERE f.idempresa = ?"
        );
        if (nomeFuncionario != null && !nomeFuncionario.isBlank())
            sql.append(" AND f.nomefuncionario LIKE ?");
        if (data != null && !data.isBlank())
            sql.append(" AND CONVERT(varchar,p.dataponto,103) LIKE ?");
        if (hora != null && !hora.isBlank())
            sql.append(" AND p.horario LIKE ?");
        sql.append(" ORDER BY p.dataponto DESC, p.horario DESC");

        PreparedStatement stmt = conn.prepareStatement(sql.toString());
        int idx = 1;
        stmt.setInt(idx++, idEmpresa);
        if (nomeFuncionario != null && !nomeFuncionario.isBlank())
            stmt.setString(idx++, "%" + nomeFuncionario + "%");
        if (data != null && !data.isBlank())
            stmt.setString(idx++, "%" + data + "%");
        if (hora != null && !hora.isBlank())
            stmt.setString(idx++, "%" + hora + "%");

        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            lista.add(new PontoRow(
                rs.getInt("idponto"),
                rs.getString("nomefuncionario"),
                rs.getString("horario"),
                rs.getString("horariofechamento") != null ? rs.getString("horariofechamento") : "--:--",
                rs.getString("dataponto"),
                rs.getString("status")
            ));
        }
        rs.close();
        stmt.close();
        return lista;
    }

    /** Conta total de funcionários */
    public int contarFuncionarios(int idEmpresa) throws SQLException {
        String sql = "SELECT COUNT(*) FROM funcionario WHERE idempresa = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, idEmpresa);
        ResultSet rs = stmt.executeQuery();
        rs.next();
        int total = rs.getInt(1);
        rs.close(); stmt.close();
        return total;
    }

    /** Conta total de pontos batidos hoje */
    public int contarPontosHoje(int idEmpresa) throws SQLException {
        String sql = "SELECT COUNT(*) FROM ponto p " +
                     "INNER JOIN funcionario f ON f.idfuncionario = p.idfuncionario " +
                     "WHERE f.idempresa = ? AND p.dataponto = CAST(GETDATE() AS date)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, idEmpresa);
        ResultSet rs = stmt.executeQuery();
        rs.next();
        int total = rs.getInt(1);
        rs.close(); stmt.close();
        return total;
    }

    /** Soma as horas trabalhadas do mês atual para toda a empresa. */
    public String somarHorasMes(int idEmpresa) throws SQLException {
        String sql = "SELECT ISNULL(SUM( " +
                     "  CASE WHEN CAST(p.horariofechamento as TIME) < CAST(p.horario as TIME) " +
                     "  THEN DATEDIFF(MINUTE, CAST(p.horario as TIME), CAST(p.horariofechamento as TIME)) + 1440 " +
                     "  ELSE DATEDIFF(MINUTE, CAST(p.horario as TIME), CAST(p.horariofechamento as TIME)) END " +
                     "), 0) " +
                     "FROM ponto p " +
                     "INNER JOIN funcionario f ON f.idfuncionario = p.idfuncionario " +
                     "WHERE f.idempresa = ? AND p.status = 'FECHADO' " +
                     "AND MONTH(p.dataponto) = MONTH(GETDATE()) AND YEAR(p.dataponto) = YEAR(GETDATE())";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, idEmpresa);
        ResultSet rs = stmt.executeQuery();
        rs.next();
        int totalMinutos = rs.getInt(1);
        rs.close(); stmt.close();
        
        long h = totalMinutos / 60;
        long m = totalMinutos % 60;
        return String.format("%02dh %02dm", h, m);
    }

    /** Atualiza os dados de um ponto (Edição Manual pelo RH) */
    public void atualizarPonto(int idPonto, String dataPonto, String horario, String horarioFechamento, String status) throws SQLException {
        String sql = "UPDATE ponto SET dataponto = CONVERT(date, ?, 103), horario = ?, horariofechamento = ?, status = ? WHERE idponto = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, dataPonto);
        stmt.setString(2, horario);
        if (horarioFechamento == null || horarioFechamento.isBlank() || horarioFechamento.equals("--:--")) {
            stmt.setNull(3, java.sql.Types.VARCHAR);
        } else {
            stmt.setString(3, horarioFechamento);
        }
        stmt.setString(4, status);
        stmt.setInt(5, idPonto);
        stmt.executeUpdate();
        stmt.close();
    }
}
