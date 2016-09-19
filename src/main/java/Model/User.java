package Model;

import org.apache.log4j.Logger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;


public class User {

    private static final Logger LOG = Logger.getLogger(User.class);

    private String userName;
    private DataOutputStream stream;
    private DataInputStream inStream;
    private int id;

    public User(String userName, DataInputStream in, DataOutputStream stream, int id) {
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

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setDataOutputStream(DataOutputStream socket) {
        this.stream = socket;
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
            e.printStackTrace();
        }
    }
}
