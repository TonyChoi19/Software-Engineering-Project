package main;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;


public class ControllerTest {

    //init
    private Controller controller = new Controller();
    private Game game = new Game(6);

    /* To make directory if it doesn't exist */
    @BeforeAll
    public static void createDir(){
        String path = Constant.CWD+"game records";
        File directory = new File(path);

        if (!directory.exists() && !directory.isDirectory())
            if (directory.mkdir())
                System.out.println("Directory for game record is created.");
    }


    /* Empty the directory before all the tests */
    @BeforeAll
    public static void emptyDirBeforeAll(){
        File recordsDir = new File(Constant.CWD+"game records/");
        File[] directoryListing = recordsDir.listFiles();
        if (directoryListing!=null) {
            for (File file : directoryListing) {
                if(file.delete())
                    System.out.println();
            }
        }
        System.out.println("Cleaned up the directory for testings.");
    }

    /* To test the record name is unique or not */
    @Test
    public void isUniqueNameRecords() {
        //It is unique when the dir is empty
        assertTrue(controller.isUniqueNameRecords("test1"));

        //It is not unique when there is a file with the same name
        controller.saveGame("test1",game, game.getPlayersList().get(0));
        assertFalse(controller.isUniqueNameRecords("test1"));
    }

    /* To test whether countRecords() can return the correct number of saved records */
    @Test
    public void countRecords() {
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

    /*  To test whether deleteRecords() can delete a record by option number */
    @Test
    public void deleteRecords() {
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

    /*  To test whether deleteRecords() can delete a record by record name */
    @Test
    public void DeleteRecords2() {
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

    /*  To test whether findRecords() can find records' name with option number */
    @Test
    public void findRecords() {
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

    /*  To test whether isNumeric() can check if it is numeric */
    @Test
    public void isNumeric() {
        assertTrue(controller.isNumeric("0"));
        assertTrue(controller.isNumeric("999"));

        //For the input, we only accept numeric, so negative number will be invalid
        assertFalse(controller.isNumeric("-999"));
        assertFalse(controller.isNumeric("abc"));
        assertFalse(controller.isNumeric("~!@#$%^&*()_+"));
        assertFalse(controller.isNumeric("/23^2121"));
    }

    /* To distinguish if the input is in the range of the records existed */
    @Test
    public void isInRangeOfRecords() {
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

    /* Game saving test*/
    @Test
    public void saveGame() {
        //no records at the beginning --> 0 record
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

    /* Game loading -- test the return of the game record which is about to load */
    @Test
    public void loadGame() {
        //Load the record which not exists
        assertNull(controller.loadGame("test1"));

        //Load the record which exists
        controller.saveGame("test1", game, game.getPlayersList().get(1));
        GameRecord gameRecord = controller.loadGame("test1");
        assertEquals( "test1", gameRecord.getName());
        assertEquals("Player 2" , gameRecord.getPlayerTurn().getName());

        //To be noted that this method is about un-serializing of the file, it is hard to directly compare the return result (different references).
        //How it checks here is to compare the info inside.
    }

    /* To check the display of a new game */
    @Test
    public void runGame1() {
        //for record the console output
        final ByteArrayOutputStream consoleOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(consoleOutput));

        //To check the display of game option menu in a new game
        controller.runGame(game, null, 1);
        String expected = "\n" +
                "\u001B[33m####\t\tRound 1\t Player 1's turn\tPosition:Go(1)\t Balance(HKD):1500\t\t#### \u001B[0m\n" +
                "\n" +
                "***\t\tGAME OPTION\t\t***\n" +
                "1. \t Throw the dice\n" +
                "2. \t Check status\n" +
                "3. \t Print Board\n" +
                "4. \t Save\n" +
                "5. \t Back to main menu\n" +
                "Please enter the number to choose\n";
        expected = expected.replaceAll("\n", "").replaceAll("\t", "");
        String actual = consoleOutput.toString()
                .replaceAll("\n", "")
                .replaceAll("\t", "")
                .replaceAll("\r","");
        assertEquals(expected, actual);
    }

    /* To check if the player can pay to leave the jail */
    @Test
    public void runGame2() {
        ByteArrayOutputStream consoleOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(consoleOutput));

        /* CASE: pay to leave successfully */
        //set player1 to jail
        game.getPlayersList().get(0).setInJail(true);
        game.getPlayersList().get(0).setInJailCount(1);
        game.getPlayersList().get(0).setPos(6);

        //say yes to pay the fee
        controller.runGame(game, null, 2);
        //deducted $150 as fee to leave the jail
        assertEquals(1500-150, game.getPlayersList().get(0).getMoney());
        //not in jail anymore
        assertFalse(game.getPlayersList().get(0).isInJail());
        //check display
        String expected = "\n" +
                "\u001B[33m####\t\tRound 1\t Player 1's turn\tPosition:Jail/Just Visiting(6)\t Balance(HKD):1500\t\t#### \u001B[0m\n" +
                "Do you want to pay a fine of HKD 150 to get free? (Y/N)\n" +
                "Fine paid successfully. HKD150 is deducted from your account.\n" +
                "Your balance: HKD1350\n" +
                "\n" +
                "Fine paid, you are now free to go.\n" +
                "\n" +
                "***\t\tGAME OPTION\t\t***\n" +
                "1. \t Throw the dice\n" +
                "2. \t Check status\n" +
                "3. \t Print Board\n" +
                "4. \t Save\n" +
                "5. \t Back to main menu\n" +
                "Please enter the number to choose\n";
        expected = expected.replaceAll("\n", "").replaceAll("\t", "");
        String actual = consoleOutput.toString()
                .replaceAll("\n", "")
                .replaceAll("\t", "")
                .replaceAll("\r","");
        assertEquals(expected, actual);


        /* CASE: not enough money to pay and leave */
        consoleOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(consoleOutput));
        //set player1 to jail & set less money
        game.getPlayersList().get(0).setInJail(true);
        game.getPlayersList().get(0).setInJailCount(1);
        game.getPlayersList().get(0).setPos(6);
        game.getPlayersList().get(0).setMoney(10);
        //say yes to pay the fee
        controller.runGame(game, null, 2);
        //not in jail anymore
        assertTrue(game.getPlayersList().get(0).isInJail());
        //check display
        expected = "\n" +
                "\u001B[33m####\t\tRound 1\t Player 1's turn\tPosition:Jail/Just Visiting(6)\t Balance(HKD):10\t\t#### \u001B[0m\n" +
                "Do you want to pay a fine of HKD 150 to get free? (Y/N)\n" +
                "Sorry, you don't have enough money to pay\n" +
                "\n" +
                "***\t\tGAME OPTION\t\t***\n" +
                "1. \t Throw the dice\n" +
                "2. \t Check status\n" +
                "3. \t Print Board\n" +
                "4. \t Save\n" +
                "5. \t Back to main menu\n" +
                "Please enter the number to choose\n";
        expected = expected.replaceAll("\n", "").replaceAll("\t", "");
        actual = consoleOutput.toString()
                .replaceAll("\n", "")
                .replaceAll("\t", "")
                .replaceAll("\r","");
        assertEquals(expected, actual);
    }

    /* To check if the messages show correctly when player choose not to pay the fee and throw dice */
    @Test
    public void runGame3456() {
        ByteArrayOutputStream consoleOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(consoleOutput));

        /* CASE: player will throw a double and leave the jail */
        //set it is a double
        //(here I didn't set player's position, just the status)
        controller.runGame(game, null, 3);
        //check display
        String expected = "\n" +
                "\u001B[33m####\t\tRound 1\t Player 1's turn\tPosition:Go(1)\t Balance(HKD):1500\t\t#### \u001B[0m\n" +
                "\n" +
                "***\t\tGAME OPTION\t\t***\n" +
                "1. \t Throw the dice\n" +
                "2. \t Check status\n" +
                "3. \t Print Board\n" +
                "4. \t Save\n" +
                "5. \t Back to main menu\n" +
                "Please enter the number to choose\n" +
                "The value of die 1 is :1\n" +
                "The value of die 2 is :1\n" +
                "\n" +
                "Congrats! It is a double. You are now free to go.\n" +
                "\n" +
                "You moved to the Position 3.\tProperty:Wan Chai\tPrice(HKD):700\tRent(HKD):65\tOwnedBy:None\u001B[32m (Available)\u001B[0m\n";
        expected = expected.replaceAll("\n", "").replaceAll("\t", "");
        String actual = consoleOutput.toString()
                .replaceAll("\n", "")
                .replaceAll("\t", "")
                .replaceAll("\r","");
        assertEquals(expected, actual);

        /* CASE: player1 will throw not a double and does no move */
        consoleOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(consoleOutput));
        game = new Game(6);
        //set play is in jail
        //set it is not a double
        controller.runGame(game, null, 4);
        expected = "\n" +
                "\u001B[33m####\t\tRound 1\t Player 1's turn\tPosition:Go(1)\t Balance(HKD):1500\t\t#### \u001B[0m\n" +
                "\n" +
                "***\t\tGAME OPTION\t\t***\n" +
                "1. \t Throw the dice\n" +
                "2. \t Check status\n" +
                "3. \t Print Board\n" +
                "4. \t Save\n" +
                "5. \t Back to main menu\n" +
                "Please enter the number to choose\n" +
                "The value of die 1 is :1\n" +
                "The value of die 2 is :2\n" +
                "\n" +
                "You moved to the Position 1.\tGo\n";
        expected = expected.replaceAll("\n", "").replaceAll("\t", "");
        actual = consoleOutput.toString()
                .replaceAll("\n", "")
                .replaceAll("\t", "")
                .replaceAll("\r","");
        assertEquals(expected, actual);

        /* CASE: player1 throws dice and must pay at the third round in jail to go */
        consoleOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(consoleOutput));
        game = new Game(6);
        //set player is in jail for 3 rounds already
        //set it is not a double
        controller.runGame(game, null, 5);
        expected = "\n" +
                "\u001B[33m####\t\tRound 1\t Player 1's turn\tPosition:Go(1)\t Balance(HKD):1500\t\t#### \u001B[0m\n" +
                "\n" +
                "***\t\tGAME OPTION\t\t***\n" +
                "1. \t Throw the dice\n" +
                "2. \t Check status\n" +
                "3. \t Print Board\n" +
                "4. \t Save\n" +
                "5. \t Back to main menu\n" +
                "Please enter the number to choose\n" +
                "The value of die 1 is :1\n" +
                "The value of die 2 is :2\n" +
                "\n" +
                "This is your third turn in jail. You have to pay HKD150 to get out of jail.\n" +
                "Fine paid successfully. HKD150 is deducted from your account.\n" +
                "Your balance: HKD1350\n" +
                "\n" +
                "Fine paid, you are now free to go.\n" +
                "\n" +
                "You moved to the Position 4.\tIncome tax\n";
        expected = expected.replaceAll("\n", "").replaceAll("\t", "");
        actual = consoleOutput.toString()
                .replaceAll("\n", "")
                .replaceAll("\t", "")
                .replaceAll("\r","");
        assertEquals(expected, actual);

        /* CASE: player1 throws dice and must pay at the third round in jail.
                 End player1 when he/she doesn't have enough money to pay. */
        consoleOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(consoleOutput));
        game = new Game(6);
        //set player is in jail for 3 rounds already
        //set player money = $50
        //set it is not a double
        controller.runGame(game, null, 6);
        expected = "\n" +
                "\u001B[33m####\t\tRound 1\t Player 1's turn\tPosition:Go(1)\t Balance(HKD):1500\t\t#### \u001B[0m\n" +
                "\n" +
                "***\t\tGAME OPTION\t\t***\n" +
                "1. \t Throw the dice\n" +
                "2. \t Check status\n" +
                "3. \t Print Board\n" +
                "4. \t Save\n" +
                "5. \t Back to main menu\n" +
                "Please enter the number to choose\n" +
                "The value of die 1 is :1\n" +
                "The value of die 2 is :2\n" +
                "\n" +
                "This is your third turn in jail. You have to pay HKD150 to get out of jail.\n" +
                "You do not have enough money.\n" +
                "Fine paid successfully. HKD150 is deducted from your account.\n" +
                "Your balance: HKD-100\n" +
                "\n" +
                "\u001B[31mYou have lost due to negative balance ($-100)\n" +
                "Your propoties will be automatically freed.\u001B[0m\n" +
                "\n" +
                "Freed Properties: NONE\n" +
                "\n" +
                "You moved to the Position 1.\tGo\n";
        expected = expected.replaceAll("\n", "").replaceAll("\t", "");
        actual = consoleOutput.toString()
                .replaceAll("\n", "")
                .replaceAll("\t", "")
                .replaceAll("\r","");
        assertEquals(expected, actual);
    }

    /* To check the display of print player's status */
    @Test
    public void runGame7() {
        ByteArrayOutputStream consoleOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(consoleOutput));

        /* CASE: print the player's status with no properties */
        controller.runGame(game, null, 7);
        String expected = "\n" +
                "\u001B[33m####\t\tRound 1\t Player 1's turn\tPosition:Go(1)\t Balance(HKD):1500\t\t#### \u001B[0m\n" +
                "\n" +
                "***\t\tGAME OPTION\t\t***\n" +
                "1. \t Throw the dice\n" +
                "2. \t Check status\n" +
                "3. \t Print Board\n" +
                "4. \t Save\n" +
                "5. \t Back to main menu\n" +
                "Please enter the number to choose\n" +
                "\n" +
                "Round 1\t Player 1's turn\tPosition:Go(1)\t Balance(HKD):1500\n" +
                "Your property:\n" +
                "NONE\n";
        expected = expected.replaceAll("\n", "").replaceAll("\t", "");
        String actual = consoleOutput.toString()
                .replaceAll("\n", "")
                .replaceAll("\t", "")
                .replaceAll("\r","");
        assertEquals(expected, actual);


        /* CASE: print the player's status with properties */
        consoleOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(consoleOutput));
        Square Central = new Property(2, "Central",800,90);
        //adding property
        game.getPlayersList().get(0).getProperties().add(Central);
        controller.runGame(game, null, 7);
        expected = "\n" +
                "\u001B[33m####\t\tRound 1\t Player 1's turn\tPosition:Go(1)\t Balance(HKD):1500\t\t#### \u001B[0m\n" +
                "\n" +
                "***\t\tGAME OPTION\t\t***\n" +
                "1. \t Throw the dice\n" +
                "2. \t Check status\n" +
                "3. \t Print Board\n" +
                "4. \t Save\n" +
                "5. \t Back to main menu\n" +
                "Please enter the number to choose\n" +
                "\n" +
                "Round 1\t Player 1's turn\tPosition:Go(1)\t Balance(HKD):1500\n" +
                "Your property:\n" +
                "POS   NAME                 PRICE    RENT    \n" +
                "2     Central              800      90      \n";
        expected = expected.replaceAll("\n", "").replaceAll("\t", "");
        actual = consoleOutput.toString()
                .replaceAll("\n", "")
                .replaceAll("\t", "")
                .replaceAll("\r","");
        assertEquals(expected, actual);
    }

    /* To check the display of print board */
    @Test
    public void runGame8() {
        ByteArrayOutputStream consoleOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(consoleOutput));

        /* print the board with no owned properties */
        controller.runGame(game, null, 8);
        String expected = "\n" +
                "\u001B[33m####\t\tRound 1\t Player 1's turn\tPosition:Go(1)\t Balance(HKD):1500\t\t#### \u001B[0m\n" +
                "\n" +
                "***\t\tGAME OPTION\t\t***\n" +
                "1. \t Throw the dice\n" +
                "2. \t Check status\n" +
                "3. \t Print Board\n" +
                "4. \t Save\n" +
                "5. \t Back to main menu\n" +
                "Please enter the number to choose\n" +
                "1   Go                \n" +
                "2   Central             \u001B[32mAvailable\u001B[0m\n" +
                "3   Wan Chai            \u001B[32mAvailable\u001B[0m\n" +
                "4   Income tax        \n" +
                "5   Stanley             \u001B[32mAvailable\u001B[0m\n" +
                "6   Jail/Just Visiting\n" +
                "7   Shek O              \u001B[32mAvailable\u001B[0m\n" +
                "8   Mong Kok            \u001B[32mAvailable\u001B[0m\n" +
                "9   Chance            \n" +
                "10  Tsing Yi            \u001B[32mAvailable\u001B[0m\n" +
                "11  Free parking      \n" +
                "12  Shatin              \u001B[32mAvailable\u001B[0m\n" +
                "13  Chance            \n" +
                "14  Tuen Mun            \u001B[32mAvailable\u001B[0m\n" +
                "15  Tai Po              \u001B[32mAvailable\u001B[0m\n" +
                "16  Go to Jail        \n" +
                "17  Sai Kung            \u001B[32mAvailable\u001B[0m\n" +
                "18  Yuen Long           \u001B[32mAvailable\u001B[0m\n" +
                "19  Chance            \n" +
                "20  Tai O               \u001B[32mAvailable\u001B[0m\n";
        expected = expected.replaceAll("\n", "").replaceAll("\t", "");
        String actual = consoleOutput.toString()
                .replaceAll("\n", "")
                .replaceAll("\t", "")
                .replaceAll("\r","");
        assertEquals(expected, actual);


        /* print the board with owned properties */
        consoleOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(consoleOutput));
        Player player1 = game.getPlayersList().get(0);
        //set 2. Central as a property of player 1
        game.getBoard().getSquares().get(1).setOwner(player1);
        controller.runGame(game, null, 8);
        expected = "\n" +
                "\u001B[33m####\t\tRound 1\t Player 1's turn\tPosition:Go(1)\t Balance(HKD):1500\t\t#### \u001B[0m\n" +
                "\n" +
                "***\t\tGAME OPTION\t\t***\n" +
                "1. \t Throw the dice\n" +
                "2. \t Check status\n" +
                "3. \t Print Board\n" +
                "4. \t Save\n" +
                "5. \t Back to main menu\n" +
                "Please enter the number to choose\n" +
                "1   Go                \n" +
                "2   Central             \u001B[31mOccupied\u001B[0m  Player 1's property\n" +
                "3   Wan Chai            \u001B[32mAvailable\u001B[0m\n" +
                "4   Income tax        \n" +
                "5   Stanley             \u001B[32mAvailable\u001B[0m\n" +
                "6   Jail/Just Visiting\n" +
                "7   Shek O              \u001B[32mAvailable\u001B[0m\n" +
                "8   Mong Kok            \u001B[32mAvailable\u001B[0m\n" +
                "9   Chance            \n" +
                "10  Tsing Yi            \u001B[32mAvailable\u001B[0m\n" +
                "11  Free parking      \n" +
                "12  Shatin              \u001B[32mAvailable\u001B[0m\n" +
                "13  Chance            \n" +
                "14  Tuen Mun            \u001B[32mAvailable\u001B[0m\n" +
                "15  Tai Po              \u001B[32mAvailable\u001B[0m\n" +
                "16  Go to Jail        \n" +
                "17  Sai Kung            \u001B[32mAvailable\u001B[0m\n" +
                "18  Yuen Long           \u001B[32mAvailable\u001B[0m\n" +
                "19  Chance            \n" +
                "20  Tai O               \u001B[32mAvailable\u001B[0m\n";
        expected = expected.replaceAll("\n", "").replaceAll("\t", "");
        actual = consoleOutput.toString()
                .replaceAll("\n", "")
                .replaceAll("\t", "")
                .replaceAll("\r","");
        assertEquals(expected, actual);
    }

    /* To check the display of saving game & its function */
    @Test
    public void runGame9() {
        ByteArrayOutputStream consoleOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(consoleOutput));

        /* to check if it can save game and show the messages correctly(the record file is named testCode9.ser) */
        controller.runGame(game, null, 9);
        String expected = "\n" +
                "\u001B[33m####\t\tRound 1\t Player 1's turn\tPosition:Go(1)\t Balance(HKD):1500\t\t#### \u001B[0m\n" +
                "\n" +
                "***\t\tGAME OPTION\t\t***\n" +
                "1. \t Throw the dice\n" +
                "2. \t Check status\n" +
                "3. \t Print Board\n" +
                "4. \t Save\n" +
                "5. \t Back to main menu\n" +
                "Please enter the number to choose\n" +
                "\n" +
                "***\t\tSAVE OPTION\t\t***\n" +
                "1. \t Save record\n" +
                "2. \t Back\n" +
                "Please enter the number to choose\n" +
                "You don't have any game records yet.\n" +
                "\n" +
                "What would you like to name your record?\n" +
                "File saved successfully\n";
        expected = expected.replaceAll("\n", "").replaceAll("\t", "");
        String actual = consoleOutput.toString()
                .replaceAll("\n", "")
                .replaceAll("\t", "")
                .replaceAll("\r","");
        assertEquals(expected, actual);

        //to check the record is saved successfully (is existed)
        assertEquals("testCode9", controller.findRecords(1));


        /* to check if it can replace a record with same name(testCode9.ser) and show the messages correctly */
        consoleOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(consoleOutput));
        //save game (the record file is named testCode9.ser)
        controller.runGame(game, null, 9);
        String subString1 = "\n" +
                "\u001B[33m####\t\tRound 1\t Player 1's turn\tPosition:Go(1)\t Balance(HKD):1500\t\t#### \u001B[0m\n" +
                "\n" +
                "***\t\tGAME OPTION\t\t***\n" +
                "1. \t Throw the dice\n" +
                "2. \t Check status\n" +
                "3. \t Print Board\n" +
                "4. \t Save\n" +
                "5. \t Back to main menu\n" +
                "Please enter the number to choose\n" +
                "\n" +
                "***\t\tSAVE OPTION\t\t***\n" +
                "1. \t Save record\n" +
                "2. \t Back\n" +
                "Please enter the number to choose\n" +
                "\n" +
                "***\t\tGAME RECORDS\t\t***\t(5 RECORDS MAXIMUM)\n" +
                "1. \t Name: testCode9.ser\n";
        String subString2 =
                "What would you like to name your record?\n" +
                "\n" +
                "There is a existing record with the same name, do you want to overwrite it? (Y/N)\n" +
                "The selected record is deleted.\n" +
                "File saved successfully\n";
        subString1 = subString1.replaceAll("\n", "").replaceAll("\t", "");
        subString2 = subString2.replaceAll("\n", "").replaceAll("\t", "");
        actual = consoleOutput.toString()
                .replaceAll("\n", "")
                .replaceAll("\t", "")
                .replaceAll("\r","");

        assertTrue(actual.contains(subString1) && actual.contains(subString2));
        //to check the record is saved successfully (is existed)
        assertEquals("testCode9", controller.findRecords(1));


        /* to check if it can replace a record and show the messages correctly
        *  (replacing test1.ser with testCode9.ser in this case)*/
        //adding a few records
        controller.deleteRecords("testCode9");
        controller.saveGame("test1",game, game.getPlayersList().get(0));
        controller.saveGame("test2",game, game.getPlayersList().get(0));
        controller.saveGame("test3",game, game.getPlayersList().get(0));
        controller.saveGame("test4",game, game.getPlayersList().get(0));
        controller.saveGame("test5",game, game.getPlayersList().get(0));
        consoleOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(consoleOutput));
        //save game (the record file is named testCode9.ser)
        controller.runGame(game, null, 9);
        subString1 = "\n" +
                "\u001B[33m####\t\tRound 1\t Player 1's turn\tPosition:Go(1)\t Balance(HKD):1500\t\t#### \u001B[0m\n" +
                "\n" +
                "***\t\tGAME OPTION\t\t***\n" +
                "1. \t Throw the dice\n" +
                "2. \t Check status\n" +
                "3. \t Print Board\n" +
                "4. \t Save\n" +
                "5. \t Back to main menu\n" +
                "Please enter the number to choose\n" +
                "\n" +
                "***\t\tSAVE OPTION\t\t***\n" +
                "1. \t Save record\n" +
                "2. \t Back\n" +
                "Please enter the number to choose\n" +
                "\n" +
                "***\t\tGAME RECORDS\t\t***\t(5 RECORDS MAXIMUM)";

        subString2 = "1. \t Name: test1.ser";
        String subString3 = "2. \t Name: test2.ser";
        String subString4 = "3. \t Name: test3.ser";
        String subString5 = "4. \t Name: test4.ser";
        String subString6 = "5. \t Name: test5.ser";
        String subString7 =
                "What would you like to name your record?\n" +
                        "Please choose one record to overwrite.\n" +
                        "The selected record is deleted.\n" +
                        "File saved successfully";

        subString1 = subString1.replaceAll("\n", "").replaceAll("\t", "");
        subString2 = subString2.replaceAll("\n", "").replaceAll("\t", "");
        subString3 = subString3.replaceAll("\n", "").replaceAll("\t", "");
        subString4 = subString4.replaceAll("\n", "").replaceAll("\t", "");
        subString5 = subString5.replaceAll("\n", "").replaceAll("\t", "");
        subString6 = subString6.replaceAll("\n", "").replaceAll("\t", "");
        subString7 = subString7.replaceAll("\n", "").replaceAll("\t", "");

        actual = consoleOutput.toString()
                .replaceAll("\n", "")
                .replaceAll("\t", "")
                .replaceAll("\r","");

        assertTrue(actual.contains(subString1) &&
                actual.contains(subString2) &&
                actual.contains(subString3) &&
                actual.contains(subString4) &&
                actual.contains(subString5) &&
                actual.contains(subString6) &&
                actual.contains(subString7)
        );

        //to check if the record is saved successfully (is existed)
        assertEquals("testCode9", controller.findRecords(5));
    }

    /* To check if it can show the messages correctly if player moves to next certain position */
    @RepeatedTest(30)
    public void runGame10(){
        ByteArrayOutputStream consoleOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(consoleOutput));

        game = new Game(6);
        game.getPlayersList().get(0).setPos(10); //set the position of player1 to 10 in order to cover all the square types
        game.getBoard().getSquares().get(14).setOwner(game.getPlayersList().get(1)); //set square14(Tuen Mun) as player2's property, to test the reaction of stepping on other's property
        controller.runGame(game, null, 10);
        final String output = consoleOutput.toString()
                .replaceAll("\n", "")
                .replaceAll("\t", "")
                .replaceAll("\r","");


        //Square -- Go to Jail
        if (game.getBoard().findSquare(game.getPlayersList().get(0).getPos()).name.equals("Go to Jail")) {
            String subString = "You are going to jail(6)";
            assertTrue( output.contains(subString));
        }

        //Square -- Income tax
        if (game.getBoard().findSquare(game.getPlayersList().get(0).getPos()).name.equals("Income tax")) {
            String subString = "You have to pay income tax for 10% of your money.";
            assertTrue( output.contains(subString));
        }

        //Square -- Chance
        if (game.getBoard().findSquare(game.getPlayersList().get(0).getPos()).name.equals("Chance")) {
            String subString = "You won a bonus of HKD";
            String subString1 = "You lost HKD";
            String subString2 = "You do not have enough money";
            assertTrue(
                    output.contains(subString) ||
                            output.contains(subString1) ||
                            output.contains(subString2)
            );
        }

        //Square -- Property
        if (game.getBoard().findSquare(game.getPlayersList().get(0).getPos()).isProperty()) {
            String subString = "You have to pay for the rent HKD";
            String subString1 = "You do not have enough money.";
            String subString2 = "You have successfully owned";
            String subString3 = "You have not enough money to purchase.";
            assertTrue(
                    output.contains(subString) ||
                            output.contains(subString1) ||
                            output.contains(subString2) ||
                            output.contains(subString3)
            );
        }

    }

    /* To check if it can show the messages correctly if the game ends */
    @Test
    public void runGame11() {

        /* Game ends when there is only one player left (game round <= 100) */
        ByteArrayOutputStream consoleOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(consoleOutput));
        game = new Game(2);     //init a 2-players game
        game.getPlayersList().get(0).setMoney(0);  //set player 1's money to 0
        game.getPlayersList().get(0).setInJail(true);  //set player 1 in jail
        game.getPlayersList().get(0).setInJailCount(3);  //set player 1 in jail for already 3 rounds, so that player 1 must pay the fee
        controller.runGame(game, null, 11);  //player 1 is set to die
        String subString = "\u001B[32m####Player 2 is the winner!!!####\u001B[0m";
        String output = consoleOutput.toString()
                .replaceAll("\n", "")
                .replaceAll("\t", "")
                .replaceAll("\r","");
        assertTrue(output.contains(subString));


        //Game ends when the game round is passed 100  (Tie version)
        consoleOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(consoleOutput));
        game = new Game(3);     //init a 3-players game with same money amount (HKD 1500)
        game.setGameRound(101);             //set to the end
        controller.runGame(game, null, 11);
        subString = "\u001B[32m####Tie!!!####" +
                "Player 1 Player 2 Player 3 are the winners!!!\u001B[0m";
        output = consoleOutput.toString()
                .replaceAll("\n", "")
                .replaceAll("\t", "")
                .replaceAll("\r","");
        assertTrue(output.contains(subString));


        //Game ends when the game round is passed 100  (decide winner by money amount)
        consoleOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(consoleOutput));
        game = new Game(3);                 //init a 3-players game
        game.getPlayersList().get(2).setMoney(1501);    //set player3 to have higher money amount
        game.setGameRound(101);     //set to the end
        controller.runGame(game, null, 11);
        subString = "\u001B[32m####Player 3 is the winner!!!####\u001B[0m";
        output = consoleOutput.toString()
                .replaceAll("\n", "")
                .replaceAll("\t", "")
                .replaceAll("\r","");
        assertTrue(output.contains(subString));
    }




    /* Empty the directory after each test */
    @AfterEach
    public void emptyDirEach(){
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