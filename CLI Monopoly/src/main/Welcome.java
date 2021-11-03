package main;

import java.io.*;

public class Welcome implements Serializable {

    public void playWelcome() throws InterruptedException {
        try {
            InputStream inputStream = getClass().getResourceAsStream("/main/resources/welcome.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                Thread.sleep(100);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
