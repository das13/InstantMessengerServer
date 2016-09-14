package Model;

import org.apache.log4j.Logger;

import java.io.DataOutputStream;


public class User {

    private static final Logger LOG = Logger.getLogger(User.class);

    private String userName;
    private DataOutputStream stream;

    public User(String userName, DataOutputStream stream) {
        this.userName = userName;
        this.stream = stream;
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
}
