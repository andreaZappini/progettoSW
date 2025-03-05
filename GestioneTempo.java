import java.io.File;
import java.io.IOException;

public class GestioneTempo implements Runnable, JSONSerializable {

    private long start;
    
    public void run(){}
    public void start(){
        this.start = System.currentTimeMillis();
        System.out.println(this.start);
    }

    //return del tipo data[0] = mese, data[1] = giorno
    public int[] giorno(){
        long tempTrascorso = System.currentTimeMillis() - this.start;
        long millisToSec = tempTrascorso / 1000;
        long SecToDay = millisToSec / 10; //un giorno dura 10 secondi
        int mese = ((int)SecToDay) / 30 + 1;
        int giorno = ((int)SecToDay) % 30 + 1;
        int[] data = {mese, giorno};
        return data;
    }
    @Override
    public void salvaSuJSON(File file) {
       ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(file, this);
            System.out.println("File JSON salvato in: " + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}