package model;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Locale;

public enum Giorni {
    LUNEDI("lunedi"),
    MARTEDI("martedi"),
    MERCOLEDI("mercoledi"),
    GIOVEDI("giovedi"),
    VENERDI("venerdi"),
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

    public static boolean equalsGiorno(ArrayList<Giorni> giorni, LocalDate giorno) {
        for (Giorni g : giorni) {
            if (g.getdescrizione().equals(giorno.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ITALY).toLowerCase())) {
                return true;
            }
        }
        return false;
    }
}
