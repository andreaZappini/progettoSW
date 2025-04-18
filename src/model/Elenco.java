package model;
import java.util.HashMap;

public class Elenco<T> {
    private HashMap<String, T> elenco;

    public Elenco() {
        this.elenco = new HashMap<String, T>();
    }

    public HashMap<String, T> getElenco() {
        return this.elenco;
    }

    public void aggiungi(T elemento) {
        if(contiene(elemento.toString())) {
            throw new IllegalArgumentException("Elemento già inserito! ");
        }else {
            this.elenco.put(elemento.toString(), elemento);
        }
    }

    public void aggiungi(Elenco<T> elenco) {
        this.elenco.putAll(elenco.getElenco());
    }

    //vedele se funge -> si funge
    @SuppressWarnings("hiding")
    public <T extends Utente> Elenco<T> getClassiUtente(Class<T> tipo) {
        Elenco<T> elencoUtenti = new Elenco<>();
        for (String key : this.elenco.keySet()) {
            Object obj = this.elenco.get(key);
            if (tipo.isInstance(obj)) {
                elencoUtenti.aggiungi(tipo.cast(obj));
            }
        }
        return elencoUtenti;
    }
    
    public void rimuovi(T elemento) {
        if(contiene(elemento.toString())) {
            this.elenco.remove(elemento.toString());
        }else {
            throw new IllegalArgumentException("Elemento NON presente!");
        }
    }

    public boolean contiene(String chiave) {
        return this.elenco.containsKey(chiave);
    }

    public String toStringElenco() {
        return this.elenco.toString();
    }

    public T getElementByKey(String chiave) {
        return this.elenco.get(chiave);
    }

    public String visualizza() {
        StringBuffer s = new StringBuffer();
        for (String key : this.elenco.keySet()) {
            s.append("\t" + "- " + this.elenco.get(key).toString());
            s.append("\n");
        }
        return s.toString();
    }
    
    public int numeroElementi() {
    	return this.elenco.size();
    }
    
    //non usato
    public String visualizzaLuogo() {
        StringBuffer s = new StringBuffer();
        for (String key : this.elenco.keySet()) {
        	Object obj = this.elenco.get(key); // Ottieni l'oggetto dalla HashMap
            
            if (obj instanceof Luogo) { // Controlla se è un'istanza di Luogo
                Luogo luogo = (Luogo) obj; // Casting a Luogo
                s.append("- " + luogo.toStringLuogo()); // Usa toStringLuogo()
            } else {
                s.append("\t - L'elemento NON è un Luogo!");
            }
            s.append("\n");
        }
        return s.toString();
    }
    
    //non usato
    public String visualizzaTipoVisita() {
        StringBuffer s = new StringBuffer();
        for (String key : this.elenco.keySet()) {
        	Object obj = this.elenco.get(key); // Ottieni l'oggetto dalla HashMap
            
            if (obj instanceof TipoVisita) { // Controlla se è un'istanza di Luogo
                TipoVisita tipoVisita = (TipoVisita) obj; // Casting a Luogo
                s.append("- " + tipoVisita.toStringTipoVisita()); // Usa toStringLuogo()
            } else {
                s.append("\t - L'elemento NON è un TipoVisita!");
            }
            s.append("\n");
        }
        return s.toString();
    }
}