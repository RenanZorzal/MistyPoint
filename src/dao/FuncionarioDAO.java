package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import model.Funcionario;

public class FuncionarioDAO {
	private Connection conn;
	
	public FuncionarioDAO(Connection conn) {
		this.conn = conn;
	}
	
	public void inserir(Funcionario funcionario) throws SQLException{
		String sql = "INSERT INTO funcionario (nomefuncionario,cpffuncionario,cargo,telefone,emailfuncionario, "
				+ "senhafuncionario,idendereco,idempresa) VALUES (?,?,?,?,?,?,1,1)";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setString(1,funcionario.getNome());
		stmt.setString(2,funcionario.getCpf());
		stmt.setString(3,funcionario.getCargo());
		stmt.setString(4,funcionario.getTelefone());
		stmt.setString(5,funcionario.getEmail());
		stmt.setString(6,funcionario.getSenha());
		
		stmt.executeUpdate();
		stmt.close();
	
		
	}
	
}
