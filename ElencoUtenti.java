import java.util.HashMap;

public class ElencoUtenti {
  
  private HashMap<String, Utente> elencoUtenti;

  public ElencoUtenti() {
    this.elencoUtenti = new HashMap<String, Utente>();
  }

  public void aggiungiUtente(Utente utente) {
    this.elencoUtenti.put(utente.getCredenziali().getUsername(), utente);
  }

  public void rimuoviUtente(Utente utente) {
    this.elencoUtenti.remove(utente.getCredenziali().getUsername());
  }

}