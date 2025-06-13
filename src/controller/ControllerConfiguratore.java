package controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import model.*;
import view.CLI;


public class ControllerConfiguratore {

    private Configuratore configuratore;
    private boolean chiudiApp = true;

    
    private static final String AZIONI_CONFIGURATORE = 
        "\n----------------------------------------------------------------------------------------------"
        + "\nBenvenuto Configuratore! Scegli una delle seguenti alternative: \n\n" +
        "1. Aggiungere un nuovo Configuratore\n" +
        "2. Indicare le date del mese i+3 precluse ad ogni visita\n" +
        "3. Modificare il numero massimo di iscritti per ogni fruitore\n" +
        "4. Visualizzare l'elenco dei volontari, con relativi tipi di visita per cui ha dato disponibilità\n" +
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

    public ControllerConfiguratore(Configuratore conf) {
        this.configuratore = conf;
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
                indicaDatePrecluse();
                break;
            case 3: 
                cambiaNumeroMassimoIscritti();
                break;
            case 4:
                visualizzaVolontari();
                break;
            case 5:
                CLI.stampaMessaggio(DatiCondivisi.getElencoLuoghi().visualizza());
                break;
            case 6:
                visualizzaVisiteLuogo();
                break;
            case 7:
                visualizzaStatoVisite();
                //VERSIONE 1 : aggiungi la visualizzare l'archivio delle visite completate
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

    public void visualizzaStatoVisite() {
        CLI.stampaMessaggio("Ecco le visite disponibili:");
        for(Visita v : DatiCondivisi.getVisite().getElenco().values()){
            CLI.stampaMessaggio(v.toString() + " --> " + v.getStato().toString().toUpperCase());
        }
    }

    // private void creaVisite(){
    //     //GestoreVisite gestoreVisite = GestoreVisite.getInstance();
    //     LocalDate[] intervallo = GestioneTempo.getInstance().intervalloDate(1);
    //     // GestoreVisite.creaVisiteMese(
    //     //     intervallo[0],
    //     //     intervallo[1]
    //     // );
    // }

    private void visualizzaVolontari() {
        CLI.stampaMessaggio("Ecco i volontari disponibili:");
        for(Volontario v : DatiCondivisi.getElencoUtenti().getClassiUtente(Volontario.class).getElenco().values()){
            CLI.stampaMessaggio(v.visualizzaVolo());
        }
    }
    //fare doppio array e scrittura xml
    private void indicaDatePrecluse(){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate data = GestioneTempo.getInstance().getDataCorrente();;
        CLI.stampaMessaggio(data.format(formatter));
        LocalDate[] intervallo = GestioneTempo.getInstance().intervalloDate(3);
        CLI.stampaMessaggio("intervallo da " + intervallo[0].format(formatter) + " a " + intervallo[1].format(formatter));

        CLI.stampaMessaggio("scegli le date da precludere(solo il numero del giorno per il prossimo mese)");
        String s = "null";
        while(!s.equals("x")){
            s = CLI.sceltaString("scegli un giorno: (x per uscire)");
            if(!s.equals("x")){
                int giorno = Integer.parseInt(s);
                LocalDate dataGiorno = GestioneTempo.contieneGiorno(intervallo[0],intervallo[1],giorno);
                if(dataGiorno != null){
                    configuratore.aggiungiDatePrecluse(dataGiorno);
                }else{
                    CLI.stampaMessaggio("giorno non valido");
                }
            }
        }

        // creaVisite();
        // configuratore.aggiungiDatePrecluse(giorniPreclusi);
    }

    private void aggiungiConfiguratore(){
        String[] datiConfiguratore = CLI.login();
        configuratore.creaConfiguratore(datiConfiguratore);
    }

    private void cambiaNumeroMassimoIscritti(){
        int numeroMassimoIscrittiFruitore = CLI.sceltaInt("nuovo numero massimo di iscritti per fruitore -> ");
        configuratore.setNumeroMassimoIscrittiFruitore(numeroMassimoIscrittiFruitore);
    }

    private void visualizzaVisiteLuogo() {
        CLI.stampaMessaggio("Ecco i luoghi disponibili:");
        CLI.stampaMessaggio(DatiCondivisi.getElencoLuoghi().visualizza());
    
        String scelta = CLI.sceltaString("Scegli il luogo -> ");
    
        CLI.stampaMessaggio(DatiCondivisi.getElencoLuoghi().getElementByKey(scelta).toStringLuogo());
    }

    public void primaConfigurazione() {
        String[] datiCorpoDati = CLI.creaCorpoDati();
        DatiCondivisi.setAmbitoTerritoriale(datiCorpoDati[0]);
        DatiCondivisi.setNumeroMassimoIscrittiFruitore(Integer.parseInt(datiCorpoDati[1]));
        creaLuogo();
    }

    private void creaLuogo(){
        if(DatiCondivisi.getElencoTipiVisita().getElenco().size() == 0){
            CLI.stampaMessaggio("prima deve esistere almeno un tipo di visita");
            creaTipoVisita();
        }

        String[] datiLuogo = CLI.messaggioCreazione(CREAZIONE_LUOGO);
        Luogo l = configuratore.creaLuogo(datiLuogo);
        String s = null;
        do{
            CLI.stampaMessaggio("scegli un tipo di visita: (x per uscire)");
            s = CLI.sceltaString(DatiCondivisi.getElencoTipiVisita().visualizza());
            if(!s.equals("x")){
                l.getElencoVisite().aggiungi(DatiCondivisi.getElencoTipiVisita().getElementByKey(s));
            }
        }while(!s.equals("x"));
    }

    private void creaTipoVisita(){
        if(DatiCondivisi.getElencoUtenti().getClassiUtente(Volontario.class).getElenco().size() == 0){
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
            s = CLI.sceltaString(DatiCondivisi.getElencoUtenti().getClassiUtente(Volontario.class).visualizza());
            if(!s.equals("x")){
                elencoV.aggiungi((Volontario)DatiCondivisi.getElencoUtenti().getElementByKey(s));
            }
        }while(!s.equals("x"));

        configuratore.creaTipoVisita(titolo, descrizione,
                                puntoIncontro, periodoAnno, 
                                giorniDisponibili, oraInizio, 
                                durata, bigliettoNecessario,
                                minPartecipanti, maxPartecipanti, 
                                elencoV);
                                
        //non deve stare qui                      
        for(Volontario v : elencoV.getElenco().values()){
            v.aggiungiVisitaVolontario(DatiCondivisi.getElencoTipiVisita().getElementByKey(titolo));
        }
    }

    private void creaVolontario(){

        String[] dati = CLI.creaUtente("volontario");
        configuratore.creaVolontario(dati);
    }
}
