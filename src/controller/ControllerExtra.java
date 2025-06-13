package controller;

import java.util.ArrayList;

import model.*;
import view.CLI;
import view.StandardError;

public class ControllerExtra {

    private Configuratore configuratore;

    public ControllerExtra(Configuratore configuratore) {
        this.configuratore = configuratore;
    }

    private static final String AZIONI_EXTRA = 
        "\n----------------------------------------------------------------------------------------------"
        + "\nAZIONI SUGLI ELENCHI\n\n" +
        "1. Introduci nuovo luogo e associalo a Tipi Visita\n" +
        "2. Aggiungi un tipo visita a un luogo gia esistente\n" +
        "3. aggiungi a un tipo di visita uno o piu volontari\n" + 
        "4. Aggiungi un nuovo tipo di visita con i relativi volontari\n" +
        "5. rimuovi luogo\n" +
        "6. rimuovi tipo visita relativo a un luogo\n" +
        "7. rimuovi un volontario da elenco volontari\n" +
        "8. Torna al menu principale\n" +
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
    
    public void start() {
        boolean continua = true;
        while(continua){
            int scelta = CLI.sceltaInt(AZIONI_EXTRA);
            continua = azioniExtraConfiguratore(scelta);
        }
    }

    private boolean azioniExtraConfiguratore(int scelta) {
        switch (scelta) {
            case 1:
                aggiungiLuogo();
                break;
            case 2:
                aggiungiTipoVisitaLuogo();
                break;
            case 3:
                aggiungiVolontarioTipoVisita();
                break;
            case 4:
                aggiungiTipoVisita();
                break;
            case 5:
                rimuoviLuogo();
                break;
            case 6:
                rimuoviTipoVisitaDaLuogo();
                break;
            case 7:
                rimuoviVolontario();
                break;
            case 8:
                return false;
            default:
                return false;
        }
        return true;
    }

    private void rimuoviVolontario(){
        try{            
            CLI.stampaMessaggio("Volontario da rimuovere: ");
            String username = CLI.sceltaString(DatiCondivisi.getElencoUtenti().getClassiUtente(Volontario.class).visualizza());
            Volontario v = (Volontario)DatiCondivisi.getElencoUtenti().getElementByKey(username);
            if (v == null) {
                System.out.println("Volontario non trovato.");
                return;
            }
            configuratore.rimuoviVolontario(v);
        }catch(Exception e){
            StandardError.stampaErrore(e.toString());
            return;
        }
    }

    private void rimuoviLuogo() {
        try{
            CLI.stampaMessaggio("Luogo da rimuovere: ");
            String codiceLuogo = CLI.sceltaString(DatiCondivisi.getElencoLuoghi().visualizza());
            Luogo l = DatiCondivisi.getElencoLuoghi().getElementByKey(codiceLuogo);
            if (l == null) {
                System.out.println("Luogo non trovato.");
                return;
            }
            configuratore.rimuoviLuogo(l);
        }catch(Exception e){
            StandardError.stampaErrore(e.toString());
            return;
        }
    }
    private void rimuoviTipoVisitaDaLuogo() {
        try{
            CLI.stampaMessaggio("che luogo scegli a cui rimuovere il tipo di visita: ");
            String codiceLuogo = CLI.sceltaString(DatiCondivisi.getElencoLuoghi().visualizza());
            Luogo l = DatiCondivisi.getElencoLuoghi().getElementByKey(codiceLuogo);
            if (l == null) {
                System.out.println("Luogo non trovato.");
                return;
            }
            CLI.stampaMessaggio("che tipo di visita vuoi rimuovere: "); 
            String titolo = CLI.sceltaString(l.getElencoVisite().visualizza());
            TipoVisita t = l.getElencoVisite().getElementByKey(titolo);
            if (t == null) {
                System.out.println("Tipo di visita non trovato.");
                return;
            }
            configuratore.rimuoviTipoVisitaDaLuogo(l, t);
        }catch(Exception e){
            StandardError.stampaErrore(e.toString());
            return;
        }
    }

    private void aggiungiTipoVisita(){

        try{
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
            configuratore.creaTipoVisita(titolo, descrizione, puntoIncontro, periodoAnno, giorniDisponibili,
                    oraInizio, durata, bigliettoNecessario, minPartecipanti, maxPartecipanti);
            TipoVisita t = DatiCondivisi.getElencoTipiVisita().getElementByKey(titolo);
            if (t == null) {
                System.out.println("Tipo di visita non trovato.");
                return;
            }
            aggiungiVolontario(t);
        }catch(Exception e){
            StandardError.stampaErrore(e.toString());
            return;
        }
    }

    private void aggiungiLuogo() {
        try{
            String[] datiLuogo = CLI.messaggioCreazione(CREAZIONE_LUOGO);
            Luogo l = configuratore.creaLuogo(datiLuogo);
    
            aggiungiVisita(l);
        }catch(Exception e){
            StandardError.stampaErrore(e.toString());
            return;
        }
    }

    private void aggiungiTipoVisitaLuogo(){
        try{
            CLI.stampaMessaggio("Luogo a cui aggiungere il tipo di visita: ");
            String codiceLuogo = CLI.sceltaString(DatiCondivisi.getElencoLuoghi().visualizza());
            Luogo l = DatiCondivisi.getElencoLuoghi().getElementByKey(codiceLuogo);
            if (l == null) {
                System.out.println("Luogo non trovato.");
                return;
            }
            aggiungiVisita(l);
        }catch(Exception e){
            StandardError.stampaErrore(e.toString());
            return;
        }
    }

    private void aggiungiVisita(Luogo l){
        try{
            String s = "";
            do{
                CLI.stampaMessaggio("scegli un tipo di visita: (x per uscire)");
                s = CLI.sceltaString(DatiCondivisi.getElencoTipiVisita().visualizza());
                if(s.equals("x"))
                    continue;
                TipoVisita t = DatiCondivisi.getElencoTipiVisita().getElementByKey(s);
                if(t == null){
                    System.out.println("Tipo di visita non trovato.");
                    continue;
                }
                t.aggiungiLuogo(l);
                l.aggiungiAElencoVisite(t);
                aggiungiVolontario(t);
            }while(!s.equals("x"));
        }catch(Exception e){
            StandardError.stampaErrore(e.toString());
            return;
        }
    }

    private void aggiungiVolontarioTipoVisita(){
        try{
            CLI.stampaMessaggio("Titolo del tipo di visita a cui aggiungere il volontario: ");
            String titolo = CLI.sceltaString(DatiCondivisi.getElencoTipiVisita().visualizza());
            TipoVisita t = DatiCondivisi.getElencoTipiVisita().getElementByKey(titolo);
            
            if (t == null) {
                System.out.println("Tipo di visita non trovato.");
                return;
            }
            else
                System.out.println("visita -> " + t.toString());
            aggiungiVolontario(t);
        }catch (Exception e){
            StandardError.stampaErrore(e.toString());
            return;
        }

    }

    private void aggiungiVolontario(TipoVisita t){
        String s = "";
        try{
            do{
                Volontario v = null;
                CLI.stampaMessaggio(DatiCondivisi.getElencoUtenti().getClassiUtente(Volontario.class).visualizza());
                s = CLI.sceltaString("aggiungi volontario esistente(e) / aggiungi volontario nuovo(n) / esci(x): ");
    
                if(s.equals("e")){
                    String volontario = CLI.sceltaString("nome del volontasrio da aggiungere: ");
                    v = (Volontario)DatiCondivisi.getElencoUtenti().getElementByKey(volontario);
                }
                else if(s.equals("n"))
                    v = creaVolontario();
    
                else if(s.equals("x"))
                    continue;
                
                else
                    CLI.stampaMessaggio("scelta non valida");
                System.err.println(t.getElencoVolontari().getElenco().size());
                System.err.println(v.getElencoTipiVisita().getElenco().size());
                t.aggiungiVolontario(v);
                v.aggiungiVisitaVolontario(t);
            }while(!s.equals("x"));
        }catch(Exception e){
            StandardError.stampaErrore(e.toString());
            return;
        }
    }

    private Volontario creaVolontario(){

        try{
            String[] dati = CLI.creaUtente("volontario");
            configuratore.creaVolontario(dati);
    
            return (Volontario)DatiCondivisi.getElencoUtenti().getElementByKey(dati[0]);
        }catch(Exception e){
            StandardError.stampaErrore(e.toString());
            return null;
        }
    }
}