import java.util.ArrayList;

public class ControllerConfiguratore {
    private Configuratore configuratore;
    private CorpoDati corpoDati;
    private Elenco<Utente> elencoUtenti;
    private Elenco<TipoVisita> elencoTipiVisita;
    private boolean chiudiApp = true;

    
    private static final String AZIONI_CONFIGURATORE = 
        "\n----------------------------------------------------------------------------------------------"
        + "\nBenvenuto Configuratore! Scegli una delle seguenti alternative: \n\n" +
        "1. Aggiungere un nuovo Configuratore\n" +
        "2. Indicare le date del mese i+3 precluse ad ogni visita\n" +
        "3. Modificare il numero massimo di iscritti per ogni fruitore\n" +
        "4. Visualizzare l'elenco dei volontari\n" +
        "5. Visualizzare l'elenco dei luoghi visitabili\n" +
        "6. Visualizzare l'elenco dei tipi di visita associati ad un determinato luogo\n" +
        "7. Visualizzare lo stato delle visite\n"+
        "8. Logout\n" +
        "9. Chiudere l'applicazione\n" + 
        "------------------------------------------------------------------------------------------------\n";

    private static final String AZIONI_CONF = 
        "\n----------------------------------------------------------------------------------------------"
        + "\nAZIONI SUL CORPO DATI\n\n" +
        "1. Aggiungere un nuovo LUOGO\n" +
        "2. Aggiungere un nuovo TIPO DI VISITA\n" +
        "3. Aggiungere un nuovo VOLONTARIO\n" + "4. Exit\n"+
        "------------------------------------------------------------------------------------------------\n";
    
        private static final String[] CREAZIONE_LUOGO = {
        "- Espressione sintetica identificativa: ",
        "- Descrizione: ",
        "- Collocazione geografica: "
    };

    private static final String[] CREAZIONE_VISITA = {
        "- Titolo: ",
        "- Descrizione: ",
        "- Punto d'incontro: ",
        "- Periodo dell'anno: ",
        "- Ora d'inizio: ",
        "- Durata: ",
        "- Biglietto necessario: ",
        "- Numero minimo di partecipanti: ",
        "- Numero massimo di partecipanti: "
    };
    public ControllerConfiguratore(Configuratore conf, CorpoDati dati, Elenco<Utente> utenti, Elenco<TipoVisita> visite) {
        this.configuratore = conf;
        this.corpoDati = dati;
        this.elencoUtenti = utenti;
        this.elencoTipiVisita = visite;
    }

    public boolean start() {
        boolean continua = true;
        while (continua) {
            int scelta = CLI.sceltaInt(AZIONI_CONFIGURATORE);
            continua = azioniConfiguratore(scelta, configuratore);        
        }
        return chiudiApp;
    }

    private boolean azioniConfiguratore(int scelta, Configuratore config){
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
                CLI.stampaMessaggio(elencoUtenti.getClassiUtente(Volontario.class).visualizza());
                break;
            case 5:
                CLI.stampaMessaggio(corpoDati.getElencoLuoghi().visualizza());
                break;
            case 6:
                visualizzaVisiteLuogo();
                break;
            case 7:
                //visualizzaStatoVisite();
                break;
            case 8:
                continua = false;
                break;
            case 9:
                chiudiApp = false;
                continua = false;
                break;
            default:
                break;
        }
        return continua;
    }

    private void aggiungiConfiguratore(){
        String[] datiConfiguratore = CLI.login();
        String username = datiConfiguratore[0];
        String password = datiConfiguratore[1];
        Utente x = new Configuratore(username, password);
        elencoUtenti.aggiungi(x);
    }

    private void cambiaNumeroMassimoIscritti(){
        int numeroMassimoIscrittiFruitore = CLI.sceltaInt("nuovo numero massimo di iscritti per fruitore -> ");
        configuratore.setNumeroMassimoIscrittiFruitore(numeroMassimoIscrittiFruitore, corpoDati);
    }

    private void visualizzaVisiteLuogo() {
        CLI.stampaMessaggio("Ecco i luoghi disponibili:");
        CLI.stampaMessaggio(corpoDati.getElencoLuoghi().visualizza());
    
        String scelta = CLI.sceltaString("Scegli il luogo -> ");
        String elencoVisite = configuratore.visualizzaVisiteDiLuogo(scelta, corpoDati);
    
        CLI.stampaMessaggio(elencoVisite);
    }

    public CorpoDati primaConfigurazione() {
        String[] datiCorpoDati = CLI.creaCorpoDati();
        corpoDati = new CorpoDati(datiCorpoDati[0], Integer.parseInt(datiCorpoDati[1]));
        creaLuogo();
        return corpoDati;
    }

    private void creaLuogo(){
        if(elencoTipiVisita.getElenco().size() == 0){
            CLI.stampaMessaggio("prima deve esistere almeno un tipo di visita");
            creaTipoVisita();
        }

        String[] datiLuogo = CLI.messaggioCreazione(CREAZIONE_LUOGO);
        Luogo l = configuratore.creaLuogo(datiLuogo, corpoDati);
        String s = null;
        do{
            CLI.stampaMessaggio("scegli un tipo di visita: (x per uscire)");
            s = CLI.sceltaString(elencoTipiVisita.visualizza());
            if(!s.equals("x")){
                l.getElencoVisite().aggiungi(elencoTipiVisita.getElementByKey(s));
            }
        }while(!s.equals("x"));
    }

    private void creaTipoVisita(){
        if(elencoUtenti.getClassiUtente(Volontario.class).getElenco().size() == 0){
            CLI.stampaMessaggio("prima deve esserci almeno un volontario");
            creaVolontario();
        }

        String[] datiLuogo = CLI.messaggioCreazione(CREAZIONE_VISITA);

        String titolo = datiLuogo[0];
        String descrizione = datiLuogo[1];
        String puntoIncontro = datiLuogo[2];
        String periodoAnno = datiLuogo[3];
        double oraInizio = Double.parseDouble(datiLuogo[4]);
        int durata = Integer.parseInt(datiLuogo[5]);
        String bigliettoNecessario = datiLuogo[6];
        int  minPartecipanti = Integer.parseInt(datiLuogo[7]);
        int  maxPartecipanti = Integer.parseInt(datiLuogo[8]);

        ArrayList<Giorni> giorniDisponibili = new ArrayList<>();
        String s = null;
        do{
            s = CLI.sceltaString("insersci un giorno: (x per uscire)");
            if(!s.equals("x")){
                giorniDisponibili.add(Giorni.fromString(s.toLowerCase()));
            }
        }while(!s.equals("x"));

        Elenco<Volontario> elencoV = new Elenco<>();

        s = null;
        do{
            CLI.stampaMessaggio("scegli volontario: (x per uscire)");
            s = CLI.sceltaString(elencoUtenti.getClassiUtente(Volontario.class).visualizza());
            if(!s.equals("x")){
                elencoV.aggiungi((Volontario)elencoUtenti.getElementByKey(s));
            }
        }while(!s.equals("x"));

        configuratore.creaTipoVisita(titolo, descrizione,
                                puntoIncontro, periodoAnno, 
                                giorniDisponibili, oraInizio, 
                                durata, bigliettoNecessario,
                                minPartecipanti, maxPartecipanti, 
                                elencoV, elencoTipiVisita);
    }

    private void creaVolontario(){

        String[] dati = CLI.login();
        configuratore.creaVolontario(dati, elencoUtenti);
    }
}
