package model;

public class DatiCondivisi {
    private Elenco<Utente> elencoUtenti;
    private Elenco<TipoVisita> elencoTipiVisita;
    private Elenco<Luogo> elencoLuoghi;
    private Elenco<Visita> elencoVisite;
    private Elenco<ListaDate> datePrecluse;

    public DatiCondivisi(Elenco<Utente> elencoUtenti, Elenco<TipoVisita> elencoTipiVisita, Elenco<Luogo> elencoLuoghi, 
                            Elenco<Visita> elencoVisite, Elenco<ListaDate> datePrecluse) {
        this.elencoUtenti = elencoUtenti;
        this.elencoTipiVisita = elencoTipiVisita;
        this.elencoLuoghi = elencoLuoghi;
        this.elencoVisite = elencoVisite;
        this.datePrecluse = datePrecluse;
    }

    public Elenco<Utente> getElencoUtenti() {
        return elencoUtenti;
    }

    public Elenco<TipoVisita> getElencoTipiVisita() {
        return elencoTipiVisita;
    }

    public Elenco<Luogo> getElencoLuoghi() {
        return elencoLuoghi;
    }

    public Elenco<Visita> getVisite() {
        return elencoVisite;
    }

    public Elenco<ListaDate> getDatePrecluse() {
        return datePrecluse;
    }
}
