package model;

public class Posizione {
    
    private static String via;
    private static String citta;

    public Posizione(String via, String citta) {
        this.via = via;
        this.citta = citta;
    }
    public String getVia() {
        return via;
    }
    public String getCitta() {
        return citta;
    }
}
