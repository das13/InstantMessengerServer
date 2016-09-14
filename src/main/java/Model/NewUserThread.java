package Model;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
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

                Document doc = builder.parse(new InputSource(new ByteArrayInputStream(xml.getBytes("utf-8"))));

                NodeList list = doc.getElementsByTagName("value");

                Node data = doc.getChildNodes().item(0);
                Node command = data.getChildNodes().item(0);
                Node value = data.getChildNodes().item(1);

                int idOfCommand = Integer.parseInt(command.getTextContent());

                String valueString = value.getTextContent();

                /*

                idOfCommand:

                1 - Get new user.
                2 - Get new message.
                3 -

                */

                switch (idOfCommand) {

                    case 4:

                        User newUser = new User(valueString, out);

                        Model.sendNewUserToClients(valueString);

                        Model.addNewUser(newUser);

                        Model.sendUserListToClient(newUser);

                        break;

                    case 5:

                        Model.sendMessagsToClients(valueString);


                        break;

                    case 7:




                        break;

                    case 8:


                        break;
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



