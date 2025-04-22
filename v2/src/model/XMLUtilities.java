package model;
import java.io.File;

import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.time.LocalDate;
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
    
    public static <T> Elenco<T> leggiXML(File file, String contesto, Function<Element, T> parser) throws Exception {
        Elenco<T> elenco = new Elenco<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(file);
        doc.getDocumentElement().normalize();
    
        //NodeList lista = doc.getElementsByTagName(contesto);
        NodeList lista = doc.getDocumentElement().getChildNodes();

    
        for (int i = 0; i < lista.getLength(); i++) {
            Node nodo = lista.item(i);
            if (nodo.getNodeType() == Node.ELEMENT_NODE) {
                Element elemento = (Element) nodo;
                try {
                    elenco.aggiungi(parser.apply(elemento));
                } catch (Exception e) {
                    System.err.println("ERRORE nel parsing dell'elemento: " + e.getMessage());
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

        NodeList lista = doc.getDocumentElement().getChildNodes();
        String[] risultato = new String[lista.getLength()];

        for (int i = 0; i < lista.getLength(); i++) {
            Node nodo = lista.item(i);
            if (nodo.getNodeType() == Node.ELEMENT_NODE) {
                risultato[i] = nodo.getTextContent().trim();
            }
        }
        return risultato;
    }
    
    public static <T> void scriviXML(File file, Elenco<T> elencoOggetti, String rootElementName) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();
        HashMap<String, T> hashMap = elencoOggetti.getElenco();
    
        Element rootElement = doc.createElement(rootElementName);
        doc.appendChild(rootElement);
    
        for (String key : hashMap.keySet()) {
            T oggetto = hashMap.get(key);

            if (oggetto instanceof LocalDate) {
                Element dataElement = doc.createElement("data");
                dataElement.appendChild(doc.createTextNode(oggetto.toString()));
                rootElement.appendChild(dataElement);
                continue;
            }
            Element objectElement = doc.createElement(oggetto.getClass().getSimpleName());
            rootElement.appendChild(objectElement);
    
            Class<?> clazz = oggetto.getClass();
            while (clazz != null) { 
                for (Field field : clazz.getDeclaredFields()) {


                    int modifiers = field.getModifiers();
                    if (java.lang.reflect.Modifier.isStatic(modifiers) || java.lang.reflect.Modifier.isFinal(modifiers)) 
                        continue;
                    

                    field.setAccessible(true);
                    Object valoreCampo = field.get(oggetto);

                    if (valoreCampo != null) {
                        if (valoreCampo instanceof Collection) {
                            Element listaElement = doc.createElement(field.getName());
                            for (Object item : (Collection<?>) valoreCampo) {
                                Element itemElement = doc.createElement("elemento");
                                itemElement.appendChild(doc.createTextNode(item.toString()));
                                listaElement.appendChild(itemElement);
                            }
                            objectElement.appendChild(listaElement);
                        } else if (valoreCampo instanceof Elenco) {
                            Element elencoElement = doc.createElement(field.getName());
                            Elenco<?> elenco = (Elenco<?>) valoreCampo;
                            for (String subKey : elenco.getElenco().keySet()) {
                                Element subElement = doc.createElement("elemento");
                                subElement.appendChild(doc.createTextNode(elenco.getElementByKey(subKey).toString()));
                                elencoElement.appendChild(subElement);
                            }
                            objectElement.appendChild(elencoElement);
                        } else {
                            Element campoElement = doc.createElement(field.getName());
                            campoElement.appendChild(doc.createTextNode(valoreCampo.toString()));
                            objectElement.appendChild(campoElement);
                        }
                    }
                }
                clazz = clazz.getSuperclass(); // Passa alla superclasse per ottenere anche i campi ereditati
            }

        }
    
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

    public static void scriviXML(File file, String[] dati, String[] campi, String contesto) throws Exception{
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();


        Element rootElement = doc.createElement(contesto);
        doc.appendChild(rootElement);

        for(int i = 0; i < dati.length; i++){
            Element e = doc.createElement(campi[i]);
            e.appendChild(doc.createTextNode(dati[i]));
            rootElement.appendChild(e);
        }

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes"); // Formattazione con indentazione
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(file);

        transformer.transform(source, result);
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