package Model;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import java.io.*;
import java.net.Socket;

public class NewUserThread extends Thread {

    private static final Logger LOG = Logger.getLogger(NewUserThread.class);

    private Socket socket;

    private DataInputStream in;
    private DataOutputStream out;

    public void run(){

        System.out.println("RUN");

        setDaemon(true);

        try {
            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true){
            try {

                DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
                f.setValidating(false);
                DocumentBuilder builder = f.newDocumentBuilder();

                String xml = in.readUTF();
                System.out.println(xml);

                Document doc = builder.parse(new InputSource(new ByteArrayInputStream(xml.getBytes("utf-8"))));

                NodeList list = doc.getElementsByTagName("values");

                for (int i = 0; i < list.getLength(); i++){
                    Element element = (Element)list.item(i);

                    int id = Integer.parseInt(element.getElementsByTagName("id").item(0).getChildNodes().item(0).getNodeValue());

                    switch (id) {

                        case 4:

                            String name = element.getElementsByTagName("user").item(0).getChildNodes().item(0).getNodeValue();

                            User newUser = new User(name, out);

                            System.out.println("send new user" + name);

                           // Model.sendNewUserToClients(name);
                            System.out.println("add new user"+name);
                            System.out.println("user name"+ newUser.getUserName());

                            Model.addNewUser(newUser);

                            System.out.println("send user list");

                            Model.sendUserListToClient(newUser);

                            break;

                        case 5:

                            //Model.sendMessagsToClients(valueString);


                            break;

                        case 7:


                            break;

                        case 8:


                            break;
                    }

                    //element.getElementsByTagName("message").item(0).getChildNodes().item(0).getNodeValue();

                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            }
        }
    }

    public NewUserThread(Socket socket) {

        this.socket = socket;
        this.run();
    }

    /*public void informAboutNewUser(String nameOfUser) throws IOException {
        LOG.info("Try to send user.");
        out.writeUTF ("<data><command>4</command><name>"+ nameOfUser +"name></data>");
        LOG.info("User sent successfully.");
    }

    public void sendMessageToUser(String message, String nameOfUser) throws IOException {
        LOG.info("Try to send message.");
        out.writeUTF ("<data><command>5</command><user>"+ nameOfUser +"</user><message>"+ message +"</message></data>");
        LOG.info("Message sent successfully.");
    }*/
}



