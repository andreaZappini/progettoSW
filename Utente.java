public abstract class Utente {
    
    private String username;
    private String password;
    private boolean primoAccesso;

    public Utente(String username, String password) {
        this.username = username;
        this.password = password;
        this.primoAccesso = true;
    }
    
    public boolean getPrimoAccesso() {
        return this.primoAccesso;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword(){
        return this.password;
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
