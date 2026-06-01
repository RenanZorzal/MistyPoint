package model;

public class Funcionario {
    private int    idFuncionario;
    private String nome;
    private String cpf;
    private String cargo;
    private String telefone;
    private String email;
    private String senha;
    private int    idEmpresa;

    // ── Endereço flat ───────────────────────────────────────────────
    private String logradouro;
    private int    numero;
    private String complemento;
    private String cep;
    private String cidade;
    private int    idEstado;

    public Funcionario(String nome, String cpf, String cargo, String telefone, String email, String senha) {
        setNome(nome);
        setCpf(cpf);
        setCargo(cargo);
        setTelefone(telefone);
        setEmail(email);
        setSenha(senha);
    }

    public Funcionario() {}

    // ── Getters / Setters básicos ───────────────────────────────────

    public int    getIdFuncionario()              { return idFuncionario; }
    public void   setIdFuncionario(int id)        { this.idFuncionario = id; }

    public String getNome()                       { return nome; }
    public void   setNome(String nome)            { this.nome = nome; }

    public String getCpf()                        { return cpf; }
    public void   setCpf(String cpf)              { this.cpf = cpf; }

    public String getCargo()                      { return cargo; }
    public void   setCargo(String cargo)          { this.cargo = cargo; }

    public String getTelefone()                   { return telefone; }
    public void   setTelefone(String telefone)    { this.telefone = telefone; }

    public String getEmail()                      { return email; }
    public void   setEmail(String email)          { this.email = email; }

    public String getSenha()                      { return senha; }
    public void   setSenha(String senha)          { this.senha = senha; }

    public int    getIdEmpresa()                  { return idEmpresa; }
    public void   setIdEmpresa(int idEmpresa)     { this.idEmpresa = idEmpresa; }

    // ── Getters / Setters de endereço ───────────────────────────────

    public String getLogradouro()                 { return logradouro; }
    public void   setLogradouro(String logradouro){ this.logradouro = logradouro; }

    public int    getNumero()                     { return numero; }
    public void   setNumero(int numero)           { this.numero = numero; }

    public String getComplemento()                { return complemento; }
    public void   setComplemento(String comp)     { this.complemento = comp; }

    public String getCep()                        { return cep; }
    public void   setCep(String cep)              { this.cep = cep; }

    public String getCidade()                     { return cidade; }
    public void   setCidade(String cidade)        { this.cidade = cidade; }

    public int    getIdEstado()                   { return idEstado; }
    public void   setIdEstado(int idEstado)       { this.idEstado = idEstado; }
}
