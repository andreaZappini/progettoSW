public class Volontario extends Utente {

    private boolean primoAccesso;
    
    public Volontario(String username, String password) {
        super(username, password);

        this.primoAccesso = true;
    }

    public Volontario(String username, String password, boolean primoAccesso){
        super(username, password);

        this.primoAccesso = primoAccesso;
    }

    public boolean getPrimoAccesso(){
        return this.primoAccesso;
    }
}