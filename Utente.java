public abstract class Utente {
    
    private Credenziale credenziali;

    public Utente(Credenziale credenziali) {
        this.credenziali = credenziali;
    }

    public Credenziale getCredenziali() {
        return this.credenziali;
    }

    public void setCredenziali(Credenziale credenziali) {
        this.credenziali = credenziali;
    }
}
