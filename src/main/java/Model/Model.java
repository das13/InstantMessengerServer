package Model;

import org.apache.log4j.Logger;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Properties;

public class Model {

    private static final Logger LOG = Logger.getLogger(Model.class);

    private static ArrayList<User> userList = new ArrayList<User>();

    public static ArrayList<User> getUserList(){
        return userList;
    }

    public void startServer() throws IOException {

        FileInputStream fin;
        Properties property = new Properties();

        fin = new FileInputStream("src/main/resources/config.properties");
        property.load(fin);

        int PORT  = Integer.parseInt(property.getProperty("PORT"));

        ServerSocket ss = new ServerSocket(PORT);

        while (true){
            Socket accept = ss.accept();

            WorkWithOneUserThread thread = new WorkWithOneUserThread(accept);
            thread.setDaemon(true);
            thread.start();
        }
    }

    public static void addNewUser(User user){
        userList.add(user);
    }

    public static void deleteUser(User user){
        userList.remove(user);
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
            LOG.error("IOException: " + e);
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
            System.out.println("for - "+ user);
            out.writeUTF(xml);
        }
    }
}