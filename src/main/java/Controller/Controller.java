package Controller;

import Model.Model;
import View.View;
import org.apache.log4j.Logger;


import java.io.IOException;

public class Controller {

    private static final Logger LOG = Logger.getLogger(Controller.class);

    View view;
    Model model;

    public Controller(View view, Model model) {

        this.view = view;
        this.model = model;

        LOG.info("Controller>startServer");

        try {
            Model.startServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
