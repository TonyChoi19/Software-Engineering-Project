package main;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;

public class View implements Serializable {

    //For reading input
    private InputStreamReader isr;
    private BufferedReader br;

    /*  Constructor */
    public View() {
        this.isr = new InputStreamReader(System.in);
        this.br = new BufferedReader(isr);
    }

    /**
     * To print out the string
     * @param str   String to be printed
     */
    public void print(String str){
        System.out.println(str);
    }

    /* ================ Game Title ================ */

    /**
     * To print the styled "CLI MONOPOLY"
     * @throws InterruptedException
     */
    public void printGameTitle() throws InterruptedException {
        try {
            InputStream inputStream = getClass().getResourceAsStream("/main/resources/gameTitle.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                Thread.sleep(100);
            }
        } catch (IOException ignored) {}
    }
    /* ================ Game Title ================ */



    /* ================ Controller ================ */
    /**
     * Read next line (user's input)
     * @return  input   The string of player's input
     */
    public String scanInput(){
        System.out.print(Constant.ANSI_GREEN + "-> " +Constant.ANSI_RESET);
        String input = "";
        try{
            input = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return input;
    }

    /**
     * Display main menu
     */
    public void printMainMenu(){
        System.out.println("\n***\t\tGAME MENU\t\t***" +
                "\n1. \t NEW GAME" +
                "\n2. \t LOAD GAME" +
                "\n3. \t MANAGE GAME RECORDS" +
                "\n4. \t EXIT" +
                "\nPlease enter the number to choose");
    }

    /**
     * Display game options
     */
    public void printMainGameOption(){
        System.out.println("\n***\t\tGAME OPTION\t\t***" +
                "\n1. \t Throw the dice" +
                "\n2. \t Check status" +
                "\n3. \t Print Board" +
                "\n4. \t Save" +
                "\n5. \t Back to main menu" +
                "\nPlease enter the number to choose");
    }

    /**
     * Display save option  (To go back if player wants)
     */
    public void printSaveGameOption(){
        System.out.println("\n***\t\tSAVE OPTION\t\t***" +
                "\n1. \t Save record" +
                "\n2. \t Back" +
                "\nPlease enter the number to choose");
    }

    /**
     * Display invalid message
     */
    public void printInvalidMsg(){
        System.out.println("Invalid input, please try again.");
    }

    /**
     * Display saved game records
     */
    public void printRecords() {
        File recordsDir = new File(Constant.CWD+"Game records/");
        File[] directoryListing = recordsDir.listFiles();
        ArrayList<File> gameRecords = new ArrayList<>();

        //Loop the directory and add the file to the arrayList
        if (directoryListing!=null && directoryListing.length!=0) {
            for (File file : directoryListing) {
                if (file.getName().endsWith(".ser"))
                    gameRecords.add(file);
            }

            System.out.println("\n***\t\tGAME RECORDS\t\t***\t(5 RECORDS MAXIMUM)");
            for (int i =0;i<gameRecords.size();i++){
                String name = gameRecords.get(i).getName();
                long time = gameRecords.get(i).lastModified();
                Date date = new Date(time);
                System.out.printf( "%d. \t Name: %-21s ",i+1,name);
                System.out.println("Created Time: " + date);
            }
        }else{
            System.out.println("You don't have any game records yet.");
        }
    }

    /**
     * To announce the winners in the end of the game
     * @param winners The arrayList to store winners
     */
    public void announceWinners(ArrayList<Player> winners){
        if (winners.size() == 1) {
            System.out.println("\n" + Constant.ANSI_GREEN + "####\t\t" + winners.get(0).getName() + " is the winner!!!\t\t####" + Constant.ANSI_RESET);
        } else {
            System.out.println("\n" + Constant.ANSI_GREEN + "####\t\tTie!!!\t\t####");
            for (Player player1 : winners) {
                System.out.print(player1.getName() + " ");
            }
            System.out.println("are the winners!!!\n" + Constant.ANSI_RESET);
        }
    }
    /* ================ Controller ================ */



    /* ================ Dice ================ */

    /**
     * To print the values of die1 & die2
     * @param dice
     */
    public void printDiceValue(Dice dice){
        System.out.println("The value of die 1 is :" + dice.getDie1());
        System.out.println("The value of die 2 is :" + dice.getDie2());
    }
    /* ================ Dice ================ */



    /* ================ Game ================ */
    /**
     * Print the position, name of the square, owner(if it is a property) after player moves
     * @param board
     * @param pos The position of the square
     */
    public void printDetailedPos(Board board, int pos){
        if (board.findSquare(pos).isProperty()){
            //the property is owned
            if (board.findSquare(pos).getOwner()!=null)
                System.out.println("\nYou moved to the Position " + pos + ".\tProperty:" + board.findSquare(pos).getName() + "\tPrice(HKD):" + board.findSquare(pos).getPrice() + "\tRent(HKD):" +board.findSquare(pos).getRent() + "\tOwnedBy:\u001B[31m" + board.findSquare(pos).getOwner().getName() + "\u001B[0m" );
                //the property is not owned
            else{
                System.out.println("\nYou moved to the Position " + pos + ".\tProperty:" + board.findSquare(pos).getName() + "\tPrice(HKD):" + board.findSquare(pos).getPrice() + "\tRent(HKD):" +board.findSquare(pos).getRent() + "\tOwnedBy:None\u001B[32m (Available)\u001B[0m" );
            }
        }else{
            System.out.println("\nYou moved to the Position " + pos + ".\t" + board.findSquare(pos).getName());
        }
    }
    /* ================ Game ================ */



    /* ================ Board ================ */
    /**
     * Print board state
     * e.g. squares no. , square name, players' position, properties' state
     * @param board
     */
    public void printBoard(Board board){
        for (Square square : board.getSquares()){
            if (square.isProperty() && square.getOwner() == null)
                System.out.printf("%-3d %-18s  \u001B[32mAvailable\u001B[0m\n", square.getPos(), square.getName());
            else if (square.isProperty() && square.getOwner() != null)
                System.out.printf("%-3d %-18s  \u001B[31mOccupied\u001B[0m  %s's property\n", square.getPos(), square.getName(), square.getOwner().getName());
            else
                System.out.printf("%-3d %-18s\n", square.getPos(), square.getName());
        }
    }
    /* ================ Board ================ */



    /* ================ Player ================ */
    /**
     * Free(empty) player's properties
     * @param player The player who lost
     */
    public void printFreePropertyMsg(Player player){
        System.out.println("\n\u001B[31mYou have lost due to negative balance ($" + player.getMoney() + ")" +
                "\nYour propoties will be automatically freed.\u001B[0m");

        if (player.getProperties().isEmpty())
            System.out.println("\nFreed Properties: NONE");
        else{
            System.out.println("\nFreed Properties:");
            for (Square property : player.getProperties()){
                System.out.printf("%d.\t%s\n", property.getPos(), property.getName());
            }
        }
    }

    /**
     * To deduct player's money
     * @param player The player who needs to deduct the money
     * @param amount How much money to deduct
     * @param reason The event to deduct
     */
    public void printDeductMoneyMsg(Player player, int amount, String reason){
        System.out.println(reason + " paid successfully. HKD" + amount + " is deducted from your account." +
                "\nYour balance: HKD" + player.getMoney());
    }

    /**
     * To add player's money
     * @param player The player who needs to add the money
     * @param amount How much money to add
     */
    public void printAddMoneyMsg(Player player, int amount){
        System.out.println("HKD"+ amount + " is added into " + player.getName() + "'s account.");
    }

    /**
     * Set player free from jail
     * @param reason Reason of releasing
     */
    public void printReleaseFromJailMsg(String reason){
        if (reason.equals("Fine"))
            System.out.println("\nFine paid, you are now free to go.");
        if (reason.equals("Double"))
            System.out.println("\nCongrats! It is a double. You are now free to go.");
    }
    /* ================ Player ================ */











}
