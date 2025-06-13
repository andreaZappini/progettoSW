package model;
import java.io.File;

import java.io.FileOutputStream;
import java.lang.reflect.Modifier;
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
        // Crea il documento XML
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();
    
        // Elemento radice
        Element rootElement = doc.createElement(rootElementName);
        doc.appendChild(rootElement);
    
        // Cicla sugli oggetti nell’elenco
        for (String key : elencoOggetti.getElenco().keySet()) {
            T oggetto = elencoOggetti.getElementByKey(key);
    
            // Crea un elemento per l’oggetto
            Element objectElement = doc.createElement(oggetto.getClass().getSimpleName());
    
            if (oggetto instanceof LocalDate) {
                objectElement.setTextContent(((LocalDate) oggetto).toString());
            } else {
                scriviOggettoXML(oggetto, doc, objectElement);
            }
    
            rootElement.appendChild(objectElement);
        }
    
        // Salva su file
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

    private static void scriviOggettoXML(Object oggetto, Document doc, Element parentElement) throws IllegalAccessException {
        if (oggetto == null) return;
    
        Class<?> clazz = oggetto.getClass();
    
        while (clazz != null) {
            for (Field field : clazz.getDeclaredFields()) {
                int modifiers = field.getModifiers();
                if (Modifier.isStatic(modifiers) || Modifier.isFinal(modifiers)) continue;
    
                field.setAccessible(true);
                Object valoreCampo = field.get(oggetto);
                if (valoreCampo == null) continue;
    
                Element campoElement = doc.createElement(field.getName());
    
                // 🔹 Caso 1: ArrayList (es. giorniDisponibili)
                if (valoreCampo instanceof Collection<?>) {
                    for (Object item : (Collection<?>) valoreCampo) {
                        Element itemElement = doc.createElement("elemento");
                        itemElement.appendChild(doc.createTextNode(item.toString()));
                        campoElement.appendChild(itemElement);
                    }
    
                // 🔹 Caso 2: Elenco<T>
                } else if (valoreCampo instanceof Elenco<?>) {
                    Elenco<?> elenco = (Elenco<?>) valoreCampo;
                    for (String key : elenco.getElenco().keySet()) {
                        Element itemElement = doc.createElement("elemento");
                        itemElement.appendChild(doc.createTextNode(key)); // Scrive solo la chiave
                        campoElement.appendChild(itemElement);
                    }
                }else if (valoreCampo instanceof LocalDate) {
                    campoElement.appendChild(doc.createTextNode(((LocalDate) valoreCampo).toString()));
                // 🔹 Caso 3: campo semplice
                } else {
                    campoElement.appendChild(doc.createTextNode(valoreCampo.toString()));
                }
    
                parentElement.appendChild(campoElement);
            }
    
            clazz = clazz.getSuperclass();
        }
    }
    
    

    public static void scriviXML(File file, String[] dati, String[] campi, String contesto) throws Exception{
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();


        Element rootElement = doc.createElement(contesto);
        doc.appendChild(rootElement);

        for(String s : campi)
            System.out.println("campo -> " + s);

        for(int i = 0; i < dati.length; i++){
            System.out.println("Creo elemento con nome: [" + campi[i] + "]");

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