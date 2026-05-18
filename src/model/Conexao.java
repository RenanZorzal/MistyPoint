package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;




public class Conexao {
	private static String server ="jdbc:sqlserver://10.109.8.9:1433;";
	private static String banco	= "databaseName=DA123_Exerc_G08;";
	private static String usuario = "user=DA123_Exerc_G08;password=;encrypt=false;trustServerCertificate=true;loginTimeout=30;";
	
	public static Connection conexao;
	
	public static void conectar() throws SQLException { // Efetua a conexão
		conexao = DriverManager.getConnection(server + banco + usuario);
		System.out.println("Conexão realizada com sucesso!");
	}
	
	public static void desconectar() { // Fecha a conexão
		try {
			if (conexao != null && !conexao.isClosed()) {
				conexao.close();
				System.out.println("Conexão fechada com sucesso!");
			}
		} catch (SQLException ex) {
			System.out.println("Erro ao fechar a conexão: " + ex.getMessage());
		}
	}
	
}
