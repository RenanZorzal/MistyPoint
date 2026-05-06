package model;

public class Funcionario {
	private String nome;
	private String cpf;
	private String cargo;
	private String telefone;
	private String email;
	private String senha;
	
	public Funcionario(String nome, String cpf, String cargo, String telefone, String email, String senha) {
		super();
		setNome(nome);
		setCpf(cpf);
		setCargo(cargo);
		setTelefone(telefone);
		setEmail(email);
		setSenha(senha);
	}
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getCargo() {
		return cargo;
	}

	public void setCargo(String cargo) {
		this.cargo = cargo;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	
	
}
