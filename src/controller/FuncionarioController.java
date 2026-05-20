package controller;

import dao.EnderecoDAO;
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
	 * Salva o endereço e o funcionário no banco após verificar duplicatas de CPF e e-mail.
	 * Insere primeiro em ENDERECO, obtém o ID gerado e usa na inserção de FUNCIONARIO.
	 *
	 * @param complemento  texto do endereço (rua, ap, bloco, etc.)
	 * @param numero       número do imóvel
	 * @param idLogradouro ID do logradouro selecionado
	 * @throws IllegalArgumentException se CPF ou e-mail já estiverem cadastrados
	 * @throws Exception se ocorrer erro de banco de dados
	 */
	public void salvarComEndereco(String complemento, int numero, int idLogradouro) throws Exception {
		Conexao.conectar();
		try {
			EnderecoDAO endDao = new EnderecoDAO(Conexao.conexao);
			dao = new FuncionarioDAO(Conexao.conexao);

			if (dao.existeCpf(funcionario.getCpf())) {
				throw new IllegalArgumentException("CPF já cadastrado no sistema!");
			}
			if (dao.existeEmail(funcionario.getEmail())) {
				throw new IllegalArgumentException("E-mail já cadastrado no sistema!");
			}

			int idEndereco = endDao.inserirEndereco(complemento, numero, idLogradouro);
			if (idEndereco <= 0) {
				throw new Exception("Falha ao registrar endereço no banco de dados.");
			}

			funcionario.setIdEndereco(idEndereco);
			dao.inserir(funcionario);
		} finally {
			Conexao.desconectar();
		}
	}

	/**
	 * @deprecated Use salvarComEndereco(). Mantido por compatibilidade.
	 */
	@Deprecated
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

