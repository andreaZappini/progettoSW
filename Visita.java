public class Visita {
    
    private StatiVisita stato;
    private String data;
    private String mese;
    private TipoVisita tipo;

    public Visita(StatiVisita stato, String data, String mese, TipoVisita tipo) {
        this.stato = stato;
        this.data = data;
        this.mese = mese;
        this.tipo = tipo;
    }

    public StatiVisita getStato() {
        return stato;
    }

    //??
    public void cambiaStato(StatiVisita stato) {
       //TODO: implement
    }
}

