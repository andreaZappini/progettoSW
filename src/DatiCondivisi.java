public class DatiCondivisi {
    private Elenco<Utente> elencoUtenti;
    private Elenco<TipoVisita> elencoTipiVisita;
    private Elenco<Luogo> elencoLuoghi;

    public DatiCondivisi(Elenco<Utente> elencoUtenti, Elenco<TipoVisita> elencoTipiVisita, Elenco<Luogo> elencoLuoghi) {
        this.elencoUtenti = elencoUtenti;
        this.elencoTipiVisita = elencoTipiVisita;
        this.elencoLuoghi = elencoLuoghi;
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

}
