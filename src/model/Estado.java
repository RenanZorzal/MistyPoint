package model;

public class Estado {
    private int    idEstado;
    private String sigla;
    private String nomeEstado;

    public Estado(int idEstado, String sigla, String nomeEstado) {
        this.idEstado   = idEstado;
        this.sigla      = sigla;
        this.nomeEstado = nomeEstado;
    }

    public int getIdEstado(){ 
    	return idEstado;
    }
    public String getSigla(){ 
    	return sigla; 
    }
    public String getNomeEstado(){ 
    	return nomeEstado; 
    }

    @Override
    public String toString() {
        return sigla + " - " + nomeEstado;
    }
}
