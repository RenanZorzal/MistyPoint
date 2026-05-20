package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Bairro;
import model.Cidade;
import model.Estado;
import model.Logradouro;

public class EnderecoDAO {
    private Connection conn;

    public EnderecoDAO(Connection conn) {
        this.conn = conn;
    }

    /** Lista todos os estados ordenados por nome */
    public List<Estado> listarEstados() throws SQLException {
        List<Estado> lista = new ArrayList<>();
        String sql = "SELECT idestado, sigla, nomeestado FROM estado ORDER BY nomeestado";
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            lista.add(new Estado(rs.getInt("idestado"), rs.getString("sigla"), rs.getString("nomeestado")));
        }
        rs.close();
        stmt.close();
        return lista;
    }

    /** Lista cidades de um estado */
    public List<Cidade> listarCidadesPorEstado(int idEstado) throws SQLException {
        List<Cidade> lista = new ArrayList<>();
        String sql = "SELECT idcidade, nomecidade, idestado FROM cidade WHERE idestado = ? ORDER BY nomecidade";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, idEstado);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            lista.add(new Cidade(rs.getInt("idcidade"), rs.getString("nomecidade"), rs.getInt("idestado")));
        }
        rs.close();
        stmt.close();
        return lista;
    }

    /** Lista bairros de uma cidade */
    public List<Bairro> listarBairrosPorCidade(int idCidade) throws SQLException {
        List<Bairro> lista = new ArrayList<>();
        String sql = "SELECT idbairro, nomebairro, idcidade FROM bairro WHERE idcidade = ? ORDER BY nomebairro";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, idCidade);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            lista.add(new Bairro(rs.getInt("idbairro"), rs.getString("nomebairro"), rs.getInt("idcidade")));
        }
        rs.close();
        stmt.close();
        return lista;
    }

    /** Lista logradouros de um bairro */
    public List<Logradouro> listarLogradourosPorBairro(int idBairro) throws SQLException {
        List<Logradouro> lista = new ArrayList<>();
        String sql = "SELECT idlogradouro, nomelogradouro, idbairro FROM logradouro WHERE idbairro = ? ORDER BY nomelogradouro";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, idBairro);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            lista.add(new Logradouro(rs.getInt("idlogradouro"), rs.getString("nomelogradouro"), rs.getInt("idbairro")));
        }
        rs.close();
        stmt.close();
        return lista;
    }

    /**
     * Insere um novo endereço e retorna o IDENDERECO gerado.
     */
    public int inserirEndereco(String complemento, int numero, int idLogradouro) throws SQLException {
        String sql = "INSERT INTO endereco (complemento, numero, idlogradouro) VALUES (?, ?, ?); SELECT SCOPE_IDENTITY();";
        PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        stmt.setString(1, complemento);
        stmt.setInt(2, numero);
        stmt.setInt(3, idLogradouro);
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
}
