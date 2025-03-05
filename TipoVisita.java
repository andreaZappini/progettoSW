
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
}
