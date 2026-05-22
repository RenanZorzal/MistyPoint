package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import model.Empresa;

public class EmpresaDAO {

    private Connection conn;

    public EmpresaDAO(Connection conn) {
        this.conn = conn;
    }

    /** Retorna true se o CNPJ já existe na tabela empresa */
    public boolean existeCnpj(String cnpj) throws SQLException {
        String sql = "SELECT COUNT(*) FROM empresa WHERE cnpj = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, cnpj);
        ResultSet rs = stmt.executeQuery();
        rs.next();
        int count = rs.getInt(1);
        rs.close();
        stmt.close();
        return count > 0;
    }

    /** Retorna true se o e-mail já existe na tabela empresa */
    public boolean existeEmail(String email) throws SQLException {
        String sql = "SELECT COUNT(*) FROM empresa WHERE emailempresa = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, email);
        ResultSet rs = stmt.executeQuery();
        rs.next();
        int count = rs.getInt(1);
        rs.close();
        stmt.close();
        return count > 0;
    }

    /**
     * Insere a empresa e retorna o IDEMPRESA gerado.
     */
    public int inserir(Empresa empresa) throws SQLException {
        String sql = "INSERT INTO empresa (cnpj, razaosocial, nomefantasia, inscricaoestadual, " +
                     "nomeempresa, emailempresa, senhaempresa) VALUES (?,?,?,?,?,?,?);";
        PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        stmt.setString(1, empresa.getCnpj());
        stmt.setString(2, empresa.getRazaoSocial());
        stmt.setString(3, empresa.getNomeFantasia());
        stmt.setString(4, empresa.getInscricaoEstadual());
        stmt.setString(5, empresa.getNomeEmpresa());
        stmt.setString(6, empresa.getEmailEmpresa());
        stmt.setString(7, empresa.getSenhaEmpresa());
        stmt.executeUpdate();

        int idGerado = -1;
        ResultSet rs = stmt.getGeneratedKeys();
        if (rs.next()) {
            idGerado = rs.getInt(1);
        }
        rs.close();
        stmt.close();
        return idGerado;
    }

    /**
     * Insere o endereço da empresa na tabela ENDERECOEMPRESA.
     */
    public void inserirEnderecoEmpresa(String complemento, int numero,
                                       int idLogradouro, int idEmpresa) throws SQLException {
        String sql = "INSERT INTO enderecoempresa (complementoempresa, numeroempresa, idlogradouro, idempresa) " +
                     "VALUES (?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, complemento);
        stmt.setInt(2, numero);
        stmt.setInt(3, idLogradouro);
        stmt.setInt(4, idEmpresa);
        stmt.executeUpdate();
        stmt.close();
    }

    /** Retorna true se o e-mail e senha batem com um registro existente */
    public boolean autenticar(String email, String senha) throws SQLException {
        String sql = "SELECT COUNT(*) FROM empresa WHERE emailempresa = ? AND senhaempresa = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, email);
        stmt.setString(2, senha);
        ResultSet rs = stmt.executeQuery();
        rs.next();
        int count = rs.getInt(1);
        rs.close();
        stmt.close();
        return count > 0;
    }

    /**
     * Autentica e retorna o objeto Empresa completo (com id).
     * Retorna null se e-mail/senha forem incorretos.
     */
    public Empresa autenticarRetornarEmpresa(String email, String senha) throws SQLException {
        String sql = "SELECT idempresa, cnpj, razaosocial, nomefantasia, inscricaoestadual, " +
                     "nomeempresa, emailempresa, senhaempresa " +
                     "FROM empresa WHERE emailempresa = ? AND senhaempresa = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, email);
        stmt.setString(2, senha);
        ResultSet rs = stmt.executeQuery();
        Empresa emp = null;
        if (rs.next()) {
            emp = new Empresa(
                rs.getString("cnpj"),
                rs.getString("razaosocial"),
                rs.getString("nomefantasia"),
                rs.getString("inscricaoestadual"),
                rs.getString("nomeempresa"),
                rs.getString("emailempresa"),
                rs.getString("senhaempresa")
            );
            emp.setIdEmpresa(rs.getInt("idempresa"));
        }
        rs.close();
        stmt.close();
        return emp;
    }
}

