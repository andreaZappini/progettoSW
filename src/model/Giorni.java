package model;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Locale;

public enum Giorni {
    LUNEDI("lunedì"),
    MARTEDI("martedì"),
    MERCOLEDI("mercoledì"),
    GIOVEDI("giovedì"),
    VENERDI("venerdì"),
    SABATO("sabato"),
    DOMENICA("domenica");

    private final String descrizione;

    Giorni(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getdescrizione() {
        return descrizione;
    }

    public static Giorni fromString(String gg) {
        for (Giorni giorno : Giorni.values()) {
            if (giorno.getdescrizione().equalsIgnoreCase(gg)) {
                return giorno;
            }
        }
        throw new IllegalArgumentException("Nessun giorno corrisponde alla stringa: " + gg);
    }

    public static Giorni traduci(LocalDate giorno) {
        
        DayOfWeek giornoDellaSettimana = giorno.getDayOfWeek();

        switch (giornoDellaSettimana) {
            case MONDAY:
                return Giorni.LUNEDI;
            case TUESDAY:
                return Giorni.MARTEDI;
            case WEDNESDAY:
                return Giorni.MERCOLEDI;
            case THURSDAY:
                return Giorni.GIOVEDI;
            case FRIDAY:
                return Giorni.VENERDI;
            case SATURDAY:
                return Giorni.SABATO;
            case SUNDAY:
                return Giorni.DOMENICA;
            default:
                throw new IllegalArgumentException("Giorno non valido: " + giornoDellaSettimana);
        }
    }

    public static boolean equalsGiorno1(ArrayList<Giorni> giorni, LocalDate giorno, Volontario v) {


        return false;
    }

    public static boolean equals(Giorni gg, LocalDate d){
        return gg.getdescrizione().equalsIgnoreCase(d.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ITALY));
    }
}
