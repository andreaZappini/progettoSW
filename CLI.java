import java.io.File;
import java.util.Scanner;

public class CLI {
    
    private static boolean working = true;
    public static void start(Elenco<Utente> elencoUtenti, CorpoDati c) {

        //veriFIGA consistenza dati XML e elencoUtenti

        System.out.println("Benvenuto!");
        File file = new File("fileXML/configuratore1.xml");
        try(Scanner in = new Scanner(System.in)){
            if(file.exists()){
                Elenco<Utente> elencop = XMLUtilities.leggiXML(
                file, 
                "configuratore", 
                elemento -> {
                    String username = elemento.getElementsByTagName("username").item(0).getTextContent();
                    String password = elemento.getElementsByTagName("password").item(0).getTextContent();
                    //boolean primoAccesso = Boolean.parseBoolean(
                        //elemento.getElementsByTagName("primoAccesso").item(0).getTextContent());
                    return new Configuratore(username, password);
                });
                //solo per debug elencop.visualizza();
                elencoUtenti.aggiungi(elencop);
                primaConfigurazione(elencoUtenti, in, c);
            }else{
                //recuoperare dati da file
            }
            while(working){
                String ruolo = login(elencoUtenti, in);
                switch (ruolo) {
                    case "Configuratore":
                        azioniConfiguratore(elencoUtenti, in);
                        break;
                    case "Volontario":
                        //azioniVolontario(elencoUtenti, in);
                        break;
                    case "Fruitore":
                        //azioniFruitore(elencoUtenti, in);
                        break;
                    default:
                        break;
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        
    }

    private static void primaConfigurazione(Elenco<Utente> elencoUtenti, 
                                            Scanner in, CorpoDati c) throws Exception{
            
        login(elencoUtenti, in);
        creazioneCorpoDati(in, c);

    }
        
    //aggiungi metodo sign in
    private static String login(Elenco<Utente> elencoUtenti, 
                        Scanner in) throws Exception{
                            
        Utente x = null;
        String username = null;
        String password = null;

        int tentativi = 3;
        while(tentativi > 0){
            System.out.println("username:");
            username = in.next();
            System.out.println("password:");
            password = in.next();

            if(!elencoUtenti.contiene(username)){
                System.out.println("Utente non trovato");
                tentativi-=1;
            }else{
                x = elencoUtenti.getElementByKey(username);
                if(x.controllaPassword(password)){
                    System.out.println("Accesso consentito");
                    if(x.getPrimoAccesso()){
                        cambiaPSW(in, x);
                    }
                }
                break;
            }
        }
        if(x == null){
            System.out.println("Tentativi esauriti");
            return null;
        }
        return x.getClass().getSimpleName();
    }

    private static void cambiaPSW(Scanner in, Utente x) {
        System.out.println("Primo accesso, modifica password");
        System.out.println("Nuova password:");
        String nuovaPassword = in.next();
        x.setPassword(nuovaPassword);
        x.setPrimoAccesso();
    }

    private static void aggiungiConfiguratore(Scanner in, Elenco<Utente> elencoUtenti){
        System.out.println("Inserisci username:");
        String username = in.next();
        System.out.println("Inserisci password:");
        String password = in.next();
        Utente c = new Configuratore(username, password);
        elencoUtenti.aggiungi(c);

        //XMLUtilities.scriviXML(file, c);
    }

    private static void azioniConfiguratore(Elenco<Utente> elencoUtenti, Scanner in) {
        Boolean continua = true;
        while(continua){
            System.out.println("Benvenuto Configuratore");
            System.out.println("1. Aggiungi Configuratore");
            System.out.println("2. Visualizza elenco utenti");
            System.out.println("3. Aggiungi visita a luogo");
            System.out.println("4. Logout");
            System.out.println("5.Chiudi applicazione");
            System.out.println("Scelta:");
            int scelta = in.nextInt();
            switch (scelta) {
                case 1:
                    aggiungiConfiguratore(in, elencoUtenti);
                    break;
                case 2:
                    elencoUtenti.visualizza();
                    break;
                case 3:
                    //aggiungiVisita(in, elencoUtenti);
                    break;
                case 4:
                    continua = false;
                    break;
                case 5:
                    working = false;
                    continua = false;
                    break;
                default:
                    break;
            }
        }
    }

    private static void creazioneCorpoDati(Scanner in, CorpoDati c) {
        System.out.println("Inserisci ambito territoriale:");
        String ambitoTerritoriale = in.next();
        System.out.println("Inserisci numero massimo iscritti fruitore:");
        int numeroMassimoIscrittiFruitore = in.nextInt();
        c = new CorpoDati(ambitoTerritoriale, numeroMassimoIscrittiFruitore);
        do{
            aggiungiLuogo(in, c);
            System.out.println("Vuoi inserire un altro luogo? (s/n)");
            String risposta = in.next();
            if(!risposta.equals("s")){
                break;
            }
        }while(true);
    }

    private static void aggiungiLuogo(Scanner in, CorpoDati c) {
        System.out.println("Inserisci luogo:");
        System.out.println("codice: ");
        String codiceLuogo = in.next();
        System.out.println("descrizione: ");
        String descrizione = in.next();
        System.out.println("collocazione geografica: ");
        String collocazioneGeografica = in.next();
        Luogo l = new Luogo(codiceLuogo, descrizione, collocazioneGeografica);
        c.aggiungiLuogo(l);
    }
}