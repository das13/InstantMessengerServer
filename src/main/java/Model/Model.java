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

    public static int idCounter = 1;

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

    public static int getIdCounter(){

        return idCounter;
    }

    public static void incIdCounter(){

        idCounter++;
    }

    public static void deleteUser(int id){

        for (int i = 0; i < userList.size(); i++){

            if (userList.get(i).getId() == id){

                userList.get(i).closeStream();

                userList.remove(i);

                LOG.info("User deleted");

                break;
            }
        }
    }

    public static void sendUserListToClient(User user){

        DataOutputStream out = user.getDataOutputStream();

        try {
            out.writeUTF(xmlGeneration.sendUserListToClient());
        } catch (IOException e) {
            LOG.error("IOException: " + e);
        }
    }

    public static void sendMessagsToClients(String message, String user) throws IOException {

        for (int i = 0; i < userList.size(); i++){
            DataOutputStream out = userList.get(i).getDataOutputStream();

            out.writeUTF(xmlGeneration.sendMessage(message,user));
        }
    }

    public static void deleteUserDromClients(String user, int id) throws IOException {

        for (int i = 0; i < userList.size(); i++){

            DataOutputStream out = userList.get(i).getDataOutputStream();

            out.writeUTF(xmlGeneration.deleteUser(user, id));
        }
    }

    public static void sendUserNameAndId(User user) throws IOException {

        DataOutputStream out = user.getDataOutputStream();

        out.writeUTF(xmlGeneration.sendUserNameAndId(user));
    }

    public static void sendNewUserToClients(String user, int id) throws IOException {

        for (int i = 0; i < userList.size(); i++){

            DataOutputStream out = userList.get(i).getDataOutputStream();

            out.writeUTF(xmlGeneration.sendNewUser(user,id));
        }
    }
}