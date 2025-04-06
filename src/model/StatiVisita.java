package model;
public enum StatiVisita {
   
    VISITA_PROPOIBILE("Visita proponibile"),
    VISITA_PROPOSTA("Visita proposta"),
    VISITA_COMPLETA("Visita completa"),
    VISITA_CONFERMATA("Visita confermata"),
    VISITA_EFFETTUATA("Visita effettuata"),
    VISITA_CANCELLATA("Visita cancellata");

    private final String statoVisita;

    StatiVisita(String statoVisita) {
        this.statoVisita = statoVisita;
    }

    public String getStatoVisita() {
        return statoVisita;
    }
}
