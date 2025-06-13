package controller;

import java.time.LocalDate;
import java.util.ArrayList;

import model.*;
import view.CLI;

public class ControllerVolontario {

    private Volontario volontario;
    
    private static final String AZIONI_VOLONTARIO =  "\n----------------------------------------------------------------------------------------------"
                + "\nBenvenuto Volontario! Scegli una delle seguenti alternative: \n\n" +
                "1. Visualizzare tutti i tipi di visita a cui sei associato\n" +
                "2. Esprimere le disponibilità in termini di date\n" +
                "3. Logout\n" +
                "------------------------------------------------------------------------------------------------\n";


    public ControllerVolontario(Volontario volontario) {
        this.volontario = volontario;
    }

    public void start(){
        boolean continua = true;
        while(continua) {
            int sccelta = CLI.sceltaInt(AZIONI_VOLONTARIO);
            continua = azioniVolontario(sccelta);
        }
    }

    private boolean azioniVolontario(int scelta) {
        switch(scelta) {
            case 1:
                visualizzaTipiVisita();
                break;
            case 2:
                esprimiDisponibilita();
                break;
            case 3:
                return false;
            default:
                System.out.println("Scelta non valida");
        }
        return true;
    }

    private void visualizzaTipiVisita() {
        System.out.println(volontario.visualizzaVolo());
    }

    private void esprimiDisponibilita() {

        LocalDate[] intervallo = GestoreVisite.getInstance().intervallo();
        if(intervallo != null){
            System.out.println("intervallo: " + intervallo[0] + " - " + intervallo[1]);
            CLI.stampaMessaggio("indica le disponibilita' nell'intervallo da " 
                + intervallo[0] + " a " + intervallo[1]);

            String s = "";
            while (!s.equals("x")) {

                ArrayList<LocalDate> date = new ArrayList<>(volontario.elencaDisponibilita(intervallo[0], intervallo[1]));
                // System.out.println("disponibilita' attuali: " + date.size());
                
                for(LocalDate d : date){
                    CLI.stampaMessaggio("giorno disponibile -> " + d.toString());;
                }
                
                s = CLI.sceltaString("inserisci la data oppure 'x' per terminare");
                if (!s.equals("x")) {
                    try {
                        LocalDate dataGiorno = LocalDate.parse(s);
                        System.out.println("giorno: " + dataGiorno);
                        if(dataGiorno != null){
                            volontario.aggiungiDisponibilita(dataGiorno);
                        }else{
                            CLI.stampaMessaggio("giorno non valido");
                        }
                    } catch (Exception e) {
                        CLI.stampaMessaggio("Formato data non valido");
                    }
                }
            }
        }else
            CLI.stampaMessaggio("Non e' possibile esprimere disponibilita' in questo momento");
    }


    // private void esprimiDisponibilita() {
    //     DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    
    //     LocalDate[] intervallo = GestioneTempo.intervalloDate(1);
    //     System.out.println("intervallo: " + intervallo[0] + " - " + intervallo[1]);
    //     CLI.stampaMessaggio("indica le disponibilita' nell'intervallo da " 
    //         + intervallo[0].format(formatter) + " a " + intervallo[1].format(formatter));
    
        
    //     String s = "";
    //     while (!s.equals("x")) {
    //         s = CLI.sceltaString("inserisci la data oppure 'x' per terminare");
    //         if (!s.equals("x")) {
    //             try {
    //                 LocalDate giorno = LocalDate.parse(s);
    //                 System.out.println("giorno: " + giorno);
    //                 if(giorno != null){
    //                     date.add(giorno);
    //                     System.out.println("ok");
    //                 }else{
    //                     CLI.stampaMessaggio("giorno non valido");
    //                 }
    //             } catch (Exception e) {
    //                 System.out.println("Formato data non valido");
    //             }
    //         }
    //     }

        
    // }
    
}
