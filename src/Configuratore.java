public class Configuratore extends Utente{

    private Boolean primoAccesso;
    
    public Configuratore(String username, String password) {
        super(username, password);

        this.primoAccesso = true;
    } 
    
    public boolean getPrimoAccesso(){
        return this.primoAccesso;
    }
}
