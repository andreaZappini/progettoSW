package model;

import java.time.LocalDate;
import java.util.HashMap;

public class Visita {
    
    private StatiVisita stato;
    LocalDate dataVisita;
    private TipoVisita tipo;
    private HashMap<Fruitore, Integer> iscrizioni; //HashMap<Cliente, PersoneIscritte>
    private int iscritti;

    //costruttore per creazione visita runtime
    public Visita(LocalDate dataVisita, TipoVisita tipo) {
        this.stato = StatiVisita.VISITA_PROPONIBILE;
        this.dataVisita = dataVisita;   
        this.tipo = tipo;
        this.iscrizioni = new HashMap<Fruitore, Integer>();
        this.iscritti = 0;
    }

    //costruttore per lettura xml
    public Visita(LocalDate dataVisita, TipoVisita tipo, StatiVisita stato, HashMap<Fruitore, Integer> iscrizioni, int iscritti) {
        this.stato = stato;
        this.dataVisita = dataVisita;   
        this.tipo = tipo;
        this.iscrizioni = iscrizioni;
        this.iscritti = iscritti;
    }   

    public StatiVisita getStato() {
        return stato;
    }

    public LocalDate getDataVisita() {
        return dataVisita;
    }

    public int getMaxPartecipanti() {
        return tipo.getMaxPartecipanti();
    }

    public int getMinPartecipanti() {
        return tipo.getMinPartecipanti();
    }

    public int getIscritti() {
        return iscritti;
    }

    public TipoVisita getTipo() {
        return tipo;
    }

    public void cambiaStato(StatiVisita stato) {
        this.stato = stato;
    }

    public HashMap<Fruitore, Integer> getIscrizioni() {
        return iscrizioni;
    }

    @Override
    public String toString() {
        return this.tipo.toString() + "-" + this.dataVisita.toString();
    }

    public void aggiungiIscrizione(Fruitore fruitore, int numPersone) {

        if(this.iscritti + numPersone <= this.tipo.getMaxPartecipanti()){
            iscritti += numPersone;
            if(iscrizioni.containsKey(fruitore)){
                int numPersonePrecedenti = iscrizioni.get(fruitore);
                iscrizioni.put(fruitore, numPersonePrecedenti + numPersone);
            } else {
                iscrizioni.put(fruitore, numPersone);
            }
        }else
            throw new IllegalArgumentException("Numero di iscritti superato");
    }

    public void rimuoviPrenotaione(Fruitore fruitore) {
        if(iscrizioni.containsKey(fruitore)){
            int numPersonePrecedenti = iscrizioni.get(fruitore);
            iscritti -= numPersonePrecedenti;
            iscrizioni.remove(fruitore);
            if(stato == StatiVisita.VISITA_COMPLETA)
                cambiaStato(StatiVisita.VISITA_PROPOSTA);
        }else
            throw new IllegalArgumentException("Non sei iscritto a questa visita");
    }
}