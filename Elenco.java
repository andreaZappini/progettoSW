import java.util.HashMap;

public class Elenco<T> {
    private HashMap<String, T> elenco;

    public Elenco() {
        this.elenco = new HashMap<String, T>();
    }

    public void aggiungi(T elemento) {
        if(contiene(elemento)) {
            throw new IllegalArgumentException("Elemento già presente");
        }else {
            this.elenco.put(elemento.toString(), elemento);
        }
    }
    
    public void rimuovi(T elemento) {
        if(contiene(elemento)) {
            this.elenco.remove(elemento.toString());
        }else {
            throw new IllegalArgumentException("Elemento non presente");
        }
    }

    public boolean contiene(T elemento) {
        return this.elenco.containsKey(elemento.toString());
    }
}
