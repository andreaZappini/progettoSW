import java.time.format.DateTimeFormatter;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import controller.Controller;
import model.GestioneTempo;

public class Main {
    
    public static void main(String[] args) throws Exception{        
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String oggi = GestioneTempo.getInstance().getDataCorrente().format(formatter);
        System.out.println("Data simulata: " + oggi);

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
            scheduler.scheduleAtFixedRate(() -> {
            GestioneTempo.getInstance().checkCambioMese();
        }, 0, 1, TimeUnit.SECONDS);

        Controller.start();
    }
}


