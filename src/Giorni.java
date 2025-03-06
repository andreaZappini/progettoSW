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
}
