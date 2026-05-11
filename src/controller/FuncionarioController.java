package controller;



import dao.FuncionarioDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import model.Conexao;
import model.Funcionario;

public class FuncionarioController {
	private FuncionarioDAO dao;
	private Funcionario funcionario;
	
	public FuncionarioController(Funcionario funcionario) {
		super();
		this.funcionario = funcionario;
	}
	
	@FXML
	public void salvarFuncionario() {
		try {
		Conexao.conectar();
		dao = new FuncionarioDAO(Conexao.conexao);
		
			dao.inserir(funcionario);
	
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setContentText("Funcionario salvo com sucesso!");
		alert.show();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Conexao.desconectar();
		}
	} 
	
}
