package model;

public class ListaVisite {
    
    private String chiave;
    private Elenco<Visita> visite;

    public ListaVisite(String chiave) {
        this.chiave = chiave;
        this.visite = new Elenco<Visita>();
        
    }

    public String getChiave() {
        return chiave;
    }

    public Elenco<Visita> getVisite() {
        return visite;
    }

    @Override
    public String toString() {
        return chiave;
    }

    public void aggiungiVisita(Visita visita) {
        visite.aggiungi(visita);
    }

    public void setVisite(Elenco<Visita> visite) {
        this.visite = visite;
    }

    public String pulisciVisite() {
        StringBuffer s = new StringBuffer();
        for(Visita v : visite.getElenco().values()){
            s.append(v.toString() + "\n");
        }
        return s.toString();
    }
}
