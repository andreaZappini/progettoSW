import java.io.File;
import java.util.ArrayList;

import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

public class Main {
    
    public static void main(String[] args) throws Exception {        

        Elenco<Utente> elencoUtenti = XMLUtilities.leggiXML(
            new File("fileXML/utenti.xml"), 
            "Utenti", 
            elemento -> creaUtente(elemento)
        );

        //funziona
        //System.out.println(elencoUtenti.visualizza());
        
        Elenco<TipoVisita> elencoTipiVisita = XMLUtilities.leggiXML(
            new File("fileXML/tipiVisita.xml"), 
            "TipiVisita", 
            elemento -> creaTipoVisita(elemento, elencoUtenti)
        );
        System.out.println(elencoTipiVisita.visualizza());
        
        Elenco<Luogo> elencoLuoghi = XMLUtilities.leggiXML(
            new File ("fileXML/luoghi.xml"),
            "Luoghi",
            elemento -> creaLuogo(elemento, elencoTipiVisita)
        );

        String[] datiRipristino = new String[6];
        datiRipristino = XMLUtilities.leggiXML(
            new File("fileXML/datiExtra.xml"),
            "datiDiConfigurazione"
        );
        CorpoDati corpoDati;
        boolean primaConfigurazione = Boolean.parseBoolean(datiRipristino[0].trim());
        if(!primaConfigurazione){
            String ambitoTerritoriale = datiRipristino[1];
            int numeroMaxIscritti = Integer.parseInt(datiRipristino[2]);
            corpoDati = new CorpoDati(ambitoTerritoriale, numeroMaxIscritti);
            corpoDati.ripristinaElenco(elencoLuoghi);
        }else{
            corpoDati = null;
        }
        
        long tempo = Long.parseLong(datiRipristino[3]);

        
        //gestione tempo e avvio thread
        GestioneTempo gt = new GestioneTempo();
        Thread t = new Thread(gt);
        t.start();
        gt.start(tempo);

        CLI.start(elencoUtenti, corpoDati, elencoTipiVisita);
    }

    public static void salvataggioDati(Elenco<Utente> elencoUtenti, Elenco<TipoVisita> elencoTipiVisita, CorpoDati corpoDati) throws Exception{
        XMLUtilities.scriviXML(
            new File("fileXML/utentibu.xml"), elencoUtenti, "Utente");

        XMLUtilities.scriviXML(
            new File("fileXML/tipiVisitabu.xml"), elencoTipiVisita, "TipiVisita");

        XMLUtilities.scriviXML(
            new File("fileXML/luoghibu.xml"), corpoDati.getElenco(), "Luoghi");
    }

    private static Luogo  creaLuogo(Element elemento, Elenco<TipoVisita> visite){
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
            System.out.println("visita: " + t);
            elencoTipoVisita.aggiungi(t);
            l.aggiungiAElencoVisite(t);
        }

        return l;
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
}


