package main;

import java.net.URISyntaxException;

public class Constant {
    //For color of text
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";

    //path of current working directory
    public static String CWD = null;

    static {
        try {
            CWD = Constant.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }


}


