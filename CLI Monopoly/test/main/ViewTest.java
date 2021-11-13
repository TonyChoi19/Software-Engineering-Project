package main;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ViewTest {
    private View view = new View();
    ByteArrayOutputStream consoleOutput = new ByteArrayOutputStream();

    /*  for record the console output   */
    @BeforeEach
    public void recordConsole() {
        System.setOut(new PrintStream(consoleOutput));
    }

    /* To check if the function can print the "CLI MONOPOLY" intro
        (It is correct that the run time is slow, because we set a delay on printing) */
    @Test
    public void printGameTitle() throws InterruptedException {
        view.printGameTitle();

        String expected = " ______     __         __        \n" +
                "/\\  ___\\   /\\ \\       /\\ \\       \n" +
                "\\ \\ \\____  \\ \\ \\____  \\ \\ \\      \n" +
                " \\ \\_____\\  \\ \\_____\\  \\ \\_\\     \n" +
                "  \\/_____/   \\/_____/   \\/_/     \n" +
                "\n" +
                " __    __     ______     __   __     ______     ______   ______     __         __  __   \n" +
                "/\\ \"-./  \\   /\\  __ \\   /\\ \"-.\\ \\   /\\  __ \\   /\\  == \\ /\\  __ \\   /\\ \\       /\\ \\_\\ \\  \n" +
                "\\ \\ \\-./\\ \\  \\ \\ \\/\\ \\  \\ \\ \\-.  \\  \\ \\ \\/\\ \\  \\ \\  _-/ \\ \\ \\/\\ \\  \\ \\ \\____  \\ \\____ \\ \n" +
                " \\ \\_\\ \\ \\_\\  \\ \\_____\\  \\ \\_\\\\\"\\_\\  \\ \\_____\\  \\ \\_\\    \\ \\_____\\  \\ \\_____\\  \\/\\_____\\\n" +
                "  \\/_/  \\/_/   \\/_____/   \\/_/ \\/_/   \\/_____/   \\/_/     \\/_____/   \\/_____/   \\/_____/\n";
        String actual = consoleOutput.toString()
                .replaceAll("\r","");
        assertEquals(expected, actual);
    }

    /* To check if the function can print the main menu */
    @Test
    public void printMainMenu() {
        view.printMainMenu();
        String expected = "\n***\t\tGAME MENU\t\t***" +
                "\n1. \t NEW GAME" +
                "\n2. \t LOAD GAME" +
                "\n3. \t MANAGE GAME RECORDS" +
                "\n4. \t EXIT" +
                "\nPlease enter the number to choose\n";
        String actual = consoleOutput.toString()
                .replaceAll("\r","");
        assertEquals(expected, actual);
    }

    /* To check if the function can print the main game option menu */
    @Test
    public void printMainGameOption() {
        view.printMainGameOption();
        String expected = ("\n***\t\tGAME OPTION\t\t***" +
                "\n1. \t Throw the dice" +
                "\n2. \t Check status" +
                "\n3. \t Print Board" +
                "\n4. \t Save" +
                "\n5. \t Back to main menu" +
                "\nPlease enter the number to choose\n");
        String actual = consoleOutput.toString()
                .replaceAll("\r","");
        assertEquals(expected, actual);
    }

    /* To check if the function can print the save game option menu */
    @Test
    public void printSaveGameOption() {
        view.printSaveGameOption();
        String expected = ("\n***\t\tSAVE OPTION\t\t***" +
                "\n1. \t Save record" +
                "\n2. \t Back" +
                "\nPlease enter the number to choose\n");
        String actual = consoleOutput.toString()
                .replaceAll("\r","");
        assertEquals(expected, actual);
    }

    /* To check if the function can print the invalid message */
    @Test
    public void printInvalidMsg() {
        view.printInvalidMsg();
        String expected = "Invalid input, please try again.\n";
        String actual = consoleOutput.toString()
                .replaceAll("\r","");
        assertEquals(expected, actual);
    }

    /* To check if the function can print existing game records */
    @Test
    public void printRecords() {
        /* CASE: no record */
        view.printRecords();
        String expected = "You don't have any game records yet.\n";
        String actual = consoleOutput.toString()
                .replaceAll("\r","");
        assertEquals(expected, actual);



        /* CASE: have records */
        Controller controller = new Controller();
        Game game = new Game(6);

        //adding a few records
        controller.saveGame("test1",game, game.getPlayersList().get(0));
        controller.saveGame("test2",game, game.getPlayersList().get(0));
        controller.saveGame("test3",game, game.getPlayersList().get(0));
        controller.saveGame("test4",game, game.getPlayersList().get(0));

        consoleOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(consoleOutput));  //reset the console output

        view.printRecords();
        String subString = "***\t\tGAME RECORDS\t\t***\t(5 RECORDS MAXIMUM)\n";
        String subString1 = "1. \t Name: test1.ser             Created Time: ";
        String subString2 = "2. \t Name: test2.ser             Created Time: ";
        String subString3 = "3. \t Name: test3.ser             Created Time: ";
        String subString4 = "4. \t Name: test4.ser             Created Time: ";
        actual = consoleOutput.toString()
                .replaceAll("\r","");

        assertTrue(actual.contains(subString) &&
                actual.contains(subString1) &&
                actual.contains(subString2) &&
                actual.contains(subString3) &&
                actual.contains(subString4));

        //delete the records after testing
        controller.deleteRecords("test1");
        controller.deleteRecords("test2");
        controller.deleteRecords("test3");
        controller.deleteRecords("test4");
    }

    /* To check if the function can print out the winners according to the arrayList */
    @Test
    public void announceWinners() {
        ArrayList<Player> winners = new ArrayList<>();

        /* CASE: one winner only */
        winners.add(new Player("Player1"));
        view.announceWinners(winners);
        String expected = "\n" +
                "\u001B[32m####\t\tPlayer1 is the winner!!!\t\t####\u001B[0m\n";
        String actual = consoleOutput.toString()
                .replaceAll("\r","");
        assertEquals(expected, actual);



        /* CASE: multiple winners (Tie) */
        consoleOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(consoleOutput));  //reset the console output

        //adding more winners
        winners.add(new Player("Player2"));
        winners.add(new Player("Player3"));

        view.announceWinners(winners);
        expected = "\n" +
                "\u001B[32m####\t\tTie!!!\t\t####\n" +
                "Player1 Player2 Player3 are the winners!!!\n" +
                "\u001B[0m\n";
        actual = consoleOutput.toString()
                .replaceAll("\r","");
        assertEquals(expected, actual);
    }

    /* To check if the function can print out the dice values according to the param */
    @Test
    public void printDiceValue() {
        Dice dice = new Dice();
        dice.setDie1(1);
        dice.setDie1(2);

        view.printDiceValue(dice);
        String expected = "The value of die 1 is :2\n" +
                "The value of die 2 is :1\n";
        String actual = consoleOutput.toString()
                .replaceAll("\r","");
        assertEquals(expected, actual);
    }

    /* To check if the function can print out detailed position information */
    @Test
    public void printDetailedPos() {
        Board board = new Board();

        /* CASE: moved to position 2 (Central), and it is not owned */
        view.printDetailedPos(board, 2);
        String expected = "\n" +
                "You moved to the Position 2.\tProperty:Central\tPrice(HKD):800\tRent(HKD):90\tOwnedBy:None\u001B[32m (Available)\u001B[0m\n";
        String actual = consoleOutput.toString()
                .replaceAll("\r","");
        assertEquals(expected, actual);



        /* CASE: moved to position 2 (Central), and it is owned */
        //reset the console output
        consoleOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(consoleOutput));

        //set Central is owned by Player1
        board.getSquares().get(1).setOwner(new Player("Player1"));

        view.printDetailedPos(board, 2);
        expected = "\n" +
                "You moved to the Position 2.\tProperty:Central\tPrice(HKD):800\tRent(HKD):90\tOwnedBy:\u001B[31mPlayer1\u001B[0m\n";
        actual = consoleOutput.toString()
                .replaceAll("\r","");
        assertEquals(expected, actual);



        /* CASE: moved to a non-property square */
        //reset the console output
        consoleOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(consoleOutput));

        //moving to position 4 (Income tax)
        view.printDetailedPos(board, 4);
        expected = "\n" +
                "You moved to the Position 4.\tIncome tax\n";
        actual = consoleOutput.toString()
                .replaceAll("\r","");
        assertEquals(expected, actual);
    }

    /* To check if the function can print out the status of the whole board, including the owner of the properties */
    @Test
    public void printBoard() {
        Board board = new Board();

        /* CASE: all properties are not owned */
        view.printBoard(board);
        String expected = "1   Go                \n" +
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
        String actual = consoleOutput.toString()
                .replaceAll("\r","");
        assertEquals(expected, actual);



        /* CASE: Some properties are owned */

        //reset the console output
        consoleOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(consoleOutput));

        //set Central is owned by Player1
        board.getSquares().get(1).setOwner(new Player("Player1"));
        //set Wan Chai is owned by Player2
        board.getSquares().get(2).setOwner(new Player("Player2"));

        view.printBoard(board);
        expected = "1   Go                \n" +
                "2   Central             \u001B[31mOccupied\u001B[0m  Player1's property\n" +
                "3   Wan Chai            \u001B[31mOccupied\u001B[0m  Player2's property\n" +
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
        actual = consoleOutput.toString()
                .replaceAll("\r","");
        assertEquals(expected, actual);
    }

    /* To check if the function can print out the free properties message */
    @Test
    public void printFreePropertyMsg() {
        Player player1 = new Player("Player1");

        /* CASE: player does not have any properties to be freed */
        view.printFreePropertyMsg(player1);
        String expected = "\n" +
                "\u001B[31mYou have lost due to negative balance ($1500)\n" +      //the money here is retrieved from player's money. The money will be negative if it shows in actual game.
                "Your propoties will be automatically freed.\u001B[0m\n" +
                "\n" +
                "Freed Properties: NONE\n";
        String actual = consoleOutput.toString()
                .replaceAll("\r","");
        assertEquals(expected, actual);



        /* CASE: player has properties to be freed */
        //purchase the property
        Square Central = new Property(2, "Central",800,90);
        player1.purchaseProperty(Central);

        //reset the console output
        consoleOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(consoleOutput));

        view.printFreePropertyMsg(player1);
        expected = "\n" +
                "\u001B[31mYou have lost due to negative balance ($700)\n" +    //the money here is retrieved from player's money. The money will be negative if it shows in actual game.
                "Your propoties will be automatically freed.\u001B[0m\n" +
                "\n" +
                "Freed Properties:\n" +
                "2.\tCentral\n";
        actual = consoleOutput.toString()
                .replaceAll("\r","");
        assertEquals(expected, actual);
    }

    /* To check if the function can print out the deduct money message */
    @Test
    public void printDeductMoneyMsg() {
        Player player1 = new Player("Player1");
        view.printDeductMoneyMsg(player1, 150, "Fine");

        String expected = "Fine paid successfully. HKD150 is deducted from your account.\n" +
                "Your balance: HKD1500\n";  //the money here is retrieved from player's money. The money will be deducted before printing this message in actual game.
        String actual = consoleOutput.toString()
                .replaceAll("\r","");
        assertEquals(expected, actual);
    }

    /* To check if the function can print out the add money message */
    @Test
    public void printAddMoneyMsg() {
        Player player1 = new Player("Player1");
        view.printAddMoneyMsg(player1, 150);

        String expected = "HKD150 is added into Player1's account.\n";
        String actual = consoleOutput.toString()
                .replaceAll("\r","");
        assertEquals(expected, actual);
    }

    /* To check if the function can print out the release from jail message */
    @Test
    public void printReleaseFromJailMsg() {

        /* CASE: paid fine to go */
        view.printReleaseFromJailMsg("Fine");
        String expected = "\nFine paid, you are now free to go.\n";
        String actual = consoleOutput.toString()
                .replaceAll("\r","");
        assertEquals(expected, actual);

        /* CASE: paid fine to go */
        //reset the console output
        consoleOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(consoleOutput));

        view.printReleaseFromJailMsg("Double");
        expected = "\nCongrats! It is a double. You are now free to go.\n";
        actual = consoleOutput.toString()
                .replaceAll("\r","");
        assertEquals(expected, actual);
    }

    /* Restore the output display back to console */
    @AfterEach
    public void restoreStreams() {
        System.setOut(System.out);
    }

}