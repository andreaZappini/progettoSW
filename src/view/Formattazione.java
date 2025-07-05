package view;

import model.*;
/**
 * Classe per la formattazione e stampa di informazioni relative agli utenti e alle visite.
 * 
 * Questa classe contiene metodi statici per stampare le informazioni di un volontario,
 * i dettagli di un tipo di visita e le informazioni generali di un utente.
 */
public class Formattazione {
    
    public static void stampaVolotario(Volontario v) {
        
        stampaUtente(v);
        for(TipoVisita t : v.getElencoTipiVisita().getElenco().values()) {
            stampaTipoVisita(t);
            System.out.println("\n");
        }
    }

    public static void stampaTipoVisita(TipoVisita t){
        if(!t.getGiorniDisponibili().isEmpty()) {
            System.out.print(t.getGiorniDisponibili().get(0));
            for (int i = 1; i < t.getGiorniDisponibili().size(); i++) {
                System.out.print(", " + t.getGiorniDisponibili().get(i));
            }
        }

        System.out.println(t.getTitolo() + " [" + t.getDescrizione() + "]");
        System.out.println("Il punto d'incontro per questo tipo di visita sarà: " + t.getPuntoIncontro());
        System.out.println("e si terrà in " + t.getPeriodoAnno() + " nei giorni " + t.getGiorniDisponibili());
        System.out.println("a partire dalle ore " + t.getOraInizio() + " per una durata di " + t.getDurata() + " minuti.");
        System.out.println("Necessità del biglietto: " + t.getBigliettoNecessario());
        System.out.println("(numero minimo partecipanti: " + t.getMinPartecipanti() + 
                           "; numero massimo partecipanti: " + t.getMaxPartecipanti() + ")");
        System.out.println("L'elenco delle guide volontarie è:");
        System.out.println(t.getElencoVolontari().visualizza());
    }

    public static void stampaUtente(Utente u) {
        System.out.println("Nome: " + u.getUsername());
    }

    public static void stampaVisita(Visita v){
        System.out.println("Tipo di visita: " + v.getTipo().getTitolo());
        System.out.println("Data visita: " + v.getDataVisita());
        System.out.println("Stato visita: " + v.getStato());
        System.out.println("Numero iscritti: " + v.getIscritti() + "/" + v.getMaxPartecipanti());
    }

    public static void stampaLuogo(Luogo l){
        System.out.println("Nome luogo: " + l.getCodiceLuogo());
        System.out.println("Descrizione: " + l.getDescrizione());
        System.out.println("Collocazione: " + l.getCollocazioneGeografica());
        System.out.println("Tipi di visita disponibili:");
        for(TipoVisita t : l.getElencoVisite().getElenco().values()) {
            System.out.println("- " + t.getTitolo());
        }
    }

    public 
}