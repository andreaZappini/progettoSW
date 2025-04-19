package model;
public abstract class Utente {
    
    private String username;
    private String password;
    private boolean primoAccesso;


    //costruttore per creazione manuale da parte di un configuratore
    public Utente(String username, String password) {
        this.username = username;
        this.password = password;
        this.primoAccesso = true;
    }

    //costruttore per creazione automatica da parte del sistema dopo lettura da XML
    public Utente(String username, String password, boolean primoAccesso) {
        this.username = username;
        this.password = password;
        this.primoAccesso = primoAccesso;
    }
    
    public boolean getPrimoAccesso() {
        return this.primoAccesso;
    }

    public void setPassword(String psw){
        this.password = psw;
    }

    //servirebbe un po' di sale cit. Il Mitico Barons
    public boolean controllaPassword(String psw){
        return this.password.equals(psw);
    }

    @Override
    public String toString() {
        return this.username;
    }

    public void setPrimoAccesso() {
        this.primoAccesso = false;
    }
}
