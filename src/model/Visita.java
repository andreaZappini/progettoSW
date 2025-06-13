package model;

import java.time.LocalDate;

public class Visita {
    
    private StatiVisita stato;
    LocalDate dataVisita;
    private TipoVisita tipo;

    //costruttore per creazione visita runtime
    public Visita(LocalDate dataVisita, TipoVisita tipo) {
        this.stato = StatiVisita.VISITA_PROPONIBILE;
        this.dataVisita = dataVisita;   
        this.tipo = tipo;
    }

    //costruttore per lettura xml
    public Visita(LocalDate dataVisita, TipoVisita tipo, StatiVisita stato) {
        this.stato = stato;
        this.dataVisita = dataVisita;   
        this.tipo = tipo;
    }   

    public StatiVisita getStato() {
        return stato;
    }

    public LocalDate getDataVisita() {
        return dataVisita;
    }

    public TipoVisita getTipo() {
        return tipo;
    }

    public void cambiaStato( ) {
       //TODO: implement
    }

    @Override
    public String toString() {
        return this.tipo.toString() + this.dataVisita.toString();
    }
}