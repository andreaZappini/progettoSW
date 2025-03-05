import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class CLI {
    
    private static boolean working = true;


    public static void start(Elenco<Utente> elencoUtenti, CorpoDati c, Elenco<TipoVisita> elencoTipiVisita) throws Exception {

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
                primaConfigurazione(elencoUtenti, elencoTipiVisita, in, c);
            }else{
                //recuoperare dati da file
            }
            while(working){
                String ruolo = login(elencoUtenti, in);
                switch (ruolo) {
                    case "Configuratore":
                        azioniConfiguratore(elencoUtenti, in, c);
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
            in.close();
        }catch(Exception e){
            e.printStackTrace();
        }   
    }

    private static void primaConfigurazione(Elenco<Utente> elencoUtenti,
                                            Elenco<TipoVisita> elencoTipiVisita,             
                                            Scanner in, CorpoDati c) throws Exception{
            
        login(elencoUtenti, in);
        creazioneCorpoDati(in, c, elencoTipiVisita, elencoUtenti);

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

    private static void azioniConfiguratore(Elenco<Utente> elencoUtenti, Scanner in, CorpoDati c) {
        Boolean continua = true;
        while(continua){
            System.out.println("Benvenuto Configuratore");
            System.out.println("1. Aggiungi Configuratore");
            System.out.println("2. Indicare le date per le attivita del mese i+3");
            System.out.println("3. Modificare numero massimo di iscritti per ogni fruitore");
            System.out.println("4. Visualizza Elenco Volontari");
            System.out.println("5. Visualizza Elenco Luoghi");
            System.out.println("6. Visualizza Elenco dei tipi di Visita Associati a un Luogo");
            System.out.println("7. Logout");
            System.out.println("8. Chiudi applicazione");
            System.out.println("Scelta:");
            int scelta = in.nextInt();
            switch (scelta) {
                case 1:
                    aggiungiConfiguratore(in, elencoUtenti);
                    break;
                case 2:
                    //indica  date
                    break;
                case 3:
                    System.out.println("indicare nuovo numero massimo di iscritti per persona: ");
                    int iscrittiMax = in.nextInt();
                    c.setNumeroMassimoIscrittiFruitore(iscrittiMax);
                    break;
                case 4:
                    Elenco<Volontario> elencoVolontari = elencoUtenti.getClassiUtente(Volontario.class);
                    elencoVolontari.visualizza();
                    break;
                case 5:
                    c.getElenco().visualizza();
                    break;
                case 6:
                    System.out.println("scegli il luogo di cui vuoi vedere le visite: ");
                    c.getElenco().visualizza();
                    String risposta = in.next();
                    c.getElenco().getElementByKey(risposta).toStringVisite();
                case 7:
                    continua = false;
                    break;
                case 8:
                    working = false;
                    continua = false;
                    break;
                default:
                    break;
            }
        }
    }

    private static void creazioneCorpoDati(Scanner in, CorpoDati c, Elenco<TipoVisita> elencoTipiVisita, Elenco<Utente> elencoUtenti) {
        System.out.println("Inserisci ambito territoriale:");
        String ambitoTerritoriale = in.next();
        System.out.println("Inserisci numero massimo iscritti per ogni fruitore:");
        int numeroMassimoIscrittiFruitore = in.nextInt();
        c = new CorpoDati(ambitoTerritoriale, numeroMassimoIscrittiFruitore);
        do{
            aggiungiLuogo(in, c, elencoTipiVisita, elencoUtenti);
            System.out.println("Vuoi inserire un altro luogo? (s/n)");
            String risposta = in.next();
            if(!risposta.equals("s")){
                break;
            }
        }while(true);
    }

    private static void aggiungiLuogo(Scanner in, CorpoDati c, Elenco<TipoVisita> elencoTipiVisita, Elenco<Utente> elencoUtenti) {
        System.out.println("Inserisci luogo:");
        if(elencoTipiVisita.getElenco().size() == 0){
            System.out.println("Prima deve esistere almeno un tipo di visita");
            creaTipoVisita(elencoUtenti, elencoTipiVisita, in, c);
        }
        System.out.println("codice: ");
        String codiceLuogo = in.next();
        System.out.println("descrizione: ");
        String descrizione = in.next();
        System.out.println("collocazione geografica: ");
        String collocazioneGeografica = in.next();
        Luogo l = new Luogo(codiceLuogo, descrizione, collocazioneGeografica);
        System.out.println("aggiungi almeno un tipo di visita");
        boolean continua = true;
        do{
            elencoTipiVisita.visualizza();
            String visita = in.next();
            l.aggiungiAElencoVisite(elencoTipiVisita.getElementByKey(visita));
            System.out.println("vuoi aggiungere altre visite? (y per continuare)");
            String risposta = in.next();
            if(!risposta.equalsIgnoreCase("y"))
                continua = false;
        }while(continua);

        c.aggiungiLuogo(l);
    }

    private static void creaTipoVisita(Elenco<Utente> elencoUtenti, 
                                        Elenco<TipoVisita> elencoTipiVisita, 
                                        Scanner in, CorpoDati c) {
        Elenco<Volontario> elencoVolontari = elencoUtenti.getClassiUtente(Volontario.class);
        if(elencoVolontari.getElenco().size() == 0){
            System.out.println("Prima deve esistere almeno un volontario");
            aggiungiVolontario(elencoUtenti, in);
        }
        System.out.println("Inserisci titolo:");
        String titolo = in.next();
        System.out.println("Inserisci descrizione:");
        String descrizione = in.next();
        System.out.println("Inserisci punto di incontro:");
        String puntoIncontro = in.next();
        System.out.println("Inserisci periodo dell'anno:");
        String periodoAnno = in.next();
        System.out.println("Inserisci giorni disponibili:");
        ArrayList<Giorni> giorniDisponibili = new ArrayList<>();
        Boolean continua = true;
        do{
            String gg = in.next();
            giorniDisponibili.add(Giorni.fromString(gg.toLowerCase()));
            System.out.println("vuoi aggiungere altri giorni? (y per continuare)");
            String risposta = in.next();
            if(!risposta.equalsIgnoreCase("y"))
                continua = false;
        }while(continua);
        
        System.out.println("Inserisci ora di inizio:");
        double oraInizio = in.nextDouble();
        System.out.println("Inserisci durata(minuti):");
        int durata = in.nextInt();
        System.out.println("Biglietto necessario? (si/no)");
        String bigliettoNecessario = in.next();
        System.out.println("numero minimo di partecipanti:");
        int minPartecipanti = in.nextInt();
        System.out.println("numero massimo di partecipanti:");
        int maxPartecipanti = in.nextInt();
        Elenco<Volontario> elencoVolontariVisita = new Elenco<>();
        continua = true;
        do{
            System.out.println("aggiungi almeno un volontario:");
            elencoVolontari.visualizza();
            String volonario = in.next();
            elencoVolontariVisita.aggiungi(elencoVolontari.getElementByKey(volonario));
            System.out.println("vuoi aggiungere un altro volontario??(y per continuare)");
            String risposta = in.next();
            if(!risposta.equalsIgnoreCase("y"))
                continua = false;
        }while(continua);
        TipoVisita tv = new TipoVisita(titolo, descrizione,
                                        puntoIncontro, periodoAnno, 
                                        giorniDisponibili, oraInizio, 
                                        durata, bigliettoNecessario,
                                        minPartecipanti, maxPartecipanti, 
                                        elencoVolontariVisita);
        elencoTipiVisita.aggiungi(tv);

    }

    private static void aggiungiVolontario(Elenco<Utente> elencoUtenti, Scanner in) {
        System.out.println("Inserisci username:");
        String username = in.next();
        System.out.println("Inserisci password:");
        String password = in.next();
        Utente v = new Volontario(username, password);
        elencoUtenti.aggiungi(v);
    }
}