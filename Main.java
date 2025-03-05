import java.io.File;
import java.util.ArrayList;

import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

public class Main {
    
    public static void main(String[] args) throws Exception {

        //gestione tempo e avvio thread
        GestioneTempo gt = new GestioneTempo();
        Thread t = new Thread(gt);
        t.start();
        gt.start();

        

        Elenco<Utente> elencoUtenti = XMLUtilities.leggiXML(
            new File("fileXML/utenti.xml"), 
            "Utente", 
            elemento -> creaUtente(elemento)
        );
        
        Elenco<TipoVisita> elencoTipiVisita = XMLUtilities.leggiXML(
            new File("fileXML/TipiVisita.xml"), 
            "TipoVisita", 
            elemento -> creaTipoVisita(elemento, elencoUtenti)
        );    
        
        Elenco<Luogo> elencoLuogohi = XMLUtilities.leggiXML(
            new File ("fileXML/luoghi.xml"),
            "Luogo",
            elemento -> creaLuogo(elemento)
        );
        CorpoDati corpoDati = null;

        CLI.start(elencoUtenti,corpoDati, elencoTipiVisita);
    }

    private static Luogo  creaLuogo(Element elemento){
        String codiceLuogo = elemento.getElementsByTagName("codiceLuogo").item(0).getTextContent();
        String descrizione = elemento.getElementsByTagName("descrizione").item(0).getTextContent();
        String collocazioneGeografica = elemento.getElementsByTagName("collocazioneGeografica").item(0).getTextContent();

        return new Luogo(codiceLuogo, descrizione, collocazioneGeografica);
    }

    private static Utente creaUtente(Element elemento){
        String nomeNodo = elemento.getNodeName(); // Configuratore o Volontario
        String username = elemento.getElementsByTagName("username").item(0).getTextContent();
        String password = elemento.getElementsByTagName("password").item(0).getTextContent();
    
        if (nomeNodo.equalsIgnoreCase("Configuratore")) {
            return new Configuratore(username, password);
        } else if (nomeNodo.equalsIgnoreCase("Volontario")) {
            return new Volontario(username, password);
        } else {
            throw new IllegalArgumentException("Tipo di utente sconosciuto: " + nomeNodo);
        }
    }

    private static TipoVisita creaTipoVisita(Element elemento, Elenco<Utente> elencoUtenti){
        String titolo = elemento.getElementsByTagName("titolo").item(0).getTextContent();
        String descrizione = elemento.getElementsByTagName("descrizione").item(0).getTextContent();
        String puntoIncontro = elemento.getElementsByTagName("PuntoIncontro").item(0).getTextContent();
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
                System.out.println("Errore: Giorno non valido trovato nel XML - " + giornoStr);
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
                volontario = (Volontario)u;
            elencoVolontari.aggiungi(volontario);
        }
        return new TipoVisita(titolo, descrizione,
                                puntoIncontro, periodoAnno, 
                                giorniDisponibili, oraInizio, 
                                durata, bigliettoNecessario,
                                minPartecipanti, maxPartecipanti, 
                                elencoVolontari);
    }
}


