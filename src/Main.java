import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import controller.Controller;
import model.DatiCondivisi;
import model.GestioneTempo;
import model.GestoreVisite;
import model.RipristinoDati;

public class Main {
    
    public static void main(String[] args) throws Exception{    

        boolean primaConfigurazione = RipristinoDati.datiRipristino();

        if(primaConfigurazione)
            DatiCondivisi.setDataUltimaEsecuzione(LocalDate.of(2025, 4, 30));
        
        
        GestoreVisite.getInstance();
        GestioneTempo.getInstance();




        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String oggi = GestioneTempo.getInstance().getDataCorrente().format(formatter);
        System.out.println("Data simulata: " + oggi);

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
            scheduler.scheduleAtFixedRate(() -> {
            GestioneTempo.getInstance().passaggioTempo();
            }, 0, 1, TimeUnit.SECONDS);
        

        Controller.start(primaConfigurazione);
        // Volontario x = (Volontario)DatiCondivisi.getElencoUtenti().getElementByKey("volo1");
        // System.err.println(x.getElencoDisponibilita().getElenco().size());
        
        scheduler.shutdown();
    }    
}


