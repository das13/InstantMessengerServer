package Model;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Model {

    private static ArrayList<NewUserThread> userList;

    public static void startServer() throws IOException {

        ServerSocket ss = new ServerSocket(3000);

        while (true) {

            Socket accept = ss.accept();

            NewUserThread thread = new NewUserThread(accept);

            userList.add(thread);
        }
    }

    public static void sendMessagsToClients(String message, String user) throws IOException {

        for (int i = 0; i < userList.size(); i++){
            userList.get(i).sendMessageToUser(message,user);
        }
    }

    public static void sendNewUserToClients(String user) throws IOException {

        for (int i = 0; i < userList.size(); i++){
            userList.get(i).informAboutNewUser(user);
        }
    }
}