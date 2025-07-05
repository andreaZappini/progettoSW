package model;


//classe creata per il pattern Factory di grasp
// in modo da poter creare utenti di tipo diverso
// senza dover modificare il codice esistente
// --> metodi tolti da classe Configuturatore e da GestioneUtenti

public class UserFactory {

    public static Configuratore creaConfiguratore(String username, String password) {
        Configuratore configuratore = new Configuratore(username, password);
        DatiCondivisi.aggiungiUtente(configuratore);
        return configuratore;
    }

    public static Volontario creaVolontario(String username, String password) {
        Volontario volontario = new Volontario(username, password);
        DatiCondivisi.aggiungiUtente(volontario);
        return volontario;
    }

    public static Fruitore creaFruitore(String username, String password) {
        Fruitore fruitore = new Fruitore(username, password);
        DatiCondivisi.aggiungiUtente(fruitore);
        return fruitore;
    }
}
