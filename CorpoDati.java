import java.io.File;
import java.io.IOException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CorpoDati implements JSONSerializable {
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

    public Elenco<Luogo> getElenco(){
        return this.elencoLuoghi;
    }

    @Override
    public void salvaSuJSON(File file) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(file, this);
            System.out.println("File JSON salvato correttamente in: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}