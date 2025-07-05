package model;

public class GestoreFruitori {
    
    private static final GestoreFruitori instance = new GestoreFruitori();

    private GestoreFruitori() {
        // DatiCondivisi.aggiungiUtente(new Fruitore("fruitore", "fruitore"));
    }

    public static GestoreFruitori getInstance() {
        return instance;
    }

    // public void aggiungiFruitore(String username, String password) {
    //     DatiCondivisi.aggiungiUtente(new Fruitore(username, password));
    // }

    public void aggiungiFruitore(String nome, String password) {
        UserFactory.creaFruitore(nome, password);
    }


    public boolean verificaPSW(String p1, String p2) {
        return (p1 != null && p1.equals(p2));
    }

    public void prenotaVisita(Fruitore fruitore, String[] datiPrenotazione) {

        String codiceVisita = datiPrenotazione[0];
        int numPersone = Integer.parseInt(datiPrenotazione[1]);
        if(numPersone > DatiCondivisi.getNumeroMassimoIscrittiFruitore())
            throw new IllegalArgumentException("Numero di persone superiore al massimo consentito");

        try{
            Visita visita = DatiCondivisi.getVisite().getElementByKey("0").getVisite().getElementByKey(codiceVisita);
                    
            if(visita.getStato() == StatiVisita.VISITA_PROPOSTA){
            visita.aggiungiIscrizione(fruitore, numPersone);
            fruitore.aggiungiPrenotazione(visita);
        }
        }catch(IllegalArgumentException e){
            throw e;
        }


    }

    public void annullaPrenotazione(Fruitore f, String c){

        try{
            Visita v = DatiCondivisi.getVisite().getElementByKey("0").getVisite().getElementByKey(c);

            v.rimuoviPrenotaione(f);
            f.rimuoviPrenotazione(v);
        }catch(IllegalArgumentException e){
            throw e;
        }

    }
}
