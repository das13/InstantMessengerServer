package Model;

import org.apache.log4j.Logger;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Model {

    private static final Logger LOG = Logger.getLogger(Model.class);

    private static ArrayList<User> userList;

    public static void startServer() throws IOException {

        ServerSocket ss = new ServerSocket(4545);

        while (true) {

            Socket accept = ss.accept();

            NewUserThread thread = new NewUserThread(accept);

            Thread th = new Thread(thread);

            th.start();

            LOG.info("New user connected!");
        }
    }

    public static void addNewUser(User user){
        userList.add(user);
    }

    public static void sendUserListToClient(User user){

        DataOutputStream out = user.getDataOutputStream();

        String xml = "?xml version=\"1.0\" encoding=\"utf-8\"?><data><command>7</command>";

        for (int i = 0; i < userList.size(); i++){

            xml = xml + "<value>" + userList.get(i).getUserName() + "</value>";
        }

        try {
            out.writeUTF(xml);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sendMessagsToClients(String message) throws IOException {

        for (int i = 0; i < userList.size(); i++){
            DataOutputStream out = userList.get(i).getDataOutputStream();
            String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?><data><command>5</command><value>"+ message +"</value></data>";
            out.writeUTF(xml);
        }
    }

    public static void sendNewUserToClients(String user) throws IOException {

        for (int i = 0; i < userList.size(); i++){
            DataOutputStream out = userList.get(i).getDataOutputStream();
            String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?><data><command>4</command><value>"+ user +"</value></data>";
            out.writeUTF(xml);
        }
    }
}