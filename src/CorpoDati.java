public class CorpoDati{
    private String ambitoTerritoriale;
    private int numeroMassimoIscrittiFruitore;
    private Elenco<Luogo> elencoLuoghi;

    public CorpoDati(String ambitoTerritoriale, int numeroMassimoIscrittiFruitore) {
        this.ambitoTerritoriale = ambitoTerritoriale;
        this.numeroMassimoIscrittiFruitore = numeroMassimoIscrittiFruitore;
        this.elencoLuoghi = new Elenco<>();
    }

    public String getAmbitoTerritoriale() {
        return  this.ambitoTerritoriale;
    }

    public int getNumeroMassimoIscrittiFruitore() {
        return this.numeroMassimoIscrittiFruitore;
    }

    public void setNumeroMassimoIscrittiFruitore(int n){
        this.numeroMassimoIscrittiFruitore = n;
    }

    public void aggiungiLuogo(Luogo l){
        this.elencoLuoghi.aggiungi(l);
    }

    public Elenco<Luogo> getElencoLuoghi(){
        return this.elencoLuoghi;
    }

    public void ripristinaElenco(Elenco<Luogo> elenco){
        this.elencoLuoghi = elenco;
    }
}