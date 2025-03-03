import java.io.File;
import java.lang.reflect.Field;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.*;

public class XMLUtilities {
    
    public static <T> Elenco<T> leggiXML(File file)throws Exception{

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(file);

        //args[0] contiene sempre il nome del tag principale del file xml
        NodeList lista = doc.getElementsByTagName(args[0]);
        String[][] dati = new String[lista.getLength()][args.length - 1];

        for(int i = 0; i < lista.getLength(); i++){
            Node p = lista.item(i);
            if(p.getNodeType() == Node.ELEMENT_NODE){
                Element persona = (Element) p;
                for(int j = 1; j < args.length; j++){
                    String x = persona.getElementsByTagName(args[j]).item(0).getTextContent();
                    dati[i][j - 1] = x;
                }
            }
        }
                return dati;
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