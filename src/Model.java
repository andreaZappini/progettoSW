import java.io.File;
import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class Model{

    private static final String AZIONI_CONFIGURATORE = "Benvenuto Configuratore\n" +
                                                        "1. Aggiungi Configuratore\n" +
                                                        "2. Indicare le date per le attivita del mese i+3\n" +
                                                        "3. Modificare numero massimo di iscritti per ogni fruitore\n" +
                                                        "4. Visualizza Elenco Volontari\n" +
                                                        "5. Visualizza Elenco Luoghi\n" +
                                                        "6. Visualizza Elenco dei tipi di Visita Associati a un Luogo\n" +
                                                        "7. Logout\n" +
                                                        "8. Chiudi applicazione\n" + 
                                                        "Scelta\n";
                                                        
    private static final String AZIONI_VOLONTARIO = "";
    private static final String AZIONI_UTENTE = "";
    private static CorpoDati corpoDati;
    private static Elenco<Utente> elencoUtenti = new Elenco<>();
    private static Elenco<Luogo> elencoLuoghi = new Elenco<>();
    private static Elenco<TipoVisita> elencoTipiVisita = new Elenco<>();
    private static boolean working;

    public static void start() throws Exception{
        
        String[] datiRipristino = new String[4];
        datiRipristino = XMLUtilities.leggiXML(
            new File("fileXML/datiExtra.xml"),
            "datiDiConfigurazione"
        );

        boolean primaConfigurazione = Boolean.parseBoolean(datiRipristino[0].trim());
        if(!primaConfigurazione){
            String ambitoTerritoriale = datiRipristino[1];
            int numeroMaxIscritti = Integer.parseInt(datiRipristino[2]);
            corpoDati = new CorpoDati(ambitoTerritoriale, numeroMaxIscritti);
            recuperaDatiXML();
            corpoDati.ripristinaElenco(elencoLuoghi);
        }else{
            datiPrimoConfiguratore();
            boolean primoAccesso = true;
            Utente x;
            while(primoAccesso){
                String[] datiUtente = CLI.login();
                String username = datiUtente[0];
                String password = datiUtente[1];
              
                if(!elencoUtenti.contiene(username)){
                    CLI.stampaMessaggio("utente non trovato");
                }else{
                    x = elencoUtenti.getElementByKey(username);
                    if(x.controllaPassword(password)){
                        CLI.stampaMessaggio("accesso consentito");
                        x.getPrimoAccesso();
                        CLI.stampaMessaggio("PRIMO ACCESSO -> cambia password: ");
                        String nuovaPassword = CLI.cambiaPassword();
                        x.setPassword(nuovaPassword);
                        x.setPrimoAccesso();
                        primoAccesso = false;
                    }else{
                        CLI.stampaMessaggio("password errata");
                    }
                }
            }
            String[] datiCorpoDati = CLI.creaCorpoDati();
            corpoDati = new CorpoDati(datiCorpoDati[0], Integer.parseInt(datiCorpoDati[1]));
        }

        working = true;
        while(working){
            Utente x;
            while(true){
                String[] datiUtente = CLI.login();
                String username = datiUtente[0];
                String password = datiUtente[1];


                if(!elencoUtenti.contiene(username)){
                    CLI.stampaMessaggio("utente non trovato");
                }else{
                    x = elencoUtenti.getElementByKey(username);
                    if(x.controllaPassword(password)){
                        CLI.stampaMessaggio("accesso consentito");
                        if(x.getPrimoAccesso()){
                            CLI.stampaMessaggio("PRIMO ACCESSO -> cambia password: ");
                            String nuovaPassword = CLI.cambiaPassword();
                            x.setPassword(nuovaPassword);
                            x.setPrimoAccesso();
                        }
                        break;
                    }else{
                        CLI.stampaMessaggio("password errata");
                    }
                }
            }
            int scelta;
            switch(x.getClass().getSimpleName()){
                case "Configuratore":
                    boolean continua = true;
                    while(continua){
                        scelta = CLI.sceltaInt(AZIONI_CONFIGURATORE);
                        continua = azioniConfiguratore(scelta);
                    }

                    break;
                case "Volontario":
                    scelta  = CLI.sceltaInt(AZIONI_VOLONTARIO);
                    break;
                case "Fruitore":
                    scelta = CLI.sceltaInt(AZIONI_UTENTE);
                    break;
                default:
                    break;
            }
        }
        CLI.chiudiScanner();
        salvataggioDati();
    }

    private static void datiPrimoConfiguratore() throws Exception{
        Elenco<Utente> elencop = XMLUtilities.leggiXML(
                new File("fileXML/configuratore1.xml"), 
                "Utente", 
                elemento -> {
                    String username = elemento.getElementsByTagName("username").item(0).getTextContent();
                    String password = elemento.getElementsByTagName("password").item(0).getTextContent();
                    //boolean primoAccesso = Boolean.parseBoolean(
                        //elemento.getElementsByTagName("primoAccesso").item(0).getTextContent());
                    return new Configuratore(username, password);
                });
                elencoUtenti.aggiungi(elencop);
    }

    private static void recuperaDatiXML() throws Exception{

        elencoUtenti = XMLUtilities.leggiXML(
            new File("fileXML/utenti.xml"), 
            "Utenti", 
            elemento -> creaUtente(elemento)
        );

        elencoTipiVisita = XMLUtilities.leggiXML(
            new File("fileXML/tipiVisita.xml"), 
            "TipiVisita", 
            elemento -> creaTipoVisita(elemento, elencoUtenti)
        );

        elencoLuoghi = XMLUtilities.leggiXML(
            new File ("fileXML/luoghi.xml"),
            "Luoghi",
            elemento -> creaLuogo(elemento, elencoTipiVisita)
        );
    }

    private static Utente creaUtente(Element elemento){
        String nomeNodo = elemento.getNodeName(); // Configuratore o Volontario
        String username = elemento.getElementsByTagName("username").item(0).getTextContent();
        String password = elemento.getElementsByTagName("password").item(0).getTextContent();
        boolean primoAccesso = Boolean.parseBoolean(elemento.getElementsByTagName("primoAccesso").item(0).getTextContent());
    
        if (nomeNodo.equalsIgnoreCase("Configuratore")) {
            return new Configuratore(username, password, primoAccesso);
        } else if (nomeNodo.equalsIgnoreCase("Volontario")) {
            return new Volontario(username, password, primoAccesso);
        } else {
            throw new IllegalArgumentException("Tipo di utente sconosciuto: " + nomeNodo);
        }
    }

    private static TipoVisita creaTipoVisita(Element elemento, Elenco<Utente> elencoUtenti){

        String titolo = elemento.getElementsByTagName("titolo").item(0).getTextContent(); 
        String descrizione = elemento.getElementsByTagName("descrizione").item(0).getTextContent();
        String puntoIncontro = elemento.getElementsByTagName("puntoIncontro").item(0).getTextContent();
        String periodoAnno = elemento.getElementsByTagName("periodoAnno").item(0).getTextContent();
        String bigliettoNecessario = elemento.getElementsByTagName("bigliettoNecessario").item(0).getTextContent();
        ArrayList<Giorni> giorniDisponibili = new ArrayList<>();
        NodeList giorniNodes = elemento.getElementsByTagName("giorno");
        for (int i = 0; i < giorniNodes.getLength(); i++) {
            String giornoStr = giorniNodes.item(i).getTextContent();
            try {
                Giorni giorno = Giorni.fromString(giornoStr); // Metodo fromString() nell'enum
                giorniDisponibili.add(giorno);
            } catch (IllegalArgumentException e) {
               CLI.stampaMessaggio("Errore: Giorno non valido trovato nel XML - " + giornoStr);
            }
        }

        // Conversione in numeri
        double oraInizio = Double.parseDouble(elemento.getElementsByTagName("oraInizio").item(0).getTextContent());
        int durata = Integer.parseInt(elemento.getElementsByTagName("durata").item(0).getTextContent());
        int minPartecipanti = Integer.parseInt(elemento.getElementsByTagName("minPartecipanti").item(0).getTextContent());
        int maxPartecipanti = Integer.parseInt(elemento.getElementsByTagName("maxPartecipanti").item(0).getTextContent());

        // Recupero dell'elenco di volontari (supponendo sia una lista di username da convertire)
        Elenco<Volontario> elencoVolontari = new Elenco<>();
        NodeList volontariNodes = elemento.getElementsByTagName("volontario");
        for (int i = 0; i < volontariNodes.getLength(); i++) {
            String nomeVolontario = volontariNodes.item(i).getTextContent();
            Utente u = elencoUtenti.getElementByKey(nomeVolontario);
            Volontario volontario = null;
            if(u instanceof Volontario)
                volontario = (Volontario) u;
                //funziona
                //System.out.println(volontario);
            elencoVolontari.aggiungi(volontario);
        }
        return new TipoVisita(titolo, descrizione,
                                puntoIncontro, periodoAnno, 
                                giorniDisponibili, oraInizio, 
                                durata, bigliettoNecessario,
                                minPartecipanti, maxPartecipanti, 
                                elencoVolontari);
    }

    private static Luogo creaLuogo(Element elemento, Elenco<TipoVisita> visite){
        String codiceLuogo = elemento.getElementsByTagName("codiceLuogo").item(0).getTextContent();
        String descrizione = elemento.getElementsByTagName("descrizione").item(0).getTextContent();
        String collocazioneGeografica = elemento.getElementsByTagName("collocazioneGeografica").item(0).getTextContent();

        Luogo l = new Luogo(codiceLuogo, descrizione, collocazioneGeografica);

        Elenco<TipoVisita> elencoTipoVisita = new Elenco<>();

        NodeList visiteNodes = elemento.getElementsByTagName("visite");
        for (int i = 0; i < visiteNodes.getLength(); i++) {
            String nomeVisita = visiteNodes.item(i).getTextContent().trim();
            //funziona
            //System.out.println(nomeVisita);
            TipoVisita t = visite.getElementByKey(nomeVisita);
            //stampa null
            elencoTipoVisita.aggiungi(t);
            l.aggiungiAElencoVisite(t);
        }

        return l;
    }

    private static boolean azioniConfiguratore(int scelta){
        boolean continua = true;
        switch (scelta) {
            case 1:
                aggiungiConfiguratore();
                break;
            case 2:
                //indica date
                break;
            case 3: 
                cambiaNumeroMassimoIscritti();
                break;
            case 4:
                CLI.visualizzaElenco(elencoUtenti.getClassiUtente(Volontario.class).visualizza());
                break;
            case 5:
                CLI.visualizzaElenco(corpoDati.getElencoLuoghi().visualizza());
                break;
            case 6:
                visualizzaVisiteLuogo();
                break;
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
        return continua;
    }

    private static void aggiungiConfiguratore(){
        String[] datiConfiguratore = CLI.login();
        String username = datiConfiguratore[0];
        String password = datiConfiguratore[1];
        Utente x = new Configuratore(username, password);
        elencoUtenti.aggiungi(x);
    }

    private static void cambiaNumeroMassimoIscritti(){
        int numeroMassimoIscrittiFruitore = CLI.sceltaInt("nuovo numero massimo di iscritti per fruitore -> ");
        corpoDati.setNumeroMassimoIscrittiFruitore(numeroMassimoIscrittiFruitore);
    }

    private static void visualizzaVisiteLuogo(){
        CLI.stampaMessaggio("ecco i luoghi dei quali puoi vedere le visite: ");
        CLI.visualizzaElenco(corpoDati.getElencoLuoghi().visualizza());
        String scetla = CLI.sceltaString("scegli il luogo -> ");
        CLI.visualizzaElenco(elencoLuoghi.getElementByKey(scetla).getElencoVisite().visualizza());
    }

    public static void salvataggioDati() throws Exception{
        XMLUtilities.scriviXML(
            new File("fileXML/utentibu.xml"), elencoUtenti, "Utente");

        XMLUtilities.scriviXML(
            new File("fileXML/tipiVisitabu.xml"), elencoTipiVisita, "TipiVisita");

        XMLUtilities.scriviXML(
            new File("fileXML/luoghibu.xml"), corpoDati.getElencoLuoghi(), "Luoghi");
    }
}