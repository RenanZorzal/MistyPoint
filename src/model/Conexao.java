package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JOptionPane;





public class Conexao {
	private static String server ="jdbc:sqlserver://10.109.8.9:1433;";
	private static String banco	= "databaseName=DA123_Exerc_G08;";
	private static String usuario = "user=DA123_Exerc_G08;password=;encrypt=false;trustServerCertificate=true;loginTimeout=30;";
	
	public static Connection conexao;
	
	public static void conectar() { // Efetua a conexão
		try {
			// Conexão com o banco
			conexao = DriverManager.getConnection(server+banco+usuario);
			JOptionPane.showMessageDialog(null, "Conexão realizada com sucesso!");
			System.out.println("Conexão realizada com sucesso!");
		} catch (SQLException ex) {
			System.out.println("Erro!");
			JOptionPane.showMessageDialog(null, "Erro de conexão!\nERRO: "+ ex.getMessage());
		}
	}
	
	public static void desconectar() { // Fecha a conexão
		try {
		conexao.close(); // Fechar conexão
		JOptionPane.showMessageDialog(null, "Conexão fechada com sucesso!");
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, "Erro ao fechar a conexão!\nERRO: " + ex.getMessage());
		}
	}
	
}
