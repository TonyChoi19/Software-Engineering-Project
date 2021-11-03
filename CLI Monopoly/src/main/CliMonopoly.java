package main;

import java.net.URISyntaxException;

public class CliMonopoly {

    public static void main(String[] args) throws InterruptedException, URISyntaxException {
        Controller controller = new Controller();
        controller.start();
    }
}
