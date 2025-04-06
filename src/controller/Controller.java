package controller;

import model.*;
import view.CLI;

public class Controller{
    
    private static CorpoDati corpoDati;
    private static Elenco<Utente> elencoUtenti = new Elenco<>();
    private static Elenco<Luogo> elencoLuoghi = new Elenco<>();
    private static Elenco<TipoVisita> elencoTipiVisita = new Elenco<>();
    private static boolean working;

    public static void start() throws Exception{
        
        String[] datiRipristino = new String[3];
        datiRipristino = RipristinoDati.datiRipristino();
        // recuperaDatiXML();

        boolean primaConfigurazione = Boolean.parseBoolean(datiRipristino[0].trim());
        System.out.println(primaConfigurazione);
        if(!primaConfigurazione){
            String ambitoTerritoriale = datiRipristino[1];
            int numeroMaxIscritti = Integer.parseInt(datiRipristino[2]);
            corpoDati = new CorpoDati(ambitoTerritoriale, numeroMaxIscritti);
            DatiCondivisi dati = RipristinoDati.datiCondivisi();

            elencoUtenti = dati.getElencoUtenti();
            elencoTipiVisita = dati.getElencoTipiVisita();
            elencoLuoghi = dati.getElencoLuoghi();

            corpoDati.ripristinaElenco(elencoLuoghi);
        }else{
            RipristinoDati.primoConfiguratore(elencoUtenti);
            boolean primoAccesso = true;
            Utente x = null;
            while(primoAccesso){
                String[] datiUtente = CLI.login();
                String username = datiUtente[0];
                String password = datiUtente[1];
              
                if(!elencoUtenti.contiene(username)){
                    CLI.stampaMessaggio("utente non trovato");
                }else{
                    x = elencoUtenti.getElementByKey(username);
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
            ControllerConfiguratore cc = new ControllerConfiguratore(
                (Configuratore)x, corpoDati, elencoUtenti, elencoTipiVisita);
            corpoDati = cc.primaConfigurazione();
        }

        working = true;
        while(working){
            Utente x;
            while(true){
                String[] datiUtente = CLI.login();
                String username = datiUtente[0];
                String password = datiUtente[1];


                if(!elencoUtenti.contiene(username)){
                    CLI.stampaMessaggio("utente non trovato");
                }else{
                    x = elencoUtenti.getElementByKey(username);
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
                    ControllerConfiguratore cc = new ControllerConfiguratore(config, corpoDati, elencoUtenti, elencoTipiVisita);
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
        RipristinoDati.salvataggioDati(elencoUtenti, elencoTipiVisita, corpoDati);
    }
}