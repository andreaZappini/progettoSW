package controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import model.*;
import view.CLI;
import view.StandardError;

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
        try {
            boolean continua = true;
            while (continua) {
                int scelta = CLI.sceltaInt(AZIONI_CONFIGURATORE);
                continua = azioniConfiguratore(scelta);
            }
            return chiudiApp;
        } catch (Exception e) {
            StandardError.stampaErrore(e.toString());
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
            // System.out.println(DatiCondivisi.getRaccoltaDisponibilitaMese1());
        } catch (Exception e) {
            StandardError.stampaErrore(e.toString());
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
            e.printStackTrace();
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
            StandardError.stampaErrore(e.toString());
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
            StandardError.stampaErrore(e.toString());
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
            StandardError.stampaErrore(e.toString());
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
            StandardError.stampaErrore(e.toString());
            return;
        }
    }

    private void aggiungiConfiguratore(){
        try {
            String[] datiConfiguratore = CLI.login();
            configuratore.creaConfiguratore(datiConfiguratore);
        } catch (Exception e) {
            StandardError.stampaErrore(e.toString());
            return;
        }
    }

    private void cambiaNumeroMassimoIscritti(){
        try {
            int numeroMassimoIscrittiFruitore = CLI.sceltaInt("nuovo numero massimo di iscritti per fruitore -> ");
            configuratore.setNumeroMassimoIscrittiFruitore(numeroMassimoIscrittiFruitore);
        } catch (Exception e) {
            StandardError.stampaErrore(e.toString());
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
            StandardError.stampaErrore(e.toString());
            return;
        }
    }

    public void primaConfigurazione() {
        try {
            String[] datiCorpoDati = CLI.creaCorpoDati();
            DatiCondivisi.setAmbitoTerritoriale(datiCorpoDati[0]);
            DatiCondivisi.setNumeroMassimoIscrittiFruitore(Integer.parseInt(datiCorpoDati[1]));
            creaLuogo();
        } catch (Exception e) {
            StandardError.stampaErrore(e.toString());
            return;
        }
    }

    private void creaLuogo(){
        try {
            if(DatiCondivisi.getElencoTipiVisita().getElenco().size() == 0){
                CLI.stampaMessaggio("prima deve esistere almeno un tipo di visita");
                creaTipoVisita();
            }
            String[] datiLuogo = CLI.messaggioCreazione(CREAZIONE_LUOGO);
            Luogo l = configuratore.creaLuogo(datiLuogo);
            String s = null;
            do {
                CLI.stampaMessaggio("scegli un tipo di visita: (x per uscire)");
                s = CLI.sceltaString(DatiCondivisi.getElencoTipiVisita().visualizza());
                if(!s.equals("x")){
                    l.getElencoVisite().aggiungi(DatiCondivisi.getElencoTipiVisita().getElementByKey(s));
                    DatiCondivisi.getElencoTipiVisita().getElementByKey(s).aggiungiLuogo(l);
                }
            } while(!s.equals("x"));
        } catch (Exception e) {
            StandardError.stampaErrore(e.toString());
            return;
        }
    }

    private void creaTipoVisita(){
        try {
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
            int minPartecipanti = Integer.parseInt(datiLuogo[7]);
            int maxPartecipanti = Integer.parseInt(datiLuogo[8]);
            ArrayList<Giorni> giorniDisponibili = new ArrayList<>();
            String s = null;
            do {
                s = CLI.sceltaString("insersci un giorno: (x per uscire)");
                if(!s.equals("x")){
                    giorniDisponibili.add(Giorni.fromString(s.toLowerCase()));
                }
            } while(!s.equals("x"));
            Elenco<Volontario> elencoV = new Elenco<>();
            s = null;
            do {
                CLI.stampaMessaggio("scegli volontario: (x per uscire)");
                s = CLI.sceltaString(DatiCondivisi.getElencoUtenti().getClassiUtente(Volontario.class).visualizza());
                if(!s.equals("x")){
                    elencoV.aggiungi((Volontario)DatiCondivisi.getElencoUtenti().getElementByKey(s));
                }
            } while(!s.equals("x"));
            configuratore.creaTipoVisita(titolo, descrizione, puntoIncontro, periodoAnno, giorniDisponibili, oraInizio, durata, bigliettoNecessario, minPartecipanti, maxPartecipanti);
            TipoVisita v = DatiCondivisi.getElencoTipiVisita().getElementByKey(titolo);
            for(Volontario volo : elencoV.getElenco().values()){
                volo.aggiungiVisitaVolontario(v);
                v.aggiungiVolontario(volo);
            }
        } catch (Exception e) {
            StandardError.stampaErrore(e.toString());
            return;
        }
    }

    private void creaVolontario(){
        try {
            String[] dati = CLI.creaUtente("volontario");
            configuratore.creaVolontario(dati);
        } catch (Exception e) {
            StandardError.stampaErrore(e.toString());
            return;
        }
    }
}
