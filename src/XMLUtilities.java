import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.function.Function;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

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
        return risultato;
    }

    public static <T> void scriviXML( File file, Elenco<T> elencoOggetti,String rootElementName) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();
        HashMap<String, T> hashMap = elencoOggetti.getElenco();

        // Creazione del nodo radice
        Element rootElement = doc.createElement(rootElementName);
        System.out.println("rootElement: " + rootElement);
        doc.appendChild(rootElement);

        // Iteriamo sugli oggetti nella HashMap
        for (String key : hashMap.keySet()) {
            T oggetto = hashMap.get(key);
            Element objectElement = doc.createElement(oggetto.getClass().getSimpleName().toString());
            rootElement.appendChild(objectElement);

            System.out.println(oggetto.getClass());
            System.out.println(oggetto.getClass().getDeclaredFields());

            // Analizziamo i campi della classe con Reflection
            for (Field field : oggetto.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                System.out.println("field: " + field);
                Object valoreCampo = field.get(oggetto);
                System.out.println("oggetto: " + valoreCampo);

                if (valoreCampo != null) {
                    // Se il campo è un `ArrayList<T>` (Collection)
                    if (valoreCampo instanceof Collection) {
                        Element listaElement = doc.createElement(field.getName());
                        for (Object item : (Collection<?>) valoreCampo) {
                            Element itemElement = doc.createElement(field.getName().substring(0, field.getName().length() - 1));
                            itemElement.appendChild(doc.createTextNode(item.toString()));
                            listaElement.appendChild(itemElement);
                        }
                        objectElement.appendChild(listaElement);
                    }
                    // Se il campo è un `Elenco<T>` (gestito separatamente)
                    else if (valoreCampo instanceof Elenco) {
                        Element elencoElement = doc.createElement(field.getName());
                        Elenco<?> elenco = (Elenco<?>) valoreCampo;

                        for (String subKey : elenco.getElenco().keySet()) {
                            Object subElemento = elenco.getElementByKey(subKey);
                            System.out.println("subElemento: " + subElemento);
                            Element subElement = doc.createElement(field.getName().substring(0, field.getName().length() - 1));
                            subElement.appendChild(doc.createTextNode(subElemento.toString()));
                            elencoElement.appendChild(subElement);
                        }
                        objectElement.appendChild(elencoElement);
                    }
                    // Se è un campo semplice
                    else {
                        Element campoElement = doc.createElement(field.getName());
                        campoElement.appendChild(doc.createTextNode(valoreCampo.toString()));
                        objectElement.appendChild(campoElement);
                    }
                }
            }
        }

        // Scrittura del file XML
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(outputStream);
            transformer.transform(source, result);
        }

        System.out.println("File XML scritto con successo: " + file.getName());
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