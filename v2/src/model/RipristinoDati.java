package model;

import view.CLI;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class RipristinoDati {
    
    public static void primoConfiguratore() throws Exception{

        //sistema controller e relativi metodi di recupero dati
        Elenco<Utente> elencop = XMLUtilities.leggiXML(
            new File("progettoSW/fileXML/configuratore1.xml"), 
            "Utente", 
            elemento -> {
                String username = elemento.getElementsByTagName("username").item(0).getTextContent();
                String password = elemento.getElementsByTagName("password").item(0).getTextContent();
                boolean primoAccesso = Boolean.parseBoolean(
                    elemento.getElementsByTagName("primoAccesso").item(0).getTextContent());
                return new Configuratore(username, password, primoAccesso);
            });
            DatiCondivisi.aggiungiElencoUtente(elencop);
    }

    public static void salvataggioDati() throws Exception{
        XMLUtilities.scriviXML(
            new File("progettoSW/fileXML/utenti.xml"), DatiCondivisi.getElencoUtenti(), "Utente");

        XMLUtilities.scriviXML(
            new File("progettoSW/fileXML/tipiVisita.xml"), DatiCondivisi.getElencoTipiVisita(), "TipiVisita");

        XMLUtilities.scriviXML(
            new File("progettoSW/fileXML/luoghi.xml"), DatiCondivisi.getElencoLuoghi(), "Luoghi");

        XMLUtilities.scriviXML(
            new File("progettoSW/fileXML/visite.xml"), DatiCondivisi.getVisite(), "Visite");

        XMLUtilities.scriviXML(
            new File("progettoSW/fileXML/datePrecluse.xml"), DatiCondivisi.getDatePrecluse(), "Date");

        String[] dati = {"false", DatiCondivisi.getAmbitoTerritoriale(), 
                String.valueOf(DatiCondivisi.getNumeroMassimoIscrittiFruitore())};
        String[] campi = {"primaConfigurazione", "ambitoTerritoriale", "numeroMassimoIscritti"};
        XMLUtilities.scriviXML(
            new File("progettoSW/fileXML/datiExtra.xml"), dati, campi, "datiDiConfigurazione");
    
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

    // private static ListaDate creaDate(Element elemento) {
    //     String chiave = elemento.getElementsByTagName("chiave").item(0).getTextContent().trim();
    
    //     NodeList dateNodes = elemento.getElementsByTagName("data");
    //     ArrayList<LocalDate> dateList = new ArrayList<>();
    
    //     for (int i = 0; i < dateNodes.getLength(); i++) {
    //         String dataStr = dateNodes.item(i).getTextContent().trim();
    //         LocalDate data = LocalDate.parse(dataStr);
    //         dateList.add(data);
    //     }
    
    //     return new ListaDate(chiave, dateList);
    // }

    private static LocalDate creaDate(Element elemento) {
        String dataStr = elemento.getTextContent().trim();
        LocalDate data = LocalDate.parse(dataStr);
    
        return data;
    }
    
    

    private static Visita creaVisita(Element elemento, Elenco<TipoVisita> elencoTipiVisita){
        String dataVisitaStr = elemento.getElementsByTagName("dataVisita").item(0).getTextContent();
        LocalDate dataVisita = LocalDate.parse(dataVisitaStr);
        String tipoVisitaStr = elemento.getElementsByTagName("tipo").item(0).getTextContent();
        TipoVisita tipo = elencoTipiVisita.getElementByKey(tipoVisitaStr);
        return new Visita(dataVisita, tipo);
    }

    public static boolean datiRipristino() throws Exception{
        
        String[] dati =  XMLUtilities.leggiXML(
        new File("progettoSW/fileXML/datiExtra.xml"),
            "datiDiConfigurazione"
        );

        if(!Boolean.parseBoolean(dati[0])){
            DatiCondivisi.setAmbitoTerritoriale(dati[1]);
            DatiCondivisi.setNumeroMassimoIscrittiFruitore(Integer.parseInt(dati[2]));
        }

        return Boolean.parseBoolean(dati[0]);
    }

    public static void datiCondivisi() throws Exception{

        Elenco<Utente> elencoUtenti = XMLUtilities.leggiXML(
            new File("progettoSW/fileXML/utenti.xml"), 
            "Utenti", 
            elemento -> creaUtente(elemento)
        );

        Elenco<TipoVisita> elencoTipiVisita = XMLUtilities.leggiXML(
            new File("progettoSW/fileXML/tipiVisita.xml"), 
            "TipoVisita", 
            elemento -> creaTipoVisita(elemento, elencoUtenti)
        );

        Elenco<Luogo> elencoLuoghi = XMLUtilities.leggiXML(
            new File("progettoSW/fileXML/luoghi.xml"),
            "Luoghi",
            elemento -> creaLuogo(elemento, elencoTipiVisita)
        );

        Elenco<LocalDate> datePrecluse = XMLUtilities.leggiXML(
            new File("progettoSW/fileXML/datePrecluse.xml"),
            "Date",
            elemento -> creaDate(elemento)
        );

        Elenco<Visita> elencoVisite = XMLUtilities.leggiXML(
            new File("progettoSW/fileXML/visite.xml"),
            "Visite",  
            elemento -> creaVisita(elemento, elencoTipiVisita)
        );

        DatiCondivisi.setVisite(elencoVisite);
        DatiCondivisi.setElencoLuoghi(elencoLuoghi);
        DatiCondivisi.setElencoTipiVisita(elencoTipiVisita);
        DatiCondivisi.setDatePrecluse(datePrecluse);
        DatiCondivisi.setElencoUtenti(elencoUtenti);

        System.out.println("date recuperate" + datePrecluse.getElenco().size());
    }
}