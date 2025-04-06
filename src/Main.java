import controller.Controller;
import model.GestioneTempo;

public class Main {
    
    public static void main(String[] args) throws Exception{        
        
        int[] data = GestioneTempo.giorno();
        System.out.println(data[0] + " " + GestioneTempo.nomeMese(data[1]) + " " + data[2]);
        Controller.start();
    }
}


