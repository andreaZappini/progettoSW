package controller;

import model.*;
import view.CLI;

public class Controller{
    
    private static boolean working;

    public static void start(boolean primaConfigurazione) throws Exception{
        
        if(!primaConfigurazione){

            RipristinoDati.datiCondivisi();
            GestioneTempo.getInstance().buchiTemporali();
        }else{

            RipristinoDati.primoConfiguratore();
            boolean primoAccesso = true;
            Utente x = null;
            while(primoAccesso){
                String[] datiUtente = CLI.login();
                String username = datiUtente[0];
                String password = datiUtente[1];
              
                if(!DatiCondivisi.getElencoUtenti().contiene(username)){
                    CLI.stampaMessaggio("utente non trovato");
                }else{
                    x = DatiCondivisi.getElencoUtenti().getElementByKey(username);
                    if(x.controllaPassword(password)){
                        CLI.stampaMessaggio("accesso consentito");
                        x.getPrimoAccesso();
                        CLI.stampaMessaggio("PRIMO ACCESSO -> cambia password: ");
                        String nuovaPassword = CLI.cambiaPassword();
                        x.setPassword(nuovaPassword);
                        x.setPrimoAccesso();
                        primoAccesso = false;
                    }else{
                        CLI.stampaMessaggio("password errata");
                    }
                }
            }
            new ControllerConfiguratore((Configuratore)x).primaConfigurazione();
        }

        working = true;
        while(working){

            int classeUtente = CLI.sceltaInt("Seleziona la classe dell'utente che vuoi creare:\n1) Configuratore/Volontario\n2) Fruitore");
            switch(classeUtente){
                case 1:
                    loginConfiguratoreVolontario();
                    break;
                case 2:
                    ControllerFruitore.start();
                    break;
            }
        }
        RipristinoDati.salvataggioDati();
    }

    private static boolean loginConfiguratoreVolontario() throws Exception{
        Utente x;
            while(true){
                String[] datiUtente = CLI.login();
                String username = datiUtente[0];
                String password = datiUtente[1];


                if(!DatiCondivisi.getElencoUtenti().contiene(username)){
                    CLI.stampaMessaggio("utente non trovato");
                }else{
                    x = DatiCondivisi.getElencoUtenti().getElementByKey(username);
                    if(x.controllaPassword(password)){
                        CLI.stampaMessaggio("accesso consentito");
                        if(x.getPrimoAccesso()){
                            CLI.stampaMessaggio("PRIMO ACCESSO -> cambia password: ");
                            String nuovaPassword = CLI.cambiaPassword();
                            x.setPassword(nuovaPassword);
                            x.setPrimoAccesso();
                        }
                        break;
                    }else{
                        CLI.stampaMessaggio("password errata");
                    }
                }
            }
            switch(x.getClass().getSimpleName()){
                case "Configuratore":
                    Configuratore config = (Configuratore)x;
                    ControllerConfiguratore cc = new ControllerConfiguratore(config);
                    working = cc.start();    
                    break;
                case "Volontario":
                    Volontario vol = (Volontario)x;
                    ControllerVolontario cv = new ControllerVolontario(vol);
                    cv.start();
                    break;
                default:
                    break;
            }
        return working;
    }
}