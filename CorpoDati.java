public abstract class CorpoDati{
    private String ambitoTerritoriale;
    private int numeroMassimoIscrittiFruitore;
    private Elenco<Luogo> elencoLuoghi;

    public CorpoDati(String ambitoTerritoriale, int numeroMassimoIscrittiFruitore, Elenco<Luogo> elencoLuoghi) {
        this.ambitoTerritoriale = ambitoTerritoriale;
        this.numeroMassimoIscrittiFruitore = numeroMassimoIscrittiFruitore;
        this.elencoLuoghi = elencoLuoghi;
    }

}