package model;

public class Logradouro {
    private int    idLogradouro;
    private String nomeLogradouro;
    private int    idBairro;

    public Logradouro(int idLogradouro, String nomeLogradouro, int idBairro) {
        this.idLogradouro   = idLogradouro;
        this.nomeLogradouro = nomeLogradouro;
        this.idBairro       = idBairro;
    }

    public int    getIdLogradouro()   { return idLogradouro; }
    public String getNomeLogradouro() { return nomeLogradouro; }
    public int    getIdBairro()       { return idBairro; }

    @Override
    public String toString() {
        return nomeLogradouro;
    }
}
