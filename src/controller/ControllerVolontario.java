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
                "--> ";


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
                CLI.stampaMessaggio("Scelta non valida");
        }
        return true;
    }

    private void visualizzaTipiVisita() {
        CLI.stampaMessaggio(volontario.visualizzaVolo());
    }

    private void esprimiDisponibilita() {

        LocalDate[] intervallo = GestoreVisite.getInstance().intervallo();
        if(intervallo != null){
            CLI.stampaMessaggio("intervallo: " + intervallo[0] + " - " + intervallo[1]);
            CLI.stampaMessaggio("indica le disponibilita' nell'intervallo da " 
                + intervallo[0] + " a " + intervallo[1]);

            String s = "";
            while (!s.equals("x")) {

                ArrayList<LocalDate> date = new ArrayList<>(volontario.elencaDisponibilita(intervallo[0], intervallo[1]));
                
                for(LocalDate d : date){
                    CLI.stampaMessaggio("giorno disponibile -> " + d.toString());;
                }
                
                s = CLI.sceltaString("inserisci la data oppure 'x' per terminare");
                if (!s.equals("x")) {
                    try {
                        LocalDate dataGiorno = LocalDate.parse(s);
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

}
