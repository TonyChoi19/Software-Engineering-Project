import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;


class ControllerTest {

    private Controller controller = new Controller();
    private Game game = new Game(6);


    /* make directory if it doesn't exist */
    @BeforeAll
    public static void createDir(){
        String path = Constant.CWD+"game records";
        File directory = new File(path);

        if (!directory.exists() && !directory.isDirectory())
            if (directory.mkdir())
                System.out.println("Directory for game record is created.");
    }




    /* Testing the record name is unique or not */
    @Test
    void isUniqueNameRecords() {
        //It is unique when the dir is empty
        assertTrue(controller.isUniqueNameRecords("test1"));

        //It is not unique when there is a file with the same name
        controller.saveGame("test1",game, game.getPlayersList().get(0));
        assertFalse(controller.isUniqueNameRecords("test1"));
    }

    /* Count the number of saved records */
    @Test
    void countRecords() {
        //return 0 if no records
        assertEquals(0, controller.countRecords());

        //return 1 if there is one record
        controller.saveGame("test1",game, game.getPlayersList().get(0));
        assertEquals(1, controller.countRecords());

        //return 2 if there are two records
        controller.saveGame("test2",game, game.getPlayersList().get(0));
        assertEquals(2, controller.countRecords());

        //return 3 if there are three records
        controller.saveGame("test3",game, game.getPlayersList().get(0));
        assertEquals(3, controller.countRecords());
    }

    /* Delete a record by option number */
    @Test
    void deleteRecords() {
        //add a record first and make sure it existed
        controller.saveGame("test1",game, game.getPlayersList().get(0));
        assertEquals(controller.findRecords(1), "test1");
        assertEquals(1, controller.countRecords());

        //Deleted the record and cannot find it anymore
        controller.deleteRecords(1);
        assertEquals(controller.findRecords(1), "");
        assertEquals(0, controller.countRecords());

        //To be noted that the number "1" here means the first option showed in console
        //In here, there is only one record, so that "1" represents test1.ser
    }

    /* Delete a record by record name */
    @Test
    void testDeleteRecords() {
        //add a record first and make sure it existed
        controller.saveGame("test1",game, game.getPlayersList().get(0));
        assertEquals(controller.findRecords(1), "test1");
        assertEquals(1, controller.countRecords());

        //Deleted the record and cannot find it anymore
        controller.deleteRecords("test1");
        assertEquals(controller.findRecords(1), "");
        assertEquals(0, controller.countRecords());

        //To be noted that the number "1" here means the first option showed in console
        //In here, there is only one record, so that "1" represents test1.ser
    }

    /* Find records' name with option number */
    @Test
    void findRecords() {
        //adding a few records
        controller.saveGame("test1",game, game.getPlayersList().get(0));
        controller.saveGame("test2",game, game.getPlayersList().get(0));
        controller.saveGame("test3",game, game.getPlayersList().get(0));
        controller.saveGame("test4",game, game.getPlayersList().get(0));

        //check the corresponding name
        assertEquals(controller.findRecords(1), "test1");
        assertEquals(controller.findRecords(2), "test2");
        assertEquals(controller.findRecords(3), "test3");
        assertEquals(controller.findRecords(4), "test4");

        //if the option is not existed, return ""
        assertEquals(controller.findRecords(5), "");
    }

    /* Check if it is numeric */
    @Test
    void isNumeric() {
        assertTrue(controller.isNumeric("0"));
        assertTrue(controller.isNumeric("999"));

        //For the input, we only accept only numeric, so negative number will be invalid
        assertFalse(controller.isNumeric("-999"));
        assertFalse(controller.isNumeric("abc"));
        assertFalse(controller.isNumeric("~!@#$%^&*()_+"));
        assertFalse(controller.isNumeric("/23^2121"));
    }

    /* Distinguish if the input is in the range of the records existed */
    @Test
    void isInRangeOfRecords() {
        //If there is no record,  no option will be provided for players. Nothing is valid
        assertFalse(controller.isInRangeOfRecords("-1"));
        assertFalse(controller.isInRangeOfRecords("0"));
        assertFalse(controller.isInRangeOfRecords("1"));
        assertFalse(controller.isInRangeOfRecords("2"));
        assertFalse(controller.isInRangeOfRecords("3"));
        assertFalse(controller.isInRangeOfRecords("4"));
        assertFalse(controller.isInRangeOfRecords("5"));


        //adding one record
        controller.saveGame("test1",game, game.getPlayersList().get(0));
        assertTrue(controller.isInRangeOfRecords("1"));
            //There is no option 2 because we got 1 record only
        assertFalse(controller.isInRangeOfRecords("2"));

        //adding second record
        controller.saveGame("test2",game, game.getPlayersList().get(0));
        assertTrue(controller.isInRangeOfRecords("2"));
        //There is no option 3 because we got 2 records only
        assertFalse(controller.isInRangeOfRecords("3"));
    }

    /* Game saving */
    @Test
    void saveGame() {
        //no records at the beginning
        assertEquals(0, controller.countRecords());

        //adding a few records
        controller.saveGame("test1",game, game.getPlayersList().get(0));
        assertEquals(1, controller.countRecords());

        controller.saveGame("test2",game, game.getPlayersList().get(0));
        assertEquals(2, controller.countRecords());

        controller.saveGame("test3",game, game.getPlayersList().get(0));
        assertEquals(3, controller.countRecords());

        controller.saveGame("test4",game, game.getPlayersList().get(0));
        assertEquals(4, controller.countRecords());

        controller.saveGame("test5",game, game.getPlayersList().get(0));
        assertEquals(5, controller.countRecords());
    }

    /* Game loading -- returning the game record which is about to load */
    @Test
    void loadGame() {
        //Load the record which not exists
        assertNull(controller.loadGame("test1"));


        //Load the record which exists
        controller.saveGame("test1", game, game.getPlayersList().get(1));
        GameRecord gameRecord = controller.loadGame("test1");
        assertEquals( "test1", gameRecord.getName());
        assertEquals("Player 2" , gameRecord.getPlayerTurn().getName());

        //To be noted that this method is about unserializing of the file, it is hard to directly compare the return result (different references).
        //How it checks here is to compare the info inside.
    }

    //Empty the the directory after each test
    @AfterEach
    public void emptyDir(){
        File recordsDir = new File(Constant.CWD+"game records/");
        File[] directoryListing = recordsDir.listFiles();
        if (directoryListing!=null) {
            for (File file : directoryListing) {
                if(file.delete())
                    System.out.println();
            }
        }
        System.out.println("Cleaned up the directory for next task.");
    }

}