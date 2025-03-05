import java.io.File;

public class JSONUtilities {
    
    public static <T> void scriviJSON(T oggetto, File file){
        oggetto.salvaSuJSON(file);
    }
}
