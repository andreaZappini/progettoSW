import java.util.ArrayList;

public class Configuratore extends Utente{

    public Configuratore(String username, String password, boolean primoAccesso) {
        super(username, password, primoAccesso);
    }

    public Configuratore(String username, String password) {
        super(username, password);
    }

    public Luogo creaLuogo(String[] datiLuogo, CorpoDati c){

        String codiceLuogo = datiLuogo[0];
        String descrizione = datiLuogo[1];
        String collocazione = datiLuogo[2];

        Luogo l = new Luogo(codiceLuogo, descrizione, collocazione);
        c.aggiungiLuogo(l);

        return l;
    }

    public Utente creaVolontario(String[] dati, Elenco<Utente> e){

        
        String username = dati[0];
        String password = dati[1];
        Utente x = new Volontario(username, password);
        e.aggiungi(x);

        return x;
    }

    public void creaTipoVisita(
            String titolo,
            String descrizione,
            String puntoIncontro,
            String periodoAnno,
            ArrayList<Giorni> giorniDisponibili,
            double oraInizio,
            int durata,
            String bigliettoNecessario,
            int minPartecipanti,
            int maxPartecipanti,
            Elenco<Volontario> elencoVolontari,
            Elenco<TipoVisita> e){

        TipoVisita x = new TipoVisita(titolo, descrizione, puntoIncontro, periodoAnno, giorniDisponibili,
                          oraInizio, durata, bigliettoNecessario,
                          minPartecipanti, maxPartecipanti, elencoVolontari);
        e.aggiungi(x);
    }

    public void setNumeroMassimoIscrittiFruitore(int num, CorpoDati c){
        c.setNumeroMassimoIscrittiFruitore(num);
    }

    public String visualizzaVisiteDiLuogo(String codiceLuogo, CorpoDati corpoDati) {
        Luogo l = corpoDati.getElencoLuoghi().getElementByKey(codiceLuogo);
        if (l == null) {
            return "Luogo non trovato.";
        }
        return l.visualizzaVisite();
    }
    
}
