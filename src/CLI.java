import java.util.Scanner;

public class CLI {
    
    private static Scanner in = new Scanner(System.in);

    public static void chiudiScanner(){
        in.close();
    }

    public static String[] creaCorpoDati(){

        String[] infoCoropDati = new String[2];

        try{
            System.out.printf("Seleziona l'ambito territoriale -> ");
            infoCoropDati[0] = in.nextLine();
            System.out.printf("Inserisci numero massimo iscritti per ogni fruitore -> ");
            infoCoropDati[1] = in.nextLine();
        }catch(Exception e){
            e.printStackTrace();
        }

        return infoCoropDati;
    }
        
     //aggiungi metodo sign in
    public static String[] login(){

        String[] datiUtente = new String[2];

        try{
            System.out.println("username:");
            datiUtente[0] = in.nextLine();
            System.out.println("password:");
            datiUtente[1] = in.nextLine();
        }catch(Exception e){
            e.printStackTrace();
        }
        return datiUtente;
    }

    public static void stampaMessaggio(String msg){
        System.out.println(msg);
    }

    public static String cambiaPassword(){
        String password = null;
        try{
            System.out.printf("nuova password -> ");
            password = in.nextLine();
        }catch(Exception e){
            e.printStackTrace();
        }
        return password;
    }

    public static int sceltaInt(String msg){
        
        int scelta = 0;

        try{
            System.out.printf(msg);
            scelta = Integer.parseInt(in.nextLine());
        }catch(Exception e){
            e.printStackTrace();
        }
        return scelta;
    }

    public static String sceltaString(String msg){
        
        String scelta = null;

        try{
            System.out.printf(msg);
            scelta = in.nextLine();
        }catch(Exception e){
            e.printStackTrace();
        }
        return scelta;
    }

    public static <T> void visualizzaElenco(String elenco){
        
    }
}