package Model;

import java.io.*;
import java.net.Socket;

public class NewUserThread extends Thread {

    private String nameOfUser;
    private Socket socket;
    private DataInputStream cin;
    private DataOutputStream cout;

    public void run(){

        try {

            DataInputStream cin = new DataInputStream(socket.getInputStream());
            DataOutputStream cout = new DataOutputStream(socket.getOutputStream());

            setDaemon(true);

            String xml = cin.readUTF();











            int idOfCommand = cin.readInt();

            switch (idOfCommand) {

                case 4:

                    String userName = "";



                    break;

                case 5:


                    break;

                case 7:




                    break;

                case 8:


                    break;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public NewUserThread(Socket socket) {

        this.socket = socket;
        this.start();
    }

    public void setUserName(String nameOfUser) {

        this.nameOfUser = nameOfUser;
    }

    public void informAboutNewUser(String nameOfUser) throws IOException {
        cout.writeChars("<data><command>4</command><name>"+ nameOfUser +"name></data>");
    }

    public void sendMessageToUser(String message, String nameOfUser) throws IOException {
        cout.writeChars("<data><command>5</command><user>"+ nameOfUser +"</user><message>"+ message +"</message></data>");
    }



}



