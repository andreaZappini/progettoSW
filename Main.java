import java.io.File;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.*;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Main {
    private static boolean abilitazioneModificaCorpoDati = true;
    public static void main(String[] args) {

        ElencoUtenti elencoUtenti = new ElencoUtenti();

        System.out.println("Benvenuto configuratore!");
        System.out.println("Inserisci username e password per accedere");
        if(abilitazioneModificaCorpoDati){
            File file = new File("configuratore1.xml");
            login(file, elencoUtenti);
        }

    }

    private static void login(File file, ElencoUtenti elencoUtenti) {
        try(Scanner in = new Scanner(System.in)){
            System.out.println("username:");
            String username = in.next();
            System.out.println("password:");
            String password = in.next();
            Credenziale credenziali = new Credenziale(username, password);
            Configuratore configuratore = new Configuratore(credenziali);
            elencoUtenti.aggiungiUtente(configuratore);
            if(leggiXML(file, username, password)){
                System.out.println("Accesso consentito");
                System.out.println("necessiario modificare credenziali");
                System.out.println("Inserisci nuova password:");
                String nuovaPassword = in.next();
                elencoUtenti.ge
                
            }else{
                System.out.println("Accesso negato");
                login(file, elencoUtenti);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private static boolean leggiXML(File file, String username, String password) throws Exception{
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(file);

        NodeList lista = doc.getElementsByTagName("configuratore");
        String u = null;
        String psw = null;

        for(int i = 0; i < lista.getLength(); i++){
            Node p = lista.item(i);
            if(p.getNodeType() == Node.ELEMENT_NODE){
                Element persona = (Element) p;
                u = persona.getElementsByTagName("username").item(0).getTextContent();
                psw = persona.getElementsByTagName("password").item(0).getTextContent();
                System.out.println("username: " + u);
                System.out.println("password: " + psw);
            }
        }
        if(u.equals(username) && psw.equals(password)){
            System.out.println("Accesso consentito");
            //effettua modifiche corpo dati
             
            return true;
        }
        return false;
    }
}


