package controller;

import dao.FuncionarioDAO;
import model.Conexao;
import model.Funcionario;

public class FuncionarioController {
	private FuncionarioDAO dao;
	private Funcionario funcionario;
	
	public FuncionarioController(Funcionario funcionario) {
		super();
		this.funcionario = funcionario;
	}

	/**
	 * Salva o funcionário no banco após verificar duplicatas de CPF e e-mail.
	 * 
	 * @throws IllegalArgumentException se CPF ou e-mail já estiverem cadastrados
	 * @throws Exception se ocorrer erro de banco de dados
	 */
	public void salvarFuncionario() throws Exception {
		Conexao.conectar();
		try {
			dao = new FuncionarioDAO(Conexao.conexao);

			if (dao.existeCpf(funcionario.getCpf())) {
				throw new IllegalArgumentException("CPF já cadastrado no sistema!");
			}
			if (dao.existeEmail(funcionario.getEmail())) {
				throw new IllegalArgumentException("E-mail já cadastrado no sistema!");
			}

			dao.inserir(funcionario);
		} finally {
			Conexao.desconectar();
		}
	}
}

