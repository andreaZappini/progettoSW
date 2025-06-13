package model;
import java.time.LocalDate;
import java.util.ArrayList;

public class Configuratore extends Utente{

    public Configuratore(String username, String password, boolean primoAccesso) {
        super(username, password, primoAccesso);
    }

    public Configuratore(String username, String password) {
        super(username, password);
    }

    public Luogo creaLuogo(String[] datiLuogo){

        String codiceLuogo = datiLuogo[0];
        String descrizione = datiLuogo[1];
        String collocazione = datiLuogo[2];

        Luogo l = new Luogo(codiceLuogo, descrizione, collocazione);

        DatiCondivisi.aggiungiLuogo(l);
        return l;
    }

    public Utente creaVolontario(String[] dati){

        String username = dati[0];
        String password = dati[1];
        Utente x = new Volontario(username, password);
        DatiCondivisi.aggiungiUtente(x);

        return x;
    }

    public Utente creaConfiguratore(String[] dati){

        String username = dati[0];
        String password = dati[1];
        Utente x = new Configuratore(username, password);
        DatiCondivisi.aggiungiUtente(x);

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
            Elenco<Volontario> elencoVolontari){

        TipoVisita x = new TipoVisita(titolo, descrizione, puntoIncontro, periodoAnno, giorniDisponibili,
                          oraInizio, durata, bigliettoNecessario,
                          minPartecipanti, maxPartecipanti);

        for (Volontario v : elencoVolontari.getElenco().values()) {
            x.aggiungiVolontario(v);
        }
        DatiCondivisi.aggiungiTipoVisita(x);
    }

    public void setNumeroMassimoIscrittiFruitore(int num){
        DatiCondivisi.setNumeroMassimoIscrittiFruitore(num);
    }
    
    public void aggiungiDatePrecluse(LocalDate datePrecluse) {
        DatiCondivisi.aggiungiData(datePrecluse);
    }
}
