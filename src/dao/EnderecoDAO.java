package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Cidade;
import model.Estado;

/**
 * DAO de endereço simplificado: apenas Estado e Cidade (para dropdowns).
 * As tabelas BAIRRO, LOGRADOURO e ENDERECO foram removidas do modelo de dados.
 */
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
            lista.add(new Estado(
                rs.getInt("idestado"),
                rs.getString("sigla"),
                rs.getString("nomeestado")
            ));
        }
        rs.close();
        stmt.close();
        return lista;
    }

    /** Lista cidades de um estado, ordenadas por nome */
    public List<Cidade> listarCidadesPorEstado(int idEstado) throws SQLException {
        List<Cidade> lista = new ArrayList<>();
        String sql = "SELECT idcidade, nomecidade, idestado FROM cidade WHERE idestado = ? ORDER BY nomecidade";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, idEstado);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            lista.add(new Cidade(
                rs.getInt("idcidade"),
                rs.getString("nomecidade"),
                rs.getInt("idestado")
            ));
        }
        rs.close();
        stmt.close();
        return lista;
    }
}
