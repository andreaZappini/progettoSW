package controller;

import model.*;
import view.CLI;

public class Controller{
    
    private static boolean working;

    public static void start() throws Exception{
        
        boolean primaConfigurazione = RipristinoDati.datiRipristino();
        // recuperaDatiXML();

        System.out.println(primaConfigurazione);
        if(!primaConfigurazione){
            RipristinoDati.datiCondivisi();
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
                    //scelta  = CLI.sceltaInt(AZIONI_VOLONTARIO);
                    break;
                case "Fruitore":
                    //scelta = CLI.sceltaInt(AZIONI_UTENTE);
                    break;
                default:
                    break;
            }
        }
        CLI.chiudiScanner();
        RipristinoDati.salvataggioDati();
    }
}