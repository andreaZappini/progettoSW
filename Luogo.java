import java.util.ArrayList;

public class Luogo{
    private String codiceLuogo; //chiave
    private String descrizione;
    private String collocazioneGeografica;
    private ArrayList<TipoVisita> visita;

    public Luogo(String codiceLuogo, String descrizione, String collocazioneGeografica) {
        this.codiceLuogo = codiceLuogo;
        this.descrizione = descrizione;
        this.collocazioneGeografica = collocazioneGeografica;
        this.visita = new ArrayList<>();
    }

    public String getCodiceLuogo() {
        return this.codiceLuogo;
    }
}