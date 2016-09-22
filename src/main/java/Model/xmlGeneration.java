package Model;

import org.apache.log4j.Logger;
import org.apache.log4j.varia.StringMatchFilter;

import java.util.ArrayList;

public class xmlGeneration {

    private static final Logger LOG = Logger.getLogger(xmlGeneration.class);

    private final static int SEND_NEW_USER = 4;
    private final static int SEND_MESSAGE = 5;
    private final static int SEND_USER_LIST = 7;
    private final static int DELETE_USER = 9;
    private final static int SEND_USER_ID_USER_NAME = 11;

    public static String sendNewUser(String name, int id){

        LOG.debug("Generating xml for sending new user");

        return  "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
                "<data>" +
                "<values>" +
                "<id>"+ SEND_NEW_USER +"</id>" +
                "<message></message>" +
                "<user>"+ name +"</user>" +
                "<userId>" + id + "</userId>" +
                "</values>" +
                "</data>";
    }

    public static String sendMessage(String message, String user){

        LOG.debug("Generating xml for sending new user");

        return  "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
                "<data>" +
                "<values>" +
                "<id>"+ SEND_MESSAGE +"</id>" +
                "<message>"+ user + ": "+ message +"</message>" +
                "<user></user >"+
                "</values>" +
                "</data>";
    }

    public static String deleteUser(String name, int id){

        LOG.debug("Generating xml for sending new user");

        return "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
                "<data>" +
                "<values>" +
                "<id>"+ DELETE_USER +"</id>" +
                "<message></message>" +
                "<user>"+ name +"</user>" +
                "<userId>" + id + "</userId>" +
                "</values>" +
                "</data>";
    }

    public static String sendUserNameAndId(User user){

        LOG.debug("Generating xml for sending new user");

        return "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
                "<data>" +
                "<values>" +
                "<id>"+ SEND_USER_ID_USER_NAME +"</id>" +
                "<message></message>" +
                "<user>" + user.getUserName() + "</user >"+
                "<userId>" + user.getId() + "</userId>" +
                "</values>" +
                "</data>";
    }

    public static String sendUserListToClient(){

        LOG.debug("Generating xml for sending new user");

        ArrayList<User> list = Model.getUserList();

        String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?><data>";

        for (int i = 0; i < list.size(); i++){

            xml = xml + "<values><id>"+ SEND_USER_LIST +"</id><message></message><user>" + list.get(i).getUserName() + "</user><userId>" + list.get(i).getId() + "</userId></values>";
        }
        xml = xml + "</data>";

        return xml;
    }
}
