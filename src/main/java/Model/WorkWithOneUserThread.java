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

public class WorkWithOneUserThread extends Thread {

    private static final Logger LOG = Logger.getLogger(WorkWithOneUserThread.class);

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

                //LOG.info("Server get xml: " + xml);

                Document doc = builder.parse(new InputSource(new ByteArrayInputStream(xml.getBytes("utf-8"))));

                NodeList list = doc.getElementsByTagName("values");

                for (int i = 0; i < list.getLength(); i++) {
                    Element element = (Element) list.item(i);

                    int id = Integer.parseInt(element.getElementsByTagName("id").item(0).getChildNodes().item(0).getNodeValue());

                    switch (id) {

                        case 4:

                            String name = element.getElementsByTagName("user").item(0).getChildNodes().item(0).getNodeValue();

                            int idOfUser = Model.getUserList().size();

                            User newUser = new User(name, in, out, idOfUser);

                            Model.sendNewUserToClients(name, idOfUser);

                            Model.addNewUser(newUser);

                            Model.sendUserListToClient(newUser);

                            break;

                        case 5:

                            String message = element.getElementsByTagName("message").item(0).getChildNodes().item(0).getNodeValue();

                            String user = element.getElementsByTagName("user").item(0).getChildNodes().item(0).getNodeValue();

                            Model.sendMessagsToClients(message,user);

                            break;

                        case 8:

                            int index = Integer.parseInt(element.getElementsByTagName("userId").item(0).getChildNodes().item(0).getNodeValue());

                            Model.deleteUserDromClients(Model.getUserList().get(index).getUserName(),index);

                            Model.deleteUser(Model.getUserList().get(index));

                            //Model.getUserList().get(index).closeStream();

                            in.close();
                            out.close();
                            socket.close();
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

       // LOG.info("New user connected!");
    }

}
