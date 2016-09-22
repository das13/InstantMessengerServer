package Model;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class WorkWithOneUserThread extends Thread {

    private static final Logger LOG = Logger.getLogger(WorkWithOneUserThread.class);

    private final static int GET_NEW_USER = 4;
    private final static int GET_MESSAGE = 5;
    private final static int DELETE_USER = 8;

    private Socket socket;

    public WorkWithOneUserThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

        try{

            DataInputStream in = new DataInputStream(socket.getInputStream());

            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
            f.setValidating(false);
            DocumentBuilder builder = f.newDocumentBuilder();

            while(true) {

                String xml = in.readUTF();

                LOG.debug("Server get xml: " + xml);

                Document doc = builder.parse(new InputSource(new ByteArrayInputStream(xml.getBytes("utf-8"))));

                NodeList list = doc.getElementsByTagName("values");

                for (int i = 0; i < list.getLength(); i++) {
                    Element element = (Element) list.item(i);

                    int id = Integer.parseInt(element.getElementsByTagName("id").item(0).getChildNodes().item(0).getNodeValue());

                    switch (id) {

                        case GET_NEW_USER:

                            String name = element.getElementsByTagName("user").item(0).getChildNodes().item(0).getNodeValue();

                            int idOfUser = Model.getIdCounter();

                            User newUser = new User(name,socket, in, out, idOfUser);

                            Model.sendNewUserToClients(name, idOfUser);

                            Model.sendUserNameAndId(newUser);

                            Model.addNewUser(newUser);

                            Model.sendUserListToClient(newUser);

                            Model.incIdCounter();

                            LOG.info("New user added!");

                            break;

                        case GET_MESSAGE:

                            String message = element.getElementsByTagName("message").item(0).getChildNodes().item(0).getNodeValue();

                            String user = element.getElementsByTagName("user").item(0).getChildNodes().item(0).getNodeValue();

                            Model.sendMessagsToClients(message,user);

                            LOG.info("Get new message!");

                            break;

                        case DELETE_USER:

                            int id8 = Integer.parseInt(element.getElementsByTagName("userId").item(0).getChildNodes().item(0).getNodeValue());

                            System.out.println("case 8 - id - "+ id8);

                            ArrayList<User> tempList = Model.getUserList();

                            for (int i8 = 0; i < tempList.size(); i8++){

                                if (tempList.get(i8).getId() == id8){

                                    Model.deleteUserDromClients(tempList.get(i8).getUserName(),id8);
                                    break;
                                }
                            }

                            Model.deleteUser(id8);

                            socket.close();
                            in.close();
                            out.close();

                            this.stop();
                    }
                }
            }
        }catch (IOException e){
            LOG.error("IOException: "+ e);
        } catch (ParserConfigurationException e) {
            LOG.error("ParserConfigurationException: "+ e);
        } catch (SAXException e) {
            LOG.error("SAXException: "+ e);
        }
       LOG.info("New user connected!");
    }
}
