package model;

import java.util.HashMap;

public class Volontario extends Utente {

    private static Elenco<TipoVisita> elencoTipiVisita = new Elenco<TipoVisita>();
    
    public Volontario(String username, String password, boolean primoAccesso) {
        super(username, password, primoAccesso);
    }

    public Volontario(String username, String password) {
        super(username, password);
    }

    public HashMap<String, TipoVisita> getElencoTipiVisita() {
        return elencoTipiVisita.getElenco();
    }

    public void aggiungiVisitaVolontario(TipoVisita visita) {
        elencoTipiVisita.aggiungi(visita);
    }

    public String visualizzaVolo() {
        StringBuffer s = new StringBuffer();
        for(TipoVisita t : elencoTipiVisita.getElenco().values()) {
            s.append(t.toString());
            s.append("\n");
        }
        return super.toString() + "\n" + "Elenco visite: " + s.toString();
    }
}