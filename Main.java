import java.io.File;

public class Main {
    
    public static void main(String[] args) {

        Elenco<Utente> elencoUtenti = new Elenco<>();
        
        File file = new File("configuratore1.xml");

        CLI.start(elencoUtenti, file);
    }
}


