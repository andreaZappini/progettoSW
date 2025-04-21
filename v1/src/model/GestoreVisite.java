package model;

import java.time.LocalDate;
import java.util.ArrayList;

public class GestoreVisite {
    
    private static final GestoreVisite instance = new GestoreVisite();
    private Elenco<Visita> elencoVisite = new Elenco<Visita>();

    private GestoreVisite() {
        // Costruttore privato per impedire l'instanziazione esterna
    }

    public static GestoreVisite getInstance() {
        return instance;
    }

    public static void creaVisiteMese(LocalDate inizio, LocalDate fine, Elenco<TipoVisita> elencoTipiVisita, 
                                Elenco<Visita> elencoVisite, ArrayList<LocalDate> datePrecluse){ 
        for(LocalDate giorno = inizio; !giorno.isAfter(fine); giorno = giorno.plusDays(1)){
            if(!datePrecluse.contains(giorno)){
                for(TipoVisita t : elencoTipiVisita.getElenco().values()){
                    if(Giorni.equalsGiorno(t.getGiorniDisponibili(), giorno)){
                        Visita v = new Visita(giorno, t);
                        elencoVisite.aggiungi(v);
                    }
                }
            }
        }
    }

    public Elenco<Visita> getElencoVisite() {
        return elencoVisite;
    }
}