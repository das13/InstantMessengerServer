package Model;

import org.apache.log4j.Logger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


public class User {

    private static final Logger LOG = Logger.getLogger(User.class);

    private String userName;
    private Socket socket;
    private DataOutputStream stream;
    private DataInputStream inStream;
    private int id;

    public User(String userName, Socket socket, DataInputStream in, DataOutputStream stream, int id) {
        this.userName = userName;
        this.stream = stream;
        this.inStream = in;
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public DataOutputStream getDataOutputStream() {
        return stream;
    }

    public void setId(int id) {
        id = id;
    }

    public int getId() {
        return id;
    }

    public void closeStream(){
        try {

            inStream.close();
            stream.close();

        } catch (IOException e) {
            LOG.error("IOException: Can't close the streams. ", e);
        }
    }
}
