import java.time.format.DateTimeFormatter;

import controller.Controller;
import model.GestioneTempo;

public class Main {
    
    public static void main(String[] args) throws Exception{        
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String oggi = GestioneTempo.getInstance().getDataCorrente().format(formatter);
        System.out.println("Data simulata: " + oggi);
        Controller.start();
    }
}


