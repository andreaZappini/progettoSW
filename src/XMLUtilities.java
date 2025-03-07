import java.io.File;
import java.lang.reflect.Field;
import java.util.function.Function;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.*;

public class XMLUtilities {
    
    public static <T> Elenco<T> leggiXML(File file, String contesto, Function<Element, T> parser)throws Exception{

        Elenco<T> elenco = new Elenco<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(file);


        NodeList lista = doc.getElementsByTagName(contesto);

        if (lista.getLength() > 0) {
            Node nodoPadre = lista.item(0); // Dovrebbe esserci solo uno <Utenti>
            NodeList figli = nodoPadre.getChildNodes();

            for (int i = 0; i < figli.getLength(); i++) {
                Node nodo = figli.item(i);
                if (nodo.getNodeType() == Node.ELEMENT_NODE) {
                    Element elemento = (Element) nodo;
                    elenco.aggiungi(parser.apply(elemento)); // Usa la funzione per creare l'oggetto
                }
            }
        }
        return elenco;
    }

    public static String[] leggiXML(File file, String contesto) throws Exception{

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(file);
        doc.getDocumentElement().normalize();
        rimuoviNodiVuoti(doc);

        NodeList lista = doc.getElementsByTagName(contesto);
        if(lista.getLength() != 1)
            throw new Exception("errore di configurazione");
        
        Node nodoPadre = lista.item(0);
        NodeList figli = nodoPadre.getChildNodes();
        String[] risultato = new String[figli.getLength()];

        for (int i = 0; i < figli.getLength(); i++) {
            Node nodo = figli.item(i);
            if (nodo.getNodeType() == Node.ELEMENT_NODE) {
                risultato[i] = nodo.getTextContent().trim();
            }
        }

        for(String s : risultato)
            if(s.isBlank())
                System.out.println("null->" + s);
        return risultato;
    }

    public static void scriviXML(File file, Object o) throws Exception{
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(file);
        doc.getDocumentElement().normalize();
        rimuoviNodiVuoti(doc);

        String nomeClasse = o.getClass().getSimpleName();
        Element elemento = doc.createElement(nomeClasse);

        for(int i = 0; i < o.getClass().getDeclaredFields().length; i++){
            Field f = o.getClass().getDeclaredFields()[i];
            f.setAccessible(true);
            Element campo = doc.createElement(f.getName());
            campo.appendChild(doc.createTextNode(f.get(o).toString()));
            elemento.appendChild(campo);
        }

    }

    public static void modificaXML(File file, String[] dati){
        //TODO
    }

    private static void rimuoviNodiVuoti(Node doc) {
        NodeList lista = doc.getChildNodes();
        for (int i = lista.getLength() - 1; i >= 0; i--) {
            Node child = lista.item(i);
            if (child.getNodeType() == Node.TEXT_NODE && child.getNodeValue().trim().isEmpty()) {
                doc.removeChild(child);
            } else if (child.getNodeType() == Node.ELEMENT_NODE) {
                rimuoviNodiVuoti(child); // Ricorsione per eliminare spazi all'interno dei tag
            }
        }
    }
}