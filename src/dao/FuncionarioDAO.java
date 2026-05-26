package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.Funcionario;

public class FuncionarioDAO {
	private Connection conn;
	
	public FuncionarioDAO(Connection conn) {
		this.conn = conn;
	}

	/** Retorna true se o CPF já existe na tabela funcionario */
	public boolean existeCpf(String cpf) throws SQLException {
		String sql = "SELECT COUNT(*) FROM funcionario WHERE cpffuncionario = ?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setString(1, cpf);
		ResultSet rs = stmt.executeQuery();
		rs.next();
		int count = rs.getInt(1);
		rs.close();
		stmt.close();
		return count > 0;
	}

	/** Retorna true se o e-mail já existe na tabela funcionario */
	public boolean existeEmail(String email) throws SQLException {
		String sql = "SELECT COUNT(*) FROM funcionario WHERE emailfuncionario = ?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setString(1, email);
		ResultSet rs = stmt.executeQuery();
		rs.next();
		int count = rs.getInt(1);
		rs.close();
		stmt.close();
		return count > 0;
	}
	
	public void inserir(Funcionario funcionario) throws SQLException {
		String sql = "INSERT INTO funcionario (nomefuncionario,cpffuncionario,cargo,telefone,emailfuncionario, "
				+ "senhafuncionario,idendereco,idempresa) VALUES (?,?,?,?,?,?,?,?)";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setString(1, funcionario.getNome());
		stmt.setString(2, funcionario.getCpf());
		stmt.setString(3, funcionario.getCargo());
		stmt.setString(4, funcionario.getTelefone());
		stmt.setString(5, funcionario.getEmail());
		stmt.setString(6, funcionario.getSenha());
		stmt.setInt(7, funcionario.getIdEndereco());
		stmt.setInt(8, funcionario.getIdEmpresa());
		
		stmt.executeUpdate();
		stmt.close();
	}

	/** Atualiza os dados de um funcionário pelo id */
	public void atualizar(int id, String nome, String cargo, String telefone, String email) throws SQLException {
		String sql = "UPDATE funcionario SET nomefuncionario=?, cargo=?, telefone=?, emailfuncionario=? WHERE idfuncionario=?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setString(1, nome);
		stmt.setString(2, cargo);
		stmt.setString(3, telefone);
		stmt.setString(4, email);
		stmt.setInt(5, id);
		stmt.executeUpdate();
		stmt.close();
	}

	/** Exclui um funcionário pelo id */
	public void excluir(int id) throws SQLException {
		String sql = "DELETE FROM funcionario WHERE idfuncionario=?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setInt(1, id);
		stmt.executeUpdate();
		stmt.close();
	}

	/** Retorna true se o e-mail e senha batem com um registro existente */
	public boolean autenticar(String email, String senha) throws SQLException {
		String sql = "SELECT COUNT(*) FROM funcionario WHERE emailfuncionario = ? AND senhafuncionario = ?";
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
	 * Autentica e retorna o objeto Funcionario completo (com id).
	 * Retorna null se e-mail/senha forem incorretos.
	 */
	public Funcionario autenticarRetornarFuncionario(String email, String senha) throws SQLException {
		String sql = "SELECT idfuncionario, nomefuncionario, cpffuncionario, cargo, telefone, "
				 + "emailfuncionario, senhafuncionario, idendereco, idempresa "
				 + "FROM funcionario WHERE emailfuncionario = ? AND senhafuncionario = ?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setString(1, email);
		stmt.setString(2, senha);
		ResultSet rs = stmt.executeQuery();
		Funcionario f = null;
		if (rs.next()) {
			f = new Funcionario();
			f.setIdFuncionario(rs.getInt("idfuncionario"));
			f.setNome(rs.getString("nomefuncionario"));
			f.setCpf(rs.getString("cpffuncionario"));
			f.setCargo(rs.getString("cargo"));
			f.setTelefone(rs.getString("telefone"));
			f.setEmail(rs.getString("emailfuncionario"));
			f.setSenha(rs.getString("senhafuncionario"));
			f.setIdEndereco(rs.getInt("idendereco"));
			f.setIdEmpresa(rs.getInt("idempresa"));
		}
		rs.close();
		stmt.close();
		return f;
	}
}
