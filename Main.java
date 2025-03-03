import java.io.File;

public class Main {
    
    public static void main(String[] args) throws Exception {

        Elenco<Utente> elencoUtenti = new Elenco<>();
        File file = new File("fileXML/configuratori.xml");

        elencoUtenti = XMLUtilities.leggiXML(file);

        
        
        
        

        CLI.start(elencoUtenti, file);
    }
}


