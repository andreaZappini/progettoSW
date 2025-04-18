package model;

public class Visita {
    
    private StatiVisita stato;
    private String data;
    private String mese;
    private TipoVisita tipo;

    public Visita(String data, String mese, TipoVisita tipo) {
        this.stato = StatiVisita.VISITA_PROPOSTA;
        this.data = data;
        this.mese = mese;
        this.tipo = tipo;
    }

    public StatiVisita getStato() {
        return stato;
    }

    public String getData() {
        return data;
    }

    public String getMese() {
        return mese;
    }

    public TipoVisita getTipo() {
        return tipo;
    }

    public void cambiaStato( ) {
       //TODO: implement
    }
}

