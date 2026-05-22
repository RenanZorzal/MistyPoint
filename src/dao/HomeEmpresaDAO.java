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
        public String dataPonto;

        public PontoRow(int id, String nomeFuncionario, String horario, String dataPonto) {
            this.id              = id;
            this.nomeFuncionario = nomeFuncionario;
            this.horario         = horario;
            this.dataPonto       = dataPonto;
        }

        public int    getId()              { return id; }
        public String getNomeFuncionario() { return nomeFuncionario; }
        public String getHorario()         { return horario; }
        public String getDataPonto()       { return dataPonto; }
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

    /** Lista todos os pontos batidos pelos funcionários de uma empresa */
    public List<PontoRow> listarPontos(int idEmpresa) throws SQLException {
        List<PontoRow> lista = new ArrayList<>();
        String sql = "SELECT p.idponto, f.nomefuncionario, p.horario, CONVERT(varchar,p.dataponto,103) as dataponto " +
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
                rs.getString("dataponto")
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

    /** Conta total de pontos no mês atual */
    public int contarPontosMes(int idEmpresa) throws SQLException {
        String sql = "SELECT COUNT(*) FROM ponto p " +
                     "INNER JOIN funcionario f ON f.idfuncionario = p.idfuncionario " +
                     "WHERE f.idempresa = ? " +
                     "AND MONTH(p.dataponto) = MONTH(GETDATE()) AND YEAR(p.dataponto) = YEAR(GETDATE())";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, idEmpresa);
        ResultSet rs = stmt.executeQuery();
        rs.next();
        int total = rs.getInt(1);
        rs.close(); stmt.close();
        return total;
    }
}
