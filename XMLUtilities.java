import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.*;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLUtilities {
    
    public static String[][] leggiXML(File file, String[] args)throws Exception{

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

    public static void scriviXML(File file, String[] dati){
        //TODO
    }

    public static void modificaXML(File file, String[] dati){
        //TODO
    }
}