public class Configuratore extends Utente{
    
    public Configuratore(Credenziale credenziali){
        super(credenziali);
    }

    protected void modificaPassword(String nuovaPassword){
        this.getCredenziali().setPassword(nuovaPassword);
    }   
}
