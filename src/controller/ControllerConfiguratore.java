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
        "8. passa a controlli su elenchi(creazione/eliminazione soggetti)\n" +
        "9. Chiudi raccolta disponibilità per il mese i+1\n" +
        "10. Creare il piano visite per il mese i+1\n" +
        "11. Aprire la raccolta disponibilità per il mese i+2\n" +
        "12. Visualizzare l'archivio delle visite\n" +
        "13. logout\n" +
        "14. Chiudere l'applicazione\n" + 
        "--> ";

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
        "- Durata(minuti): ",
        "- Biglietto necessario(S/N): ",
        "- Numero minimo di partecipanti: ",
        "- Numero massimo di partecipanti: "
    };

    public ControllerConfiguratore(Configuratore conf) {
        this.configuratore = conf;
    }

    public boolean start() {
        try {
            boolean continua = true;
            while (continua) {
                int scelta = CLI.sceltaInt(AZIONI_CONFIGURATORE);
                continua = azioniConfiguratore(scelta);
            }
            return chiudiApp;
        } catch (Exception e) {
           CLI.stampaMessaggio(e.toString());
            return false;
        }
    }

    private boolean azioniConfiguratore(int scelta){

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
                break;
            case 8:
                if(DatiCondivisi.getRaccoltaDisponibilitaMese1() == DatiCondivisi.StatiRaccoltaDisponibilita.CHIUSA){
                    ControllerExtra controllerExtra = new ControllerExtra(configuratore);
                    controllerExtra.start();
                } else {
                    CLI.stampaMessaggio("impossibile accedere a questa funzionalita" +
                    "(non è possibile apportare modifiche con la raccolta disponibilita aperta)");
                }
                break;
            case 9:
                chiudiDiponibilitaMese1();
                break;
            case 10:
                creaPianoVisite();
                break;
            case 11:
                apriDipsonibilitaMese2();
                break;
            case 12:
                visualizzaArchivio();
                break;
            case 13:
                continua = false;
                break;
            case 14:
                chiudiApp = false;
                continua = false;
                break;
            default:
                break;
        }
        return continua;
    }

    private void visualizzaArchivio(){
        CLI.stampaMessaggio("Ecco l'archivio delle visite:");
        CLI.stampaMessaggio(DatiCondivisi.getArchivio().visualizza());
    }

    private void chiudiDiponibilitaMese1(){
        try {
            if(DatiCondivisi.getRaccoltaDisponibilitaMese1() == DatiCondivisi.StatiRaccoltaDisponibilita.APERTA){
                
                CLI.stampaMessaggio("chiusura disponibilita per il mese successivo al prossimo avvenuta con successo");
                DatiCondivisi.setIntervalloPianoVisite(GestoreVisite.getInstance().intervallo());
                DatiCondivisi.chiudiRaccoltaDisponibilitaMese1();
            } else {
                CLI.stampaMessaggio("disponibilita gia chiusa");
            }
        } catch (Exception e) {
           CLI.stampaMessaggio(e.toString());
            return;
        }
    }

    private void creaPianoVisite(){
        try{
            if(DatiCondivisi.getRaccoltaDisponibilitaMese1() == DatiCondivisi.StatiRaccoltaDisponibilita.CHIUSA){
                CLI.stampaMessaggio("creazione piano visite avvenuta con successo");
                LocalDate[] intervallo = DatiCondivisi.getIntervalloPianoVisite();
                GestoreVisite.getInstance().creaPianoViste(intervallo[0], intervallo[1]);
            } else {
                CLI.stampaMessaggio("ancora possibile la raccolta delle disponibilita da perte dei volontari");
            }
        }catch(Exception e){
            CLI.stampaMessaggio(e.getMessage());
            return;
        }
    }

    public void visualizzaStatoVisite() {
        try {
            System.out.println(DatiCondivisi.getVisite().numeroElementi());
            CLI.stampaMessaggio("Ecco le visite disponibili:");
            for (ListaVisite lv : DatiCondivisi.getVisite().getElenco().values()) {
                for (Visita v : lv.getVisite().getElenco().values()) {
                    System.out.println("    Visita -> " + v.getDataVisita() + ", " + v.getTipo() + ", Stato: " + v.getStato());
                }
            }
        } catch (EccezioneOggetto e) {
           CLI.stampaMessaggio(e.toString());
            return;
        }
    }

    private void apriDipsonibilitaMese2(){
        try {
            if(DatiCondivisi.getRaccoltaDisponibilitaMese2() == DatiCondivisi.StatiRaccoltaDisponibilita.CHIUSA){
                CLI.stampaMessaggio("apertura raccolta disponibilita per il mese successivo al prossimo avvenuta con successo");
                DatiCondivisi.apriRaccoltaDisponibilitaMese2();
            } else {
                CLI.stampaMessaggio("disponibilita gia aperta");
            }
            System.out.println(DatiCondivisi.getRaccoltaDisponibilitaMese2());
        } catch (Exception e) {
           CLI.stampaMessaggio(e.toString());
            return;
        }
    }

    private void visualizzaVolontari() {
        try {
            CLI.stampaMessaggio("Ecco i volontari disponibili:");
            for(Volontario v : DatiCondivisi.getElencoUtenti().getClassiUtente(Volontario.class).getElenco().values()){
                CLI.stampaMessaggio(v.visualizzaVolo());
            }
        } catch (Exception e) {
           CLI.stampaMessaggio(e.toString());
            return;
        }
    }

    private void indicaDatePrecluse(){
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate data = GestioneTempo.getInstance().getDataCorrente();
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
                    } else {
                        CLI.stampaMessaggio("giorno non valido");
                    }
                }
            }
        } catch (Exception e) {
           CLI.stampaMessaggio(e.toString());
            return;
        }
    }

    private void aggiungiConfiguratore(){
        boolean ok = false;
        while(!ok){
            try {
                String[] dati = CLI.creaUtente("configuratore");
                configuratore.creaConfiguratore(dati);
                ok = true;
            } catch (Exception e) {
                CLI.stampaMessaggio(e.getMessage());
                ok = false;
            }
        }
    }

    private void cambiaNumeroMassimoIscritti(){
        try {
            int numeroMassimoIscrittiFruitore = CLI.sceltaInt("nuovo numero massimo di iscritti per fruitore -> ");
            configuratore.setNumeroMassimoIscrittiFruitore(numeroMassimoIscrittiFruitore);
        } catch (Exception e) {
            CLI.stampaMessaggio(e.toString());
            return;
        }
    }

    private void visualizzaVisiteLuogo() {
        try {
            CLI.stampaMessaggio("Ecco i luoghi disponibili:");
            CLI.stampaMessaggio(DatiCondivisi.getElencoLuoghi().visualizza());
            String scelta = CLI.sceltaString("Scegli il luogo -> ");
            CLI.stampaMessaggio(DatiCondivisi.getElencoLuoghi().getElementByKey(scelta).toStringLuogo());
        } catch (Exception e) {
           CLI.stampaMessaggio(e.toString());
            return;
        }
    }

    public void primaConfigurazione() {
        try {
            String[] datiCorpoDati = CLI.creaCorpoDati();
            do{
                if(datiCorpoDati[0] == null || datiCorpoDati[0].isEmpty()){
                    CLI.stampaMessaggio("devi inserire un valore valido per l'ambito territoriale");
                    datiCorpoDati[0] = CLI.sceltaString("inserisci l'ambito territoriale: ");
                }
                else
                    DatiCondivisi.setAmbitoTerritoriale(datiCorpoDati[0]);
            }while(datiCorpoDati[0] == null || datiCorpoDati[0].isEmpty());
            boolean ok = false;
            do{
                try{
                    DatiCondivisi.setNumeroMassimoIscrittiFruitore(Integer.parseInt(datiCorpoDati[1]));
                    ok = true;
                }catch(NumberFormatException e){
                    CLI.stampaMessaggio("devi inserire un numero intero per il numero massimo di fruitori");
                    datiCorpoDati[1] = CLI.sceltaString("inserisci il numero massimo di fruitori: ");
                    ok = false;
                }
            }while(!ok);
        
            DatiCondivisi.setNumeroMassimoIscrittiFruitore(Integer.parseInt(datiCorpoDati[1]));
            creaLuogo();
        } catch (Exception e) {
            CLI.stampaMessaggio(e.toString());
            return;
        }
    }

    private void creaLuogo(){
        try {
            if(DatiCondivisi.getElencoTipiVisita().getElenco().size() == 0){
                CLI.stampaMessaggio("prima deve esistere almeno un tipo di visita");
                creaTipoVisita();
            }
            CLI.stampaMessaggio("creazione di un nuovo luogo visitabile\n\n");
            String[] datiLuogo = CLI.messaggioCreazione(CREAZIONE_LUOGO);
            Luogo l = configuratore.creaLuogo(datiLuogo);
            String s = null;
            do {
                CLI.stampaMessaggio("scegli un tipo di visita: (x per uscire)");
                s = CLI.sceltaString(DatiCondivisi.getElencoTipiVisita().visualizza());
                if(!s.equals("x")){
                    try{
                        l.getElencoVisite().aggiungi(DatiCondivisi.getElencoTipiVisita().getElementByKey(s));
                        DatiCondivisi.getElencoTipiVisita().getElementByKey(s).aggiungiLuogo(l);
                    }catch(IllegalArgumentException e){
                        CLI.stampaMessaggio(e.getMessage());
                    }
                }
                if(l.getElencoVisite().numeroElementi() == 0){
                    CLI.stampaMessaggio("deve esserci almeno un tipo di visita");
                    s = "";
                }
            } while(!s.equals("x") || l.getElencoVisite().numeroElementi() == 0);
        } catch (Exception e) {
           CLI.stampaMessaggio(e.toString());
            return;
        }
    }

    private void creaTipoVisita(){
        try {
            if(DatiCondivisi.getElencoUtenti().getClassiUtente(Volontario.class).getElenco().size() == 0){
                CLI.stampaMessaggio("prima deve esserci almeno un volontario");
                creaVolontario();
            }
            CLI.stampaMessaggio("creazione di un nuovo tipo di visita\n\n");
            String[] datiLuogo = CLI.messaggioCreazione(CREAZIONE_VISITA);
            ArrayList<Giorni> giorniDisponibili = new ArrayList<>();
            String s = null;
            do {
                s = CLI.sceltaString("inserisci un giorno: (x per uscire)");
                if(!s.equals("x")){
                    try{
                        giorniDisponibili.add(Giorni.fromString(s.toLowerCase()));
                    } catch (IllegalArgumentException e) {
                        CLI.stampaMessaggio(e.getMessage());
                    }
                }
                if(giorniDisponibili.isEmpty()){
                    CLI.stampaMessaggio("deve esserci almeno un giorno");
                    s = "";
                }
            } while(!s.equals("x") || giorniDisponibili.isEmpty());
            Elenco<Volontario> elencoV = new Elenco<>();
            s = null;
            do {
                CLI.stampaMessaggio("scegli volontario: (x per uscire)");
                s = CLI.sceltaString(DatiCondivisi.getElencoUtenti().getClassiUtente(Volontario.class).visualizza());
                if(!s.equals("x")){
                    try{
                        elencoV.aggiungi((Volontario)DatiCondivisi.getElencoUtenti().getElementByKey(s));
                    }catch (IllegalArgumentException e) {
                        CLI.stampaMessaggio(e.getMessage());
                    }
                }
                if(elencoV.numeroElementi() == 0){
                    CLI.stampaMessaggio("deve esserci almeno un volontario");
                    s = "";
                }
            } while(!s.equals("x") || elencoV.numeroElementi() == 0);

            for(int i = 0; i < datiLuogo.length; i++){
                if(datiLuogo[i] == null || datiLuogo[i].isEmpty()){
                    do{
                        datiLuogo[i] = CLI.sceltaString(CREAZIONE_VISITA[i]);
                    }while(datiLuogo[i] == null || datiLuogo[i].isEmpty());
                }
                if(i == 5 || i == 7 || i == 8){
                    boolean ok = false;
                    do{
                        try{
                            Integer.parseInt(datiLuogo[i]);
                            ok = true;
                        }catch(NumberFormatException e){
                            ok = false;
                            CLI.stampaMessaggio("devi inserire un numero intero per " + CREAZIONE_VISITA[i]);
                            datiLuogo[i] = CLI.sceltaString(CREAZIONE_VISITA[i]);
                        }
                    }while(!ok);
                }
                if(i == 4){
                    boolean ok = false;
                    do{
                        try{
                            Double.parseDouble(datiLuogo[i]);
                            ok = true;
                        }catch(NumberFormatException e){
                            ok = false;
                            CLI.stampaMessaggio("devi inserire un numero con virgola per " + CREAZIONE_VISITA[i]);
                            datiLuogo[i] = CLI.sceltaString(CREAZIONE_VISITA[i]);
                        }
                    }while(!ok);
                }
            }
            String titolo = datiLuogo[0];
            String descrizione = datiLuogo[1];
            String puntoIncontro = datiLuogo[2];
            String periodoAnno = datiLuogo[3];
            double oraInizio = Double.parseDouble(datiLuogo[4]);
            int durata = Integer.parseInt(datiLuogo[5]);
            String bigliettoNecessario = datiLuogo[6];
            int minPartecipanti = Integer.parseInt(datiLuogo[7]);
            int maxPartecipanti = Integer.parseInt(datiLuogo[8]);

            if(minPartecipanti < 0){
                minPartecipanti = 1;
                CLI.stampaMessaggio("numero minimo di partecipanti non può essere negativo, impostato a 1");
            }

            if(maxPartecipanti < minPartecipanti){
                maxPartecipanti = minPartecipanti + 1;
                CLI.stampaMessaggio("numero massimo di partecipanti non può essere minore del minimo, impostato a " + maxPartecipanti);
            }

            if(!bigliettoNecessario.equalsIgnoreCase("s") && !bigliettoNecessario.equalsIgnoreCase("n")){
                bigliettoNecessario = "N";
                CLI.stampaMessaggio("biglietto necessario deve essere S o N, impostato a N");
            }
           
            configuratore.creaTipoVisita(titolo, descrizione, puntoIncontro, periodoAnno, giorniDisponibili, oraInizio, durata, bigliettoNecessario, minPartecipanti, maxPartecipanti);
            TipoVisita v = DatiCondivisi.getElencoTipiVisita().getElementByKey(titolo);
            for(Volontario volo : elencoV.getElenco().values()){
                volo.aggiungiVisitaVolontario(v);
                v.aggiungiVolontario(volo);
            }
        } catch (Exception e) {
            CLI.stampaMessaggio(e.toString());
            return;
        }
    }

    private void creaVolontario(){
        boolean ok = false;
        while(!ok){
            try {
                String[] dati = CLI.creaUtente("volontario");
                configuratore.creaVolontario(dati);
                ok = true;
            } catch (Exception e) {
               CLI.stampaMessaggio(e.getMessage());
                ok = false;
            }
        }
    }
}
