package model;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class RipristinoDati {

    private static final Map<Volontario, ArrayList<String>> elencoVisiteVolontario = new HashMap<>();
    private static final Map<TipoVisita, ArrayList<String>> elencoVolontariVisite = new HashMap<>();

    public static void primoConfiguratore() throws Exception {
        Elenco<Utente> elencop = XMLUtilities.leggiXML(
            new File("src/fileXML/configuratore1.xml"), 
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

    public static void salvataggioDati() throws Exception {
        XMLUtilities.scriviXML(new File("src/fileXML/utentiBE.xml"), DatiCondivisi.getElencoUtenti(), "Utente");
        XMLUtilities.scriviXML(new File("src/fileXML/tipiVisita.xml"), DatiCondivisi.getElencoTipiVisita(), "TipiVisita");
        XMLUtilities.scriviXML(new File("src/fileXML/luoghi.xml"), DatiCondivisi.getElencoLuoghi(), "Luoghi");
        XMLUtilities.scriviXML(new File("src/fileXML/visite.xml"), DatiCondivisi.getVisite(), "Visite");
        XMLUtilities.scriviXML(new File("src/fileXML/datePrecluse.xml"), DatiCondivisi.getDatePrecluse(), "Date");
        XMLUtilities.scriviArchivioXML(new File("src/fileXML/archivioVisite.xml"), DatiCondivisi.getArchivio());

        String[] dati = {"false", DatiCondivisi.getAmbitoTerritoriale(), 
                         String.valueOf(DatiCondivisi.getNumeroMassimoIscrittiFruitore()),
                         DatiCondivisi.getRaccoltaDisponibilitaMese1().toString(),
                         DatiCondivisi.getRaccoltaDisponibilitaMese2().toString(),
                         GestioneTempo.getInstance().getDataCorrente().toString()
                        };
        String[] campi = {"primaConfigurazione", "ambitoTerritoriale", "numeroMassimoIscritti", 
        "raccoltaDisponibilitaMese1", "raccoltaDisponibilitaMese2", "dataUltimaEsecuzione"};
        XMLUtilities.scriviXML(new File("src/fileXML/datiExtra.xml"), dati, campi, "datiDiConfigurazione");
    }

    private static Utente creaUtente(Element elemento) {
        String nomeNodo = elemento.getNodeName();
        String username = elemento.getElementsByTagName("username").item(0).getTextContent();
        String password = elemento.getElementsByTagName("password").item(0).getTextContent();
        boolean primoAccesso = Boolean.parseBoolean(
            elemento.getElementsByTagName("primoAccesso").item(0).getTextContent());

        if (nomeNodo.equalsIgnoreCase("Configuratore")) 
            return new Configuratore(username, password, primoAccesso);

        else if (nomeNodo.equalsIgnoreCase("Fruitore")) 
           return new Fruitore(username, password);

        else if (nomeNodo.equalsIgnoreCase("Volontario")) {
            
            ArrayList<String> listaTipiVisita = new ArrayList<>();
            NodeList tipiVisitaNodes = elemento.getElementsByTagName("elencoTipiVisita");
            Element tipiVisitaElement = (Element) tipiVisitaNodes.item(0);
            NodeList singoloTipoVisitaNodes = tipiVisitaElement.getElementsByTagName("elemento");

            for(int i = 0; i < singoloTipoVisitaNodes.getLength(); i++) {
                String titolo = singoloTipoVisitaNodes.item(i).getTextContent().trim();
                listaTipiVisita.add(titolo);
            }

            Elenco<LocalDate> listaDisponibilita = new Elenco<>();
            NodeList disponibilitaNodes = elemento.getElementsByTagName("elencoDisponibilita");
            Element disponibilitaElement = (Element) disponibilitaNodes.item(0);
            NodeList singolaDisponibilitaNodes = disponibilitaElement.getElementsByTagName("elemento");
            
            for (int i = 0; i < singolaDisponibilitaNodes.getLength(); i++) {
                LocalDate data = LocalDate.parse(singolaDisponibilitaNodes.item(i).getTextContent().trim());
                listaDisponibilita.aggiungi(data);
            }

            Volontario v = new Volontario(username, password, primoAccesso);
            v.setElencoDisponibilita(listaDisponibilita);
            elencoVisiteVolontario.put(v, listaTipiVisita);
            return v;
        } else {
            throw new IllegalArgumentException("Tipo di utente sconosciuto: " + nomeNodo);
        }
    }

    private static TipoVisita creaTipoVisita(Element elemento, Elenco<Utente> elencoUtenti) {
        String titolo = elemento.getElementsByTagName("titolo").item(0).getTextContent();
        String descrizione = elemento.getElementsByTagName("descrizione").item(0).getTextContent();
        String puntoIncontro = elemento.getElementsByTagName("puntoIncontro").item(0).getTextContent();
        String periodoAnno = elemento.getElementsByTagName("periodoAnno").item(0).getTextContent();
        String bigliettoNecessario = elemento.getElementsByTagName("bigliettoNecessario").item(0).getTextContent();    

        ArrayList<Giorni> giorniDisponibili = new ArrayList<>();
        NodeList giorniNodes = elemento.getElementsByTagName("giorniDisponibili");
        Element giorniElement = (Element) giorniNodes.item(0);
        NodeList singoloGiornoNodes = giorniElement.getElementsByTagName("elemento");

        for (int i = 0; i < singoloGiornoNodes.getLength(); i++) {
            String giornoStr = singoloGiornoNodes.item(i).getTextContent().trim();
            if (giornoStr.isEmpty()) continue;
            try {
                Giorni giorno = Giorni.valueOf(giornoStr);
                giorniDisponibili.add(giorno);
            } catch (IllegalArgumentException e) {
                System.err.println("Errore nel parsing del giorno: '" + giornoStr + "'");
            }
        }
    
        
        double oraInizio = Double.parseDouble(elemento.getElementsByTagName("oraInizio").item(0).getTextContent());
        int durata = Integer.parseInt(elemento.getElementsByTagName("durata").item(0).getTextContent());
        int minPartecipanti = Integer.parseInt(elemento.getElementsByTagName("minPartecipanti").item(0).getTextContent());
        int maxPartecipanti = Integer.parseInt(elemento.getElementsByTagName("maxPartecipanti").item(0).getTextContent());

        NodeList volontariNodes = elemento.getElementsByTagName("elemento");
        ArrayList<String> listaVolontari = new ArrayList<>();
        for (int i = 0; i < volontariNodes.getLength(); i++) {
            listaVolontari.add(volontariNodes.item(i).getTextContent().trim());
        }

        TipoVisita v = new TipoVisita(titolo, descrizione, puntoIncontro, periodoAnno, giorniDisponibili, oraInizio,
                durata, bigliettoNecessario, minPartecipanti, maxPartecipanti);

        elencoVolontariVisite.put(v, listaVolontari);
        return v;
    }

    public static void datiCondivisi() throws Exception {
        Elenco<Utente> elencoUtenti = XMLUtilities.leggiXML(new File("src/fileXML/utentiBE.xml"), "Utenti", elemento -> creaUtente(elemento));
        Elenco<Fruitore> fruitori = elencoUtenti.getClassiUtente(Fruitore.class);
        Elenco<TipoVisita> elencoTipiVisita = XMLUtilities.leggiXML(new File("src/fileXML/tipiVisita.xml"), "TipoVisita", elemento -> creaTipoVisita(elemento, elencoUtenti));
        Elenco<Luogo> elencoLuoghi = XMLUtilities.leggiXML(new File("src/fileXML/luoghi.xml"), "Luoghi", elemento -> creaLuogo(elemento, elencoTipiVisita));
        Elenco<ListaDate> datePrecluse = XMLUtilities.leggiXML(new File("src/fileXML/datePrecluse.xml"), "Date", elemento -> creaDate(elemento));
        Elenco<ListaVisite> elencoVisite = XMLUtilities.leggiListaVisiteXML(new File("src/fileXML/visite.xml"), elencoTipiVisita, fruitori);
        

        DatiCondivisi.setVisite(elencoVisite);
        DatiCondivisi.setElencoLuoghi(elencoLuoghi);
        DatiCondivisi.setElencoTipiVisita(elencoTipiVisita);
        DatiCondivisi.setDatePrecluse(datePrecluse);
        DatiCondivisi.setElencoUtenti(elencoUtenti);

        for (Volontario v : elencoVisiteVolontario.keySet()) {
            for (String titolo : elencoVisiteVolontario.get(v)) {
                TipoVisita tipo = elencoTipiVisita.getElementByKey(titolo);
                if (tipo != null) {
                    v.aggiungiVisitaVolontario(tipo);
                    tipo.aggiungiVolontario(v);
                }
            }
        }

        for(Luogo l : elencoLuoghi.getElenco().values())
            for(TipoVisita t : l.getElencoVisite().getElenco().values())
                t.aggiungiLuogo(l);

        Elenco<Visita> archivio = XMLUtilities.leggiArchivioXML(new File("src/fileXML/archivioVisite.xml"), elencoTipiVisita, fruitori);
        DatiCondivisi.setArchivioVisite(archivio);

        for(Visita v : DatiCondivisi.getVisite().getElementByKey("0").getVisite().getElenco().values()){
            for(Fruitore f : v.getIscrizioni().keySet()) {
                if(fruitori.contiene(f.toString())) {
                    f.aggiungiPrenotazione(v);
                } else {
                    System.err.println("Fruitore " + f.getUsername() + " non trovato nell'elenco fruitori.");
                }
            }
        }

        for(Visita v : DatiCondivisi.getArchivio().getElenco().values()){
            for(Fruitore f : v.getIscrizioni().keySet()) {
                if(fruitori.contiene(f.toString())) {
                    f.aggiungiPrenotazione(v);
                } else {
                    System.err.println("Fruitore " + f.getUsername() + " non trovato nell'elenco fruitori.");
                }
            }
        }
    }

    private static Luogo creaLuogo(Element elemento, Elenco<TipoVisita> visite) {
        String codiceLuogo = elemento.getElementsByTagName("codiceLuogo").item(0).getTextContent();
        String descrizione = elemento.getElementsByTagName("descrizione").item(0).getTextContent();
        String collocazioneGeografica = elemento.getElementsByTagName("collocazioneGeografica").item(0).getTextContent();
        Luogo l = new Luogo(codiceLuogo, descrizione, collocazioneGeografica);

        NodeList visiteNodes = elemento.getElementsByTagName("visite");
        for (int i = 0; i < visiteNodes.getLength(); i++) {
            String nomeVisita = visiteNodes.item(i).getTextContent().trim();
            TipoVisita t = visite.getElementByKey(nomeVisita);
            if (t != null) {
                l.aggiungiAElencoVisite(t);
            }
        }
        return l;
    }

    private static ListaDate creaDate(Element elemento) {
        String chiave = elemento.getElementsByTagName("chiave").item(0).getTextContent();
        ListaDate listaDate = new ListaDate(chiave);
    
        // Prendi il nodo <date>
        NodeList dateNodes = elemento.getElementsByTagName("date");
        if (dateNodes.getLength() > 0) {
            Element dateElement = (Element) dateNodes.item(0);
            NodeList elementi = dateElement.getElementsByTagName("elemento");
    
            for (int i = 0; i < elementi.getLength(); i++) {
                String dataStr = elementi.item(i).getTextContent().trim();
                try {
                    LocalDate data = LocalDate.parse(dataStr);
                    listaDate.aggiungiData(data);
                } catch (Exception e) {
                    System.err.println("Errore nel parsing della data: '" + dataStr + "'");
                }
            }
        }
    
        return listaDate;
    }
    


    public static boolean datiRipristino() throws Exception {
        String[] dati = XMLUtilities.leggiXML(
            new File("src/fileXML/datiExtra.xml").getCanonicalFile(),
            "datiDiConfigurazione"
        );

        if (!Boolean.parseBoolean(dati[0])) {
            DatiCondivisi.setAmbitoTerritoriale(dati[1]);
            DatiCondivisi.setNumeroMassimoIscrittiFruitore(Integer.parseInt(dati[2]));
            if(dati[3].equals("APERTA")) {
                DatiCondivisi.apriRaccoltaDisponibilitaMese1();
            }else{
                DatiCondivisi.chiudiRaccoltaDisponibilitaMese1();
            }

            if(dati[4].equals("APERTA")) {
                DatiCondivisi.apriRaccoltaDisponibilitaMese2();
            }else{
                DatiCondivisi.chiudiRaccoltaDisponibilitaMese2();
            }
            DatiCondivisi.setDataUltimaEsecuzione(LocalDate.parse(dati[5]));
        }
        return Boolean.parseBoolean(dati[0]);
    }
} 
