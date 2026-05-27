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

    public Empresa(String cnpj, String razaoSocial, String nomeFantasia,
                   String inscricaoEstadual, String nomeEmpresa,
                   String emailEmpresa, String senhaEmpresa) {
        super();
        setCnpj(cnpj);
        setRazaoSocial(razaoSocial);
        setNomeFantasia(nomeFantasia);
        setInscricaoEstadual(inscricaoEstadual);
        setNomeEmpresa(nomeEmpresa);
        setEmailEmpresa(emailEmpresa);
        setSenhaEmpresa(senhaEmpresa);
    }

    public Empresa() {
        super();
    }

    public int getIdEmpresa() { 
    	return idEmpresa;
    }
    public void setIdEmpresa(int idEmpresa) {
    	this.idEmpresa = idEmpresa;
    }

    public String getCnpj() {
    	return cnpj;
    }
    public void setCnpj(String cnpj) {
    	this.cnpj = cnpj;
    }

    public String getRazaoSocial() {
    	return razaoSocial;
    }
    public void setRazaoSocial(String razaoSocial) {
    	this.razaoSocial = razaoSocial;
    }

    public String getNomeFantasia() {
    	return nomeFantasia;
    }
    public void setNomeFantasia(String nomeFantasia) {
    	this.nomeFantasia = nomeFantasia;
    }

    public String getInscricaoEstadual() {
    	return inscricaoEstadual;
    }
    public void setInscricaoEstadual(String inscricaoEstadual) {
    	this.inscricaoEstadual = inscricaoEstadual;
    }

    public String getNomeEmpresa() {
    	return nomeEmpresa;
    }
    public void setNomeEmpresa(String nomeEmpresa) {
    	this.nomeEmpresa = nomeEmpresa;
    }

    public String getEmailEmpresa() {
    	return emailEmpresa;
    }
    public void setEmailEmpresa(String emailEmpresa) {
    	this.emailEmpresa = emailEmpresa;
    }

    public String getSenhaEmpresa() {
    	return senhaEmpresa;
    }
    public void setSenhaEmpresa(String senhaEmpresa) {
    	this.senhaEmpresa = senhaEmpresa;
    }
}
