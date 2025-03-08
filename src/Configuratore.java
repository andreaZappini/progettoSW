public class Configuratore extends Utente{

    @SuppressWarnings("unused")
    private String username;
    @SuppressWarnings("unused")
    private String password;
    private Boolean primoAccesso;

    
    public Configuratore(String username, String password) {
        super(username, password);

        this.primoAccesso = true;
    }

    public Configuratore(String username, String password, boolean primoAccesso){
        super(username, password);
        this.primoAccesso = primoAccesso;
    }
    
    public boolean getPrimoAccesso(){
        return this.primoAccesso;
    }

    public void setPrimoAccesso(){
        this.primoAccesso = false;
    }
}
