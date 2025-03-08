public class Luogo{
    private String codiceLuogo; //chiave
    private String descrizione;
    private String collocazioneGeografica;
    private Elenco<TipoVisita> visite;

    public Luogo(String codiceLuogo, String descrizione, String collocazioneGeografica) {
        this.codiceLuogo = codiceLuogo;
        this.descrizione = descrizione;
        this.collocazioneGeografica = collocazioneGeografica;
        this.visite = new Elenco<>();
    }

    public String getCodiceLuogo() {
        return this.codiceLuogo;
    }

    public String getDescrizione(){
        return this.descrizione;
    }

    public String getCollocazione(){
        return this.collocazioneGeografica;
    }

    public void aggiungiAElencoVisite(TipoVisita v){
       this.visite.aggiungi(v);
    }

    public String toStringVisite(){
        return this.visite.toString();
    }

    @Override
    public String toString(){
        return this.codiceLuogo;
    }
}