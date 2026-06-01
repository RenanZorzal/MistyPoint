package model;

public class Empresa {

    private int    idEmpresa;
    private String cnpj;
    private String razaoSocial;
    private String nomeFantasia;
    private String inscricaoEstadual;
    private String nomeEmpresa;
    private String emailEmpresa;
    private String senhaEmpresa;

    // ── Endereço flat ───────────────────────────────────────────────
    private String logradouro;
    private int    numero;
    private String complemento;
    private String cep;
    private String cidade;
    private int    idEstado;

    public Empresa(String cnpj, String razaoSocial, String nomeFantasia,
                   String inscricaoEstadual, String nomeEmpresa,
                   String emailEmpresa, String senhaEmpresa) {
        setCnpj(cnpj);
        setRazaoSocial(razaoSocial);
        setNomeFantasia(nomeFantasia);
        setInscricaoEstadual(inscricaoEstadual);
        setNomeEmpresa(nomeEmpresa);
        setEmailEmpresa(emailEmpresa);
        setSenhaEmpresa(senhaEmpresa);
    }

    public Empresa() {}

    // ── Getters / Setters básicos ───────────────────────────────────

    public int    getIdEmpresa()                      { return idEmpresa; }
    public void   setIdEmpresa(int idEmpresa)         { this.idEmpresa = idEmpresa; }

    public String getCnpj()                           { return cnpj; }
    public void   setCnpj(String cnpj)                { this.cnpj = cnpj; }

    public String getRazaoSocial()                    { return razaoSocial; }
    public void   setRazaoSocial(String r)            { this.razaoSocial = r; }

    public String getNomeFantasia()                   { return nomeFantasia; }
    public void   setNomeFantasia(String n)           { this.nomeFantasia = n; }

    public String getInscricaoEstadual()              { return inscricaoEstadual; }
    public void   setInscricaoEstadual(String i)      { this.inscricaoEstadual = i; }

    public String getNomeEmpresa()                    { return nomeEmpresa; }
    public void   setNomeEmpresa(String n)            { this.nomeEmpresa = n; }

    public String getEmailEmpresa()                   { return emailEmpresa; }
    public void   setEmailEmpresa(String e)           { this.emailEmpresa = e; }

    public String getSenhaEmpresa()                   { return senhaEmpresa; }
    public void   setSenhaEmpresa(String s)           { this.senhaEmpresa = s; }

    // ── Getters / Setters de endereço ───────────────────────────────

    public String getLogradouro()                     { return logradouro; }
    public void   setLogradouro(String logradouro)    { this.logradouro = logradouro; }

    public int    getNumero()                         { return numero; }
    public void   setNumero(int numero)               { this.numero = numero; }

    public String getComplemento()                    { return complemento; }
    public void   setComplemento(String complemento)  { this.complemento = complemento; }

    public String getCep()                            { return cep; }
    public void   setCep(String cep)                  { this.cep = cep; }

    public String getCidade()                         { return cidade; }
    public void   setCidade(String cidade)            { this.cidade = cidade; }

    public int    getIdEstado()                       { return idEstado; }
    public void   setIdEstado(int idEstado)           { this.idEstado = idEstado; }
}
