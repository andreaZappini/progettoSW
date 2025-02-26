import java.util.ArrayList;

public class Luogo extends Impostazioni {
    private String codiceLuogo;
    private String descrizione;
    private String collocazioneGeografica;
    private ArrayList<TipoVisita> visita = new ArrayList<TipoVisita>();

    public Luogo(String ambitoTerritoriale, int numeroMassimoIscrittiFruitore, Elenco<Luogo> elencoLuoghi, String codiceLuogo, String descrizione, String collocazioneGeografica, ArrayList<TipoVisita> visita) {
        super(ambitoTerritoriale, numeroMassimoIscrittiFruitore, elencoLuoghi);
        this.codiceLuogo = codiceLuogo;
        this.descrizione = descrizione;
        this.collocazioneGeografica = collocazioneGeografica;
        this.visita = visita;
    }
}