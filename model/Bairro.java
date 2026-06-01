package model;

public class Bairro {
    private int    idBairro;
    private String nomeBairro;
    private int    idCidade;

    public Bairro(int idBairro, String nomeBairro, int idCidade) {
        this.idBairro   = idBairro;
        this.nomeBairro = nomeBairro;
        this.idCidade   = idCidade;
    }

    public int getIdBairro(){
    	return idBairro;
    }
    public String getNomeBairro(){
    	return nomeBairro;
    }
    public int getIdCidade(){
    	return idCidade;
    }

    @Override
    public String toString() {
        return nomeBairro;
    }
}
