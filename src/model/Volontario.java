package model;

import java.time.LocalDate;
import java.util.ArrayList;

public class Volontario extends Utente {

    private Elenco<TipoVisita> elencoTipiVisita;
    private Elenco<LocalDate> elencoDisponibilita;
    
    public Volontario(String username, String password, boolean primoAccesso) {
        super(username, password, primoAccesso);

        this.elencoDisponibilita = new Elenco<LocalDate>();
        this.elencoTipiVisita = new Elenco<TipoVisita>();
    }

    public Volontario(String username, String password) {
        super(username, password);

        this.elencoDisponibilita = new Elenco<LocalDate>();
        this.elencoTipiVisita = new Elenco<TipoVisita>();
    }

    public Elenco<LocalDate> getElencoDisponibilita() {
        return elencoDisponibilita;
    }

    public void setElencoDisponibilita(Elenco<LocalDate> elencoDisponibilita) {
        this.elencoDisponibilita = elencoDisponibilita;
    }

    public void setDisponibilita(Elenco<LocalDate> elencoDisponibilita) {
        this.elencoDisponibilita = elencoDisponibilita;
    }

    public void setElencoTipiVisita(Elenco<TipoVisita> elencoTipiVisita) {
        this.elencoTipiVisita = elencoTipiVisita;
    }

    public Elenco<TipoVisita> getElencoTipiVisita() {
        return elencoTipiVisita;
    }

    public void aggiungiVisitaVolontario(TipoVisita visita) {
        this.elencoTipiVisita.aggiungi(visita);
    }

    public void aggiungiDisponibilita(LocalDate data){
        this.elencoDisponibilita.aggiungi(data);
    }

    public String visualizzaVolo() {
        StringBuffer s = new StringBuffer();
        for(TipoVisita t : elencoTipiVisita.getElenco().values()) {
            s.append(t.toString());
            s.append("\n");
        }
        return super.toString() + "\n" + "Elenco visite: " + s.toString();
    }

    public void rimuoviTipoVisita(TipoVisita t) {
        this.elencoTipiVisita.rimuovi(t);
        if(this.elencoTipiVisita.vuoto()){
            DatiCondivisi.rimuoviVolontario(this);
        }
    }

    public ArrayList<LocalDate> elencaDisponibilita(LocalDate i, LocalDate f){
        ArrayList<LocalDate> date = new ArrayList<>();
        for(TipoVisita tv : this.elencoTipiVisita.getElenco().values()){
            for(Giorni gg : tv.getGiorniDisponibili()){
                for(LocalDate d = i; d.isBefore(f); d = d.plusDays(1)){
                    if(Giorni.equals(gg, d)){
                        date.add(d);
                    }
                }
            }
        }
        return date;
    }
}