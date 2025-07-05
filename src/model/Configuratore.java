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

        try{
            String username = dati[0];
            String password = dati[1];
            return UserFactory.creaVolontario(username, password);
        }catch (IllegalArgumentException e){
            throw new IllegalArgumentException("Errore nella creazione del volontario: " + e.getMessage());
        }
    }

    public Utente creaConfiguratore(String[] dati){

        try{
            String username = dati[0];
            String password = dati[1];
            return UserFactory.creaConfiguratore(username, password);
        }catch (IllegalArgumentException e){
            throw new IllegalArgumentException("Errore nella creazione del configuratore: " + e.getMessage());
        }
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
            int maxPartecipanti){

        TipoVisita x = new TipoVisita(titolo, descrizione, puntoIncontro, periodoAnno, giorniDisponibili,
                          oraInizio, durata, bigliettoNecessario,
                          minPartecipanti, maxPartecipanti);

        DatiCondivisi.aggiungiTipoVisita(x);
    }


    public void setNumeroMassimoIscrittiFruitore(int num){
        DatiCondivisi.setNumeroMassimoIscrittiFruitore(num);
    }
    
    public void aggiungiDatePrecluse(LocalDate datePrecluse) {
        DatiCondivisi.aggiungiDataMese3(datePrecluse);
    }

    public void rimuoviTipoVisitaDaLuogo(Luogo l, TipoVisita t) {
        l.rimuoviDaElencoTipiVisita(t);
        t.rimuoviLuogo(l);
        for(Volontario v : t.getElencoVolontari().getElenco().values()){
            v.rimuoviTipoVisita(t);
            t.rimuoviVolontario(v);
        }
    }

    public void rimuoviLuogo(Luogo l){
        DatiCondivisi.rimuoviLuogo(l);
        for(TipoVisita t : l.getElencoVisite().getElenco().values()){
            t.rimuoviLuogo(l);
            for(Volontario v : t.getElencoVolontari().getElenco().values()){
                v.rimuoviTipoVisita(t);
                t.rimuoviVolontario(v);
            }
        }
    }

    public void rimuoviVolontario(Volontario v) {

        for (TipoVisita t : v.getElencoTipiVisita().getElenco().values()) {
            t.rimuoviVolontario(v);
        }

        DatiCondivisi.rimuoviVolontario(v);
    }
}
