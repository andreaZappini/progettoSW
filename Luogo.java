public class Luogo extends Impostazioni {
    private String codiceLuogo;
    private String descrizione;
    private String collocazioneGeografica;
    private arrayList<TipoVisita> visita = new arrayList<TipoVisita>();

    public Luogo(String ambitoTerritoriale, int numeroMassimoIscrittiFruitore, ElencoLuoghi elencoLuoghi, String codiceLuogo, String descrizione, String collocazioneGeografica, arrayList<TipoVisita> visita) {
        super(ambitoTerritoriale, numeroMassimoIscrittiFruitore, elencoLuoghi);
        this.codiceLuogo = codiceLuogo;
        this.descrizione = descrizione;
        this.collocazioneGeografica = collocazioneGeografica;
        this.visita = visita;
    }
}