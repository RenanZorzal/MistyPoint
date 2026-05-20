package model;

public class Cidade {
    private int    idCidade;
    private String nomeCidade;
    private int    idEstado;

    public Cidade(int idCidade, String nomeCidade, int idEstado) {
        this.idCidade   = idCidade;
        this.nomeCidade = nomeCidade;
        this.idEstado   = idEstado;
    }

    public int    getIdCidade()   { return idCidade; }
    public String getNomeCidade() { return nomeCidade; }
    public int    getIdEstado()   { return idEstado; }

    @Override
    public String toString() {
        return nomeCidade;
    }
}
