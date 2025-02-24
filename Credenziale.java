public class Credenziale {
    
    private String username;
    private String password;

    public Credenziale(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return this.username;
    }

    public void setPassword(String psw){
        this.password = psw;
    }

    //servirebbe un po' di sale cit. Il Mitico Barons
    public boolean controllaPassword(String psw){
        return this.password.equals(psw);
    }
}
