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
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Model {

    private static final Logger LOG = Logger.getLogger(Model.class);

    private static ArrayList<User> userList = new ArrayList<User>();

    public static void startServer() throws IOException {

        ServerSocket ss = new ServerSocket(4545);

        while (true) {

            final Socket accept = ss.accept();

            new Thread(() -> {

                try{

                    DataOutputStream out = new DataOutputStream(accept.getOutputStream());
                    DataInputStream in = new DataInputStream(accept.getInputStream());

                    DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
                    f.setValidating(false);
                    DocumentBuilder builder = f.newDocumentBuilder();

                    while(true) {

                        String xml = in.readUTF();

                        LOG.info("Server get xml: " + xml);

                        Document doc = builder.parse(new InputSource(new ByteArrayInputStream(xml.getBytes("utf-8"))));

                        NodeList list = doc.getElementsByTagName("values");

                        for (int i = 0; i < list.getLength(); i++) {
                            Element element = (Element) list.item(i);

                            int id = Integer.parseInt(element.getElementsByTagName("id").item(0).getChildNodes().item(0).getNodeValue());

                            switch (id) {

                                case 4:

                                    String name = element.getElementsByTagName("user").item(0).getChildNodes().item(0).getNodeValue();

                                    int idOfUser = userList.size();

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

                                    deleteUserDromClients(userList.get(index).getUserName(),index);

                                    userList.get(index).closeStream();

                                    userList.remove(index);
                            }
                        }
                    }

                }catch (IOException e){
                    e.printStackTrace();
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                }
            }).start();

            LOG.info("New user connected!");
        }
    }

    public static void addNewUser(User user){
        userList.add(user);
    }

    public static void sendUserListToClient(User user){

        System.out.println("send user list to client");

        DataOutputStream out = user.getDataOutputStream();

        String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?><data>";

        for (int i = 0; i < userList.size(); i++){

            xml = xml + "<values><id>"+ 7 +"</id><message></message><user>" + userList.get(i).getUserName() + "</user><userId>" + userList.get(i).getId() + "</userId></values>";
        }
            xml = xml + "</data>";

        System.out.println("list size - "+userList.size());

        System.out.println("XML send" + xml);

        try {
            out.writeUTF(xml);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sendMessagsToClients(String message, String user) throws IOException {

        for (int i = 0; i < userList.size(); i++){
            DataOutputStream out = userList.get(i).getDataOutputStream();
            String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
                    "<data>" +
                    "<values>" +
                    "<id>"+ 5 +"</id>" +
                    "<message>"+user+ ": "+ message +"</message>" +
                    "<user></user >"+
                    "</values>" +
                    "</data>";
            System.out.println("Send xml");
            out.writeUTF(xml);
        }
    }

    public static void deleteUserDromClients(String user, int id) throws IOException {

        System.out.println("del new user (update) ");

        for (int i = 0; i < userList.size(); i++){
            DataOutputStream out = userList.get(i).getDataOutputStream();
            String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
                    "<data>" +
                    "<values>" +
                    "<id>"+ 9 +"</id>" +
                    "<message></message>" +
                    "<user>"+ user +"</user>" +
                    "<userId>" + id + "</userId>" +
                    "</values>" +
                    "</data>";
            out.writeUTF(xml);
        }
    }

    public static void sendNewUserToClients(String user, int id) throws IOException {

        System.out.println("Send new user (update) ");

        for (int i = 0; i < userList.size(); i++){
            DataOutputStream out = userList.get(i).getDataOutputStream();
            String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
                         "<data>" +
                            "<values>" +
                                "<id>"+ 4 +"</id>" +
                                "<message></message>" +
                                "<user>"+ user +"</user>" +
                                "<userId>" + id + "</userId>" +
                            "</values>" +
                         "</data>";
            out.writeUTF(xml);
        }
    }
}