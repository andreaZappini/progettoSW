package model;

import view.CLI;

import java.io.File;
import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class RipristinoDati {
    
    public static void primoConfiguratore(Elenco<Utente> e) throws Exception{
        //sistema controller e relativi metodi di recupero dati
        Elenco<Utente> elencop = XMLUtilities.leggiXML(
            new File("fileXML/configuratore1.xml"), 
            "Utente", 
            elemento -> {
                String username = elemento.getElementsByTagName("username").item(0).getTextContent();
                String password = elemento.getElementsByTagName("password").item(0).getTextContent();
                boolean primoAccesso = Boolean.parseBoolean(
                    elemento.getElementsByTagName("primoAccesso").item(0).getTextContent());
                return new Configuratore(username, password, primoAccesso);
            });
            e.aggiungi(elencop);
    }

    public static void salvataggioDati(Elenco<Utente> elencoUtenti, Elenco<TipoVisita> elencoTipiVisita, 
            CorpoDati corpoDati) throws Exception{
        XMLUtilities.scriviXML(
            new File("fileXML/utenti.xml"), elencoUtenti, "Utente");

        XMLUtilities.scriviXML(
            new File("fileXML/tipiVisita.xml"), elencoTipiVisita, "TipiVisita");

        XMLUtilities.scriviXML(
            new File("fileXML/luoghi.xml"), corpoDati.getElencoLuoghi(), "Luoghi");

        String[] dati = {"false", corpoDati.getAmbitoTerritoriale(), 
                String.valueOf(corpoDati.getNumeroMassimoIscrittiFruitore())};
        String[] campi = {"primaConfigurazione", "ambitoTerritoriale", "numeroMassimoIscritti"};
        XMLUtilities.scriviXML(
            new File("fileXML/datiExtra.xml"), dati, campi, "datiDiConfigurazione");
    
    }

        private static Utente creaUtente(Element elemento){
        String nomeNodo = elemento.getNodeName(); // Configuratore o Volontario
        String username = elemento.getElementsByTagName("username").item(0).getTextContent();
        String password = elemento.getElementsByTagName("password").item(0).getTextContent();
        boolean primoAccesso = Boolean.parseBoolean(elemento.getElementsByTagName("primoAccesso").item(0).getTextContent());
    
        if (nomeNodo.equalsIgnoreCase("Configuratore")) {
            return new Configuratore(username, password, primoAccesso);
        } else if (nomeNodo.equalsIgnoreCase("Volontario")) {
            return new Volontario(username, password, primoAccesso);
        } else {
            throw new IllegalArgumentException("Tipo di utente sconosciuto: " + nomeNodo);
        }
    }

    private static TipoVisita creaTipoVisita(Element elemento, Elenco<Utente> elencoUtenti){

        String titolo = elemento.getElementsByTagName("titolo").item(0).getTextContent(); 
        String descrizione = elemento.getElementsByTagName("descrizione").item(0).getTextContent();
        String puntoIncontro = elemento.getElementsByTagName("puntoIncontro").item(0).getTextContent();
        String periodoAnno = elemento.getElementsByTagName("periodoAnno").item(0).getTextContent();
        String bigliettoNecessario = elemento.getElementsByTagName("bigliettoNecessario").item(0).getTextContent();
        ArrayList<Giorni> giorniDisponibili = new ArrayList<>();
        NodeList giorniNodes = elemento.getElementsByTagName("giorno");
        for (int i = 0; i < giorniNodes.getLength(); i++) {
            String giornoStr = giorniNodes.item(i).getTextContent();
            try {
                Giorni giorno = Giorni.fromString(giornoStr); // Metodo fromString() nell'enum
                giorniDisponibili.add(giorno);
            } catch (IllegalArgumentException e) {
               CLI.stampaMessaggio("Errore: Giorno non valido trovato nel XML - " + giornoStr);
            }
        }

        // Conversione in numeri
        double oraInizio = Double.parseDouble(elemento.getElementsByTagName("oraInizio").item(0).getTextContent());
        int durata = Integer.parseInt(elemento.getElementsByTagName("durata").item(0).getTextContent());
        int minPartecipanti = Integer.parseInt(elemento.getElementsByTagName("minPartecipanti").item(0).getTextContent());
        int maxPartecipanti = Integer.parseInt(elemento.getElementsByTagName("maxPartecipanti").item(0).getTextContent());

        // Recupero dell'elenco di volontari (supponendo sia una lista di username da convertire)
        Elenco<Volontario> elencoVolontari = new Elenco<>();
        NodeList volontariNodes = elemento.getElementsByTagName("volontario");
        for (int i = 0; i < volontariNodes.getLength(); i++) {
            String nomeVolontario = volontariNodes.item(i).getTextContent();
            Utente u = elencoUtenti.getElementByKey(nomeVolontario);
            Volontario volontario = null;
            if(u instanceof Volontario)
                volontario = (Volontario) u;
                //funziona
                //System.out.println(volontario);
            elencoVolontari.aggiungi(volontario);
        }
        return new TipoVisita(titolo, descrizione,
                                puntoIncontro, periodoAnno, 
                                giorniDisponibili, oraInizio, 
                                durata, bigliettoNecessario,
                                minPartecipanti, maxPartecipanti, 
                                elencoVolontari);
    }

    private static Luogo creaLuogo(Element elemento, Elenco<TipoVisita> visite){
        String codiceLuogo = elemento.getElementsByTagName("codiceLuogo").item(0).getTextContent();
        String descrizione = elemento.getElementsByTagName("descrizione").item(0).getTextContent();
        String collocazioneGeografica = elemento.getElementsByTagName("collocazioneGeografica").item(0).getTextContent();

        Luogo l = new Luogo(codiceLuogo, descrizione, collocazioneGeografica);

        Elenco<TipoVisita> elencoTipoVisita = new Elenco<>();

        NodeList visiteNodes = elemento.getElementsByTagName("visite");
        for (int i = 0; i < visiteNodes.getLength(); i++) {
            String nomeVisita = visiteNodes.item(i).getTextContent().trim();
            //funziona
            //System.out.println(nomeVisita);
            TipoVisita t = visite.getElementByKey(nomeVisita);
            //stampa null
            elencoTipoVisita.aggiungi(t);
            l.aggiungiAElencoVisite(t);
        }
        return l;
    }

    public static String[] datiRipristino() throws Exception{
        return XMLUtilities.leggiXML(
            new File("fileXML/datiExtra.xml"),
            "datiDiConfigurazione"
        );
    }

    public static DatiCondivisi datiCondivisi() throws Exception{
        Elenco<Utente> elencoUtenti = XMLUtilities.leggiXML(
            new File("fileXML/utenti.xml"), 
            "Utenti", 
            elemento -> creaUtente(elemento)
        );

        System.out.println(elencoUtenti.visualizza());

        Elenco<TipoVisita> elencoTipiVisita = XMLUtilities.leggiXML(
            new File("fileXML/tipiVisita.xml"), 
            "TipoVisita", 
            elemento -> creaTipoVisita(elemento, elencoUtenti)
        );

        Elenco<Luogo> elencoLuoghi = XMLUtilities.leggiXML(
            new File ("fileXML/luoghi.xml"),
            "Luoghi",
            elemento -> creaLuogo(elemento, elencoTipiVisita)
        );
        
        return new DatiCondivisi(elencoUtenti, elencoTipiVisita, elencoLuoghi);

    }
}