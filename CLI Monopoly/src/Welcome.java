import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Welcome {
    ArrayList<String> line = new ArrayList<>();

    /* load the text in welcome.txt to the arraylist */
    public void loadToList(String filename) {
        try {
            File file = new File(filename);

            Scanner scanner = new Scanner(file);
            while(scanner.hasNextLine()){
                line.add(scanner.nextLine());
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    /* play the text in the arraylist with delay */
    public void playHeading() throws InterruptedException {
        for (String lines:line){
            System.out.println(lines);
            Thread.sleep(100);
        }
    }

    public void playWelcome() throws InterruptedException {
        loadToList(".\\src\\welcome.txt");
        playHeading();
    }



}
