package model;
import java.util.ArrayList;

public class TipoVisita {

    private String titolo;
    private String descrizione;
    private String puntoIncontro;
    private String periodoAnno;
    private ArrayList<Giorni> giorniDisponibili;
    private double oraInizio;
    private int durata;
    private String bigliettoNecessario;
    private int minPartecipanti;
    private int maxPartecipanti;
    private Elenco<Volontario> elencoVolontari;

    public TipoVisita(String titolo, 
                        String descrizione, 
                        String puntoIncontro, 
                        String periodoAnno, 
                        ArrayList<Giorni> giorniDisponibili, 
                        double oraInizio, 
                        int durata, 
                        String bigliettoNecessario, 
                        int minPartecipanti, 
                        int maxPartecipanti, 
                        Elenco<Volontario> elencoVolontari) {
                            
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.puntoIncontro = puntoIncontro;
        this.periodoAnno = periodoAnno;
        this.giorniDisponibili = giorniDisponibili;
        this.oraInizio = oraInizio;
        this.durata = durata;
        this.bigliettoNecessario = bigliettoNecessario;
        this.minPartecipanti = minPartecipanti;
        this.maxPartecipanti = maxPartecipanti;
        this.elencoVolontari = elencoVolontari;
    } 

    public String getTitolo(){
        return this.titolo;
    }

    public String getDescrizione() {
        return this.descrizione;
    }

    public String getPuntoIncontro() {
        return this.puntoIncontro;
    }

    public String getPeriodoAnno() {
        return this.periodoAnno;
    }

    public ArrayList<Giorni> getGiorniDisponibili() {
        return this.giorniDisponibili;
    }

    public double getOraInizio() {
        return this.oraInizio;
    }

    public int getDurata() {
        return this.durata;
    }

    public String getBigliettoNecessario() {
        return this.bigliettoNecessario;
    }

    public int getMinPartecipanti() {
        return this.minPartecipanti;
    }

    public int getMaxPartecipanti() {
        return this.maxPartecipanti;
    }

    public Elenco<Volontario> getElencoVolontari() {
        return this.elencoVolontari;
    }

    public String toStringTipoVisita(){
    	StringBuffer s = new StringBuffer();
    	
    	if (!this.giorniDisponibili.isEmpty()) {
            s.append(this.giorniDisponibili.get(0));
            for (int i = 1; i < this.giorniDisponibili.size(); i++) {
                s.append(", ").append(this.giorniDisponibili.get(i));
            }
        }
    	
    	return this.titolo + " [" + this.descrizione + "]\n"
        + "Il punto d'incontro per questo tipo di visita sarà: " + this.puntoIncontro
        + "\ne si terrà in " + this.periodoAnno + " nei giorni " + s.toString() 
        + "\na partire dalle ore " + this.oraInizio
        + " per una durata di " + this.durata + " minuti.\n"
        + "Necessità del biglietto: " + this.bigliettoNecessario 
        + "\n(numero minimo partecipanti: " + this.minPartecipanti
        + "; numero massimo partecipanti: " + this.maxPartecipanti + ")\n"
        + "L'elenco delle guide volontarie è:\n" + this.elencoVolontari.visualizza();
    }

    @Override
    public String toString(){
        return this.titolo;
    }
}
