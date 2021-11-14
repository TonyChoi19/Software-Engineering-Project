package main;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class Controller {

    private View view;

    /* Constructor */
    public Controller() {
        this.view = new View();
    }

    /**
     * Game start
     * @throws InterruptedException
     */
    public void start() throws InterruptedException {
        //Play styled game intro
        view.printGameTitle();

        String input;
        boolean backToMainMenu;

        do {
            //Display the main menu options
            view.printMainMenu();
            input = view.scanInput();
            backToMainMenu =false;

            switch (input) {
                /* NEW GAME */
                case "1":
                    /* Set the number of players for new game */
                    do {
                        view.print("\nPlease enter the number of players: (Minimum=2, Maximum=6)");
                        input = view.scanInput();
                        if (input.equals("") || Integer.parseInt(input) < 2 || Integer.parseInt(input) > 6) {
                            view.printInvalidMsg();
                        }else
                            break;
                    } while (true);

                    Game game = new Game(Integer.parseInt(input));
                    runGame(game, null, null);
                    backToMainMenu = true;

                    break;



                /* Load game records */
                case "2":
                    view.printRecords();

                    /* Directory not found */
                    File gameRecordsDir = new File(Constant.CWD+"Game records");
                    if (!gameRecordsDir.exists() || countRecords()==0){
                        input = "999";
                        break;
                    }


                    /* Load record and start playing */
                    int chosenRecordNo;
                    do {
                        view.print("\nWhich record would you want to load? (Type \"back\" to return)");
                        input = view.scanInput();
                        if (!isInRangeOfRecords(input)){
                            if (input.toUpperCase().equals("BACK"))
                                break;
                            view.printInvalidMsg();
                            continue;
                        }

                        chosenRecordNo = Integer.parseInt(input);

                        GameRecord  loadedGameRecord = loadGame(findRecords(chosenRecordNo));
                        Game loadedGame = loadedGameRecord.getGame();
                        runGame(loadedGame, loadedGameRecord.getPlayerTurn(), null);
                        backToMainMenu = true;

                    }while(!isInRangeOfRecords(input));

                    break;



                /* Manage game records
                * (View and delete) */
                case "3":
                    view.printRecords();
                    File directory = new File(Constant.CWD+"Game records");
                    if (directory.exists() && countRecords()!=0){
                        do {
                            view.print("\nWhich record would you like to delete? (Type \"back\" to return)");
                            input = view.scanInput();
                            if (isInRangeOfRecords(input)) {
                                deleteRecords(Integer.parseInt(input));
                                break;
                            }else if(input.toUpperCase().equals("BACK")){
                                break;
                            }else{
                                view.printInvalidMsg();
                            }
                        }while (!isInRangeOfRecords(input));
                    }
                    backToMainMenu = true;
                    break;

                case "4":
                    end();
                    break;

                default:
                    view.print("Sorry, please enter the correct number.");
                    break;
            }
        }while(!input.equals("1") && !input.equals("2") && !input.equals("3") && !input.equals("4") || backToMainMenu);

    }


    /**
     * Play "Thank you" and close application
     */
    public void end(){
        view.print("*** \tThank you for playing. Bye Bye! \t***");
        System.exit(0);
    }
    
    /**
     * Distinguish the name if it is used
     *
     * @param name  The name for the record
     * @return  Boolean  True if unique, False if unique
     */
    public boolean isUniqueNameRecords(String name){
        File recordsDir = new File(Constant.CWD+"Game records/");
        File[] directoryListing = recordsDir.listFiles();
        boolean ret = true;
        if (directoryListing!=null) {
            for (File file : directoryListing) {
                if (file.getName().equals(name+".ser"))
                    ret = false;
            }

        }
        return ret;
    }

    /**
     * Count the number of saved records
     *
     * @return The number of saved records
     */
    public int countRecords(){
        File recordsDir = new File(Constant.CWD+"Game records/");
        File[] directoryListing = recordsDir.listFiles();
        if (directoryListing!=null) {
            return directoryListing.length;
        }else
            return 0;
    }


    /**
     * Delete saved record
     *
     * @param number    The number that represents the certain record
     */
    public void deleteRecords(int number){
        File recordsDir = new File(Constant.CWD+"Game records/");
        File[] directoryListing = recordsDir.listFiles();
        if (directoryListing!=null) {
            for (int i=0; i<directoryListing.length;i++) {
                if (i == (number-1))
                    if(directoryListing[i].delete())
                        view.print("The selected record is deleted.");
            }
        }
    }

    /**
     * Delete saved record
     *
     * @param name      The name of the record
     */
    public void deleteRecords(String name){
        File recordsDir = new File(Constant.CWD+"Game records/");
        File[] directoryListing = recordsDir.listFiles();
        if (directoryListing!=null) {
            for (File file : directoryListing) {
                if (file.getName().equals(name+".ser")) {
                    if (file.delete())
                        view.print("The selected record is deleted.");
                }
            }
        }
    }

    /**
     * Get the name of record by number
     *
     * @param number    The number that represents the certain record
     * @return          The name of the record
     */
    public String findRecords(int number){
        File recordsDir = new File(Constant.CWD+"Game records/");
        File[] directoryListing = recordsDir.listFiles();
        String ret ="";
        if (directoryListing!=null) {
            for (int i=0; i<directoryListing.length;i++) {
                if (i == (number-1))
                    ret = directoryListing[i].getName().replace(".ser","");
            }
        }
        return ret;
    }

    /**
     * Distinguish the input is numeric of not
     *
     * @param input     Player's input
     * @return          Numeric --> true, not numeric --> false,
     */
    public boolean isNumeric(String input){
        return input != null && input.matches("[0-9.]+");
    }

    /**
     * Distinguish if the input is in the range of the records existed
     *
     * @param input     Number
     * @return          In the range --> true, not the range --> false
     */
    public boolean isInRangeOfRecords(String input){
        if (isNumeric(input)){
            return  Integer.parseInt(input)>=1 && Integer.parseInt(input)<=countRecords();
        }else
            return false;
    }

    /**
     * Save a game record as a file
     *
     * @param name      The record name
     * @param game      The game to be saved
     * @param player    The turn of player
     */
    public void saveGame(String name, Game game, Player player) {
        GameRecord recordToSave = new GameRecord(name, game, player);
        try{
            FileOutputStream fos = new FileOutputStream(Constant.CWD+"Game records/"+name+".ser");
            ObjectOutputStream out =new ObjectOutputStream(fos);
            out.writeObject(recordToSave);
            out.close();
            fos.close();
            view.print("File saved successfully");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Load a game record by the file name
     *
     * @param fileName      The name of the game record
     * @return              The record to be loaded
     */
    public GameRecord loadGame(String fileName){
        GameRecord recordToLoad;
        try{
            FileInputStream fis = new FileInputStream(Constant.CWD+"Game records/"+fileName+".ser");
            ObjectInputStream in =new ObjectInputStream(fis);
            recordToLoad = (GameRecord)in.readObject();
            in.close();
            fis.close();
            view.print(fileName+".ser loaded successfully.");
        } catch (IOException | ClassNotFoundException e) {
            view.printInvalidMsg();
            return null;
        }

        return recordToLoad;
    }

    /**
     * Run game
     *  @param game  New game / game from game record
     * @param playerTurn    Null for new game, player for load game
     * @param testCode  For test only
     */
    public void runGame(Game game, Player playerTurn, Integer testCode) {

        String input;
        int initGameRound = game.getGameRound();
        boolean playerFoundInFirstRound = false;

        gameLoop:
        /* Loop until the game round has reached 100 / only one player left */
        while(game.getGameRound() <=100 && game.getPlayersList().size() != 1){
            Iterator<Player> it = game.getPlayersList().iterator();
            while( it.hasNext()){
                Player player = it.next();

                //restore the player turn
                if (playerTurn!=null && player!=playerTurn && game.getGameRound()==initGameRound && !playerFoundInFirstRound)
                    continue;
                else
                    playerFoundInFirstRound = true;

                view.print("\n"+Constant.ANSI_YELLOW+"####\t\tRound " + game.getGameRound()  +"\t " + player.getName() +"'s turn\tPosition:" + game.getBoard().findSquare(player.getPos()).getName() + "(" + player.getPos() + ")"+ "\t Balance(HKD):" + player.getMoney() + "\t\t#### "+ Constant.ANSI_RESET);

                int step;

                /* Payment option for players in jail */
                if (player.isHaveChoicePayJail()){
                    do {
                        view.print("Do you want to pay a fine of HKD 150 to get free? (Y/N)");

                        /* For test here */
                        if (testCode!= null && testCode == 2){
                            input = "Y";
                            break;
                        }
                        if (testCode!= null && testCode == 4 || testCode!= null && testCode == 5){
                            input = "N";
                            break;
                        }
                        /* For test here */

                        input = view.scanInput();
                        if (!input.toUpperCase().equals("Y") && !input.toUpperCase().equals("N"))
                            view.printInvalidMsg();
                    }while(!input.toUpperCase().equals("Y") && !input.toUpperCase().equals("N"));

                    /* pay fine to go */
                    if (input.toUpperCase().equals("Y") && player.isEnoughMoneyToPay(150)){
                        player.deductMoney(150, "Fine");
                        player.releaseFromJail("Fine");
                    }

                    /* not enough money to pay fine */
                    if (input.toUpperCase().equals("Y") && !player.isEnoughMoneyToPay(150)){
                        view.print("Sorry, you don't have enough money to pay");
                    }

                }

                boolean repeatMenuFlag;
                do {
                    repeatMenuFlag = false;
                    view.printMainGameOption();

                    /* For test here */
                    if (testCode!= null && (testCode == 1 || testCode == 2))
                        break gameLoop;
                    else if (testCode!= null && (testCode == 3 || testCode == 4 || testCode == 5 || testCode == 6 || testCode == 10 || testCode == 11))
                        input = "1";    //throw the dice
                    else if (testCode!= null && testCode == 7)
                        input = "2";    //print player's status
                    else if (testCode!= null && testCode == 8)
                        input = "3";    //print board
                    else if (testCode!= null && testCode == 9)
                        input = "4";    //choose to save record
                    /* For test here */


                    else
                        input = view.scanInput();

                    /* Throw the dice */
                    if (input.equals("1")) {
                        step = game.getDice().throwDice();


                        /* For test here */
                        if (testCode!= null && testCode == 3){
                            player.setInJail(true);
                            player.setInJailCount(1);
                            game.getDice().setDie1(1);
                            game.getDice().setDie2(1);
                            game.getDice().setDouble(true);
                            step = game.getDice().getDie1() + game.getDice().getDie2();
                        }
                        if (testCode!= null && testCode == 4){
                            player.setInJail(true);
                            player.setInJailCount(1);
                            game.getDice().setDie1(1);
                            game.getDice().setDie2(2);
                            game.getDice().setDouble(false);
                            step = game.getDice().getDie1() + game.getDice().getDie2();
                        }
                        if (testCode!= null && testCode == 5){
                            player.setInJail(true);
                            player.setInJailCount(3);
                            game.getDice().setDie1(1);
                            game.getDice().setDie2(2);
                            game.getDice().setDouble(false);
                            step = game.getDice().getDie1() + game.getDice().getDie2();
                        }
                        if (testCode!= null && testCode == 6){
                            player.setInJail(true);
                            player.setInJailCount(3);
                            player.setMoney(50);
                            game.getDice().setDie1(1);
                            game.getDice().setDie2(2);
                            game.getDice().setDouble(false);
                            step = game.getDice().getDie1() + game.getDice().getDie2();
                        }
                        if (testCode!= null && testCode == 11){
                            game.getDice().setDie1(1);
                            game.getDice().setDie2(2);
                            game.getDice().setDouble(false);
                            step = game.getDice().getDie1() + game.getDice().getDie2();
                        }
                        /* For test here */



                        view.printDiceValue(game.getDice());

                        /* player in jail gets double */
                        if (game.getDice().isDouble() && player.isInJail()) {
                            player.releaseFromJail("Double");
                        } else {
                            /* player in jail (not third turn) doesn't throw a double */
                            if (player.isHaveChoicePayJail()) {
                                player.setInJailCount(player.getInJailCount() + 1);
                            }
                            /* player in jail (third turn) doesn't throw a double --> must pay the fine */
                            if (player.isInJail() && player.getInJailCount() == 3) {
                                view.print("\nThis is your third turn in jail. You have to pay HKD150 to get out of jail.");
                                if (player.isEnoughMoneyToPay(150)) {
                                    player.deductMoney(150, "Fine");
                                    player.releaseFromJail("Fine");
                                } else {
                                    view.print("You do not have enough money.");
                                    player.deductMoney(150, "Fine");
                                    player.freeProperty();
                                    it.remove();
                                }
                            }
                        }

                        if (!player.isInJail()) {
                            player.move(step);
                        }

                        /* Check all the info of the player */
                    } else if (input.equals("2")) {
                        view.print("\nRound " + game.getGameRound() + "\t " + player.getName() + "'s turn\tPosition:" + game.getBoard().findSquare(player.getPos()).getName() + "(" + player.getPos() + ")" + "\t Balance(HKD):" + player.getMoney());
                        view.print("Your property:");
                        if (player.getProperties().isEmpty())
                            view.print("NONE");
                        else {
                            System.out.printf("%-5s %-20s %-8s %-8s\n", "POS", "NAME", "PRICE", "RENT");
                            for (Square property : player.getProperties()) {
                                System.out.printf("%-5s %-20s %-8s %-8s\n", property.getPos(), property.getName(), property.getPrice(), property.getRent());
                            }
                        }

                        /* Print the details of the board */
                    } else if (input.equals("3")) {
                        view.printBoard(game.getBoard());

                        /* Save a record */
                    } else if (input.equals("4")) {
                        do {
                            view.printSaveGameOption();

                            /* For test here */
                            if (testCode != null && testCode == 9)
                                input = "1";
                            /* For test here */

                            else
                                input = view.scanInput();
                            if (input.equals("1"))
                                break;
                            if (input.equals("2"))
                                break;
                            else
                                view.printInvalidMsg();
                        } while (true);

                        if (input.equals("2")) {
                            repeatMenuFlag = true;
                            continue;
                        }

                        view.printRecords();
                        String path = Constant.CWD+"Game records";
                        File directory = new File(path);

                        if (!directory.exists() && !directory.isDirectory())
                            if (directory.mkdir())
                                view.print("Directory for game record is created.");

                        view.print("\nWhat would you like to name your record?");
                        /* For test here */
                        if (testCode != null && testCode == 9)
                            input = "testCode9";
                            /* For test here */

                        else
                            input = view.scanInput();

                        String recordName = input;
                        recordName = recordName.replaceAll("[\"\\\\/:*?<>|]", "").trim();
                        if (isUniqueNameRecords(recordName) && countRecords()<5){
                            saveGame(recordName, game, player);
                        }
                        else{
                            if (countRecords()==5){
                                do {
                                    view.print("Please choose one record to overwrite." );

                                    /* For test here */
                                    if (testCode != null && testCode == 9)
                                        input = "1";
                                        /* For test here */

                                    else
                                        input = view.scanInput();
                                    if (!isInRangeOfRecords(input))
                                        view.printInvalidMsg();
                                }while(!isInRangeOfRecords(input));

                                deleteRecords(Integer.parseInt(input));
                                saveGame(recordName, game, player);
                            }else{
                                do {
                                    view.print("\nThere is a existing record with the same name, do you want to overwrite it? (Y/N)" );

                                    /* For test here */
                                    if (testCode != null && testCode == 9)
                                        input = "Y";
                                        /* For test here */

                                    else
                                        input = view.scanInput();
                                    if (!input.toUpperCase().equals("Y") && !input.toUpperCase().equals("N"))
                                        view.printInvalidMsg();
                                }while(!input.toUpperCase().equals("Y") && !input.toUpperCase().equals("N"));

                                if (input.toUpperCase().equals("Y")){
                                    deleteRecords(recordName);
                                    saveGame(recordName, game, player);
                                }
                            }

                        }
                        repeatMenuFlag = true;


                        /* Back to main menu */
                    } else if (input.equals("5")) {
                        break gameLoop;
                    } else
                        view.printInvalidMsg();

                    /* For test here */
                    if (testCode != null && (testCode == 7 || testCode == 8 || testCode == 9))
                        break gameLoop;
                    /* For test here */

                } while (!input.equals("1") || repeatMenuFlag);

                /* print player's pos after moving */
                view.printDetailedPos(game.getBoard(), player.getPos());

                /* For test here */
                if (testCode!= null && (testCode == 3 || testCode == 4 || testCode == 5 || testCode == 6))
                    break gameLoop;
                /* For test here */



                /* After the move */



                /* Move to next position
                 * Do the action due to the new position */
                int newPlayerPos = player.getPos();
                if (game.getBoard().findSquare(newPlayerPos).getName().equals("Go to Jail")){
                    view.print("You are going to jail(6).");
                    player.setPos(6);
                    player.setInJail(true);
                }

                if (game.getBoard().findSquare(newPlayerPos).getName().equals("Income tax")){
                    int tax;
                    tax = (int) (player.getMoney()*0.1-(player.getMoney()*0.1%10));
                    view.print("You have to pay income tax for 10% of your money. (HKD"+ tax + ")");
                    player.deductMoney(Math.abs(tax), "Tax");
                }

                if (game.getBoard().findSquare(newPlayerPos).getName().equals("Chance")){
                    Random random = new Random();
                    int rand=0;
                    while(rand==0) {
                        rand = (random.nextInt(200 + 300) - 300) / 10 * 10;
                    }
                    if (rand > 0){
                        view.print("You won a bonus of HKD"+ rand);
                        player.addMoney(rand);
                    }
                    else {
                        view.print("You lost HKD" + Math.abs(rand));
                        if (player.isEnoughMoneyToPay(Math.abs(rand))){
                            player.deductMoney(Math.abs(rand), "Fee");
                        }else{
                            view.print("You do not have enough money.");
                            player.deductMoney(Math.abs(rand), "Fee");
                            player.freeProperty();
                            it.remove();
                        }
                    }
                }

                if (game.getBoard().findSquare(newPlayerPos).isProperty()){
                    Square square;
                    square = game.getBoard().findSquare(newPlayerPos);
                    int rent = square.getRent();
                    int price = square.getPrice();
                    Player owner;
                    owner = square.getOwner();
                    /* owned */
                    if (owner!=null){
                        /* The property does not belong to current player */
                        if (player!=owner ){
                            view.print("You have to pay for the rent HKD" + rent);
                            if (player.isEnoughMoneyToPay(rent)){
                                player.deductMoney(Math.abs(rent), "Rent");
                                owner.addMoney(rent);
                            }else{
                                view.print("\nYou do not have enough money.");
                                view.print("The landlord will take over all your remaining money.");
                                player.setMoney(player.getMoney() - Math.abs(rent));
                                owner.addMoney(player.getMoney()+Math.abs(rent));
                                player.freeProperty();
                                it.remove();
                            }
                        }

                    }else{
                        /* The property is available */
                        do {
                            view.print("Do you want to buy this property for HKD" + price +
                                    "\n1 : Purchase" +
                                    "\n2 : No, thank you.");

                            /* For test here */
                            if (testCode!= null && testCode == 10)
                                input = "1";
                            /* For test here */

                            else
                                input = view.scanInput();

                            if (input.equals("1")){
                                /* have enough money to buy */
                                if (player.isEnoughMoneyToPay(price)){
                                    player.purchaseProperty(square);
                                    square.setOwner(player);
                                    view.print("You have successfully owned " + square.getName());
                                }else {
                                    view.print("You have not enough money to purchase.");
                                }
                            }
                        } while (!input.equals("1") && !input.equals("2"));
                    }

                }

                /* For test here */
                if (testCode!= null && testCode == 10)
                    break gameLoop;
                /* For test here */

                /* announce game result when only one player left*/
                if (game.getPlayersList().size() == 1) {
                    view.announceWinners(game.getPlayersList());
                }

            }

            game.setGameRound(game.getGameRound() + 1);
        }

        /* announce game result when game round has passed 100 */
        if (game.getGameRound()==101) {
            int maxMoney = 0;
            ArrayList<Player> winners = new ArrayList<>();

            //check the biggest amount of money
            for (Player player1 : game.getPlayersList()) {
                if (player1.getMoney() > maxMoney)
                    maxMoney = player1.getMoney();
            }

            //check winners
            for (Player player1 : game.getPlayersList()) {
                if (player1.getMoney() == maxMoney)
                    winners.add(player1);
            }

            view.announceWinners(winners);
        }

    }














}

