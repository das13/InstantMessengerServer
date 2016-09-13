package Controller;

import Model.Model;
import View.View;

public class StartProgram {

    public static void main(String[] args) {

        View view = new View();

        Model model = new Model();

        Controller controller = new Controller(view, model);
    }
}
