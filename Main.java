import java.io.File;

public class Main {
    
    public static void main(String[] args) throws Exception {

        Elenco<Utente> elencoUtenti = new Elenco<>();
        File file = new File("fileXML/configuratori.xml");

        elencoUtenti = XMLUtilities.leggiXML(
            file, 
            "configuratore", 
            elemento -> {
                String username = elemento.getElementsByTagName("username").item(0).getTextContent();
                String password = elemento.getElementsByTagName("password").item(0).getTextContent();
                
                //boolean primoAccesso = Boolean.parseBoolean(
                    //elemento.getElementsByTagName("primoAccesso").item(0).getTextContent());
                return new Configuratore(username, password);
            }
        );

        elencoUtenti.visualizza();

        
        
        
        

        CLI.start(elencoUtenti);
    }
}


