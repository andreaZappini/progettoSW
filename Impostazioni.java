public abstract class Impostazioni implements CLI {
    private String ambitoTerritoriale;
    private int numeroMassimoIscrittiFruitore;
    private ElencoLuoghi elencoLuoghi;

    public Impostazioni(String ambitoTerritoriale, int numeroMassimoIscrittiFruitore, ElencoLuoghi elencoLuoghi) {
        this.ambitoTerritoriale = ambitoTerritoriale;
        this.numeroMassimoIscrittiFruitore = numeroMassimoIscrittiFruitore;
        this.elencoLuoghi = elencoLuoghi;
    }

}