import java.util.ArrayList;

public class Luogo{
    private String codiceLuogo; //chiave
    private String descrizione;
    private String collocazioneGeografica;
    private ArrayList<TipoVisita> visite;

    public Luogo(String codiceLuogo, String descrizione, String collocazioneGeografica) {
        this.codiceLuogo = codiceLuogo;
        this.descrizione = descrizione;
        this.collocazioneGeografica = collocazioneGeografica;
        this.visite = new ArrayList<>();
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
        if(!visite.contains(v))
            this.visite.add(v);
        else
            throw new IllegalArgumentException("Tipo di visita gia' presente");
    }

    public String toStringVisite(){
        StringBuffer s = new StringBuffer();
        for(TipoVisita t : this.visite)
            s.append(t.getTitolo());

        return s.toString();
    }
}