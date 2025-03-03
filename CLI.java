import java.io.File;
import java.util.Scanner;

public class CLI {

    //salvataggio su XML!
    private static boolean creazioneCorpoDati = true;
    
    public static void start(Elenco<Utente> elencoUtenti, 
                            File file){

        //veriFIGA consistenza dati XML e elencoUtenti
        System.out.println("Benvenuto!");

        if(creazioneCorpoDati){
            primaConfigurazione(elencoUtenti, file);
        }
        
    }

    //cambia su XML il valore di creazioneCorpoDati
    private static void primaConfigurazione(Elenco<Utente> elencoUtenti, 
                                            File file){
        //possibile loop CONTROLLARE
        login(elencoUtenti, file);
        try(Scanner in = new Scanner(System.in)){
            System.out.println("Indicare l'AMBITO TERRITORIALE di competenza dell'applicazione: ");
            String ambito = in.next();
            int iscritti = maxIscrittiFruitore();
            //fai il terzo punto: elenco luoghi con tipi di visita
            }
        }catch(Exception e){
            e.printStackTrace();   
        }
        
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
                        switch(x.getClass().getName()){
                            case "Configuratore":
                                //TODO
                                break;
                            case "Volontario":
                                //TODO
                                break;
                            case "Fruitore":
                                //TODO
                                break;
                        }
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

    private static int maxIscrittiFruitore() {
        try(Scanner in = new Scanner(System.in)){
            int x = -1;
            System.out.println("Indicare il numero massimo di iscritti per fruitore: ");
            while(x = in.nextInt() < 1) {
                System.out.println("Inserire un numero maggiore di 0");
            }
            return x;
        }catch(Exception e){
            e.printStackTrace();
            return 0;
        }
    }
}