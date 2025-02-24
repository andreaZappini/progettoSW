import java.io.File;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.*;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Main {
    public static void main(String[] args) {

        File file = new File("configuratore.xml");
        System.out.println("Benvenuto configuratore!");
        System.out.println("Inserisci username e password per accedere");
        try(Scanner in = new Scanner(System.in)){
            System.out.println("username:");
            String username = in.next();
            System.out.println("password:");
            String password = in.next();
            leggiXML(file, username, password);
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    private static boolean leggiXML(File file, String username, String password) throws Exception{
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(file);

        NodeList lista = doc.getElementsByTagName("persona");
        for(int i = 0; i < lista.getLength(); i++){
            Node p = lista.item(i);
            if(p.getNodeType() == Node.ELEMENT_NODE){
                Element persona = (Element) p;
                System.out.println("Nome: " + persona.getElementsByTagName("nome").item(0).getTextContent());
                System.out.println("Cognome: " + persona.getElementsByTagName("cognome").item(0).getTextContent());
                System.out.println("Eta: " + persona.getElementsByTagName("eta").item(0).getTextContent());
            }

        }
    }
}


