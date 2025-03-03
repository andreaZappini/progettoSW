import java.io.File;
import java.util.Scanner;

public class CLI {

    private static boolean abilitazioneModificaCorpoDati = true;
    
    public static void start(Elenco<Utente> elencoUtenti, 
                            File file){

        //veriFIGA consistenza dati XML e elencoUtenti
        System.out.println("Benvenuto!");

        if(abilitazioneModificaCorpoDati){
            primaConfigurazione(elencoUtenti, file);
        }
        
    }

    private static void primaConfigurazione(Elenco<Utente> elencoUtenti, 
                                            File file){
            
        //possibile loop CONTROLLARE
        login(elencoUtenti, file);
        

    }

    //aggiungi metodo sign in
    private static void login(Elenco<Utente> elencoUtenti, 
                        File file) {

        try(Scanner in = new Scanner(System.in)){
            System.out.println("username:");
            String username = in.next();
            System.out.println("password:");
            String password = in.next();

            if(!elencoUtenti.contiene(username)){
                System.out.println("Utente non trovato");
                login(elencoUtenti, file);
            }else{
                Utente x = elencoUtenti.getElementByKey(username);
                if(x.controllaPassword(password)){
                    System.out.println("Accesso consentito");
                    if(x.getPrimoAccesso()){
                        cambiaPSW(file, in, x);
                        XMLUtilities.scriviXML(new File("fileXML/configuratori.xml"), x);
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private static void cambiaPSW(File file, Scanner in, Utente x) {
        System.out.println("Primo accesso, modifica password");
        System.out.println("Nuova password:");
        String nuovaPassword = in.next();
        x.setPassword(nuovaPassword);
        x.setPrimoAccesso();
        XMLUtilities.modificaXML(file, new String[]{x.getUsername(), x.getPassword()});
    }

    private static void aggiungiConfiguratore(Scanner in){
        File file = new File("fileXML/configuratori.xml");




    }
}