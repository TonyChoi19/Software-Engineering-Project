import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;

import javax.swing.plaf.synth.SynthTextAreaUI;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class Controller {
    private InputStreamReader isr;
    private BufferedReader br;

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";

    public Controller() {
        this.isr = new InputStreamReader(System.in);
        this.br = new BufferedReader(isr);
    }

    public void start() throws InterruptedException {
        Welcome welcome = new Welcome();
        welcome. playWelcome();

        String input = null;

        do {
            printMainMenu();
            input = scanInput();

            outerLoop:
            switch (input) {
                /* NEW GAME */
                case "1":
                    do {
                        System.out.println("\nPlease enter the number of players: (Minimum=2, Maximum=6)");
                        input = scanInput();
                        if (Integer.parseInt(input) < 2 || Integer.parseInt(input) > 6)
                            printInvalidMsg();
                    } while (Integer.parseInt(input) < 2 || Integer.parseInt(input) > 6);

                    Game game = new Game(Integer.parseInt(input));

                    while(game.getGameRound() <=100 && game.playersList.size() != 1){
                        for( Player player : game.playersList ){
                            System.out.println("\n"+ANSI_YELLOW+"####\t\tRound " + game.getGameRound()  +"\t " + player.getName() +"'s turn\tPosition:" + game.board.findSquare(player.getPos()).getName() + "(" + player.getPos() + ")"+ "\t Balacne(HKD):" + player.getMoney() + "\t\t#### "+ ANSI_RESET +"\n");

                            int step=0;

                            /* Payment option for players in jail */
                            if (player.isHaveChoicePayJail()){
                                do {
                                    System.out.println("Do you want to pay a fine of HKD 150 to get free? (Y/N)");
                                    input = scanInput();
                                    if (!input.toUpperCase().equals("Y") && !input.toUpperCase().equals("N"))
                                        printInvalidMsg();
                                }while(!input.toUpperCase().equals("Y") && !input.toUpperCase().equals("N"));

                                /* pay fine to go */
                                if (input.toUpperCase().equals("Y") && player.isEnoughMoneyToPay(150)){
                                    player.deductMoney(150, "Fine");
                                    player.releaseFromJail("Fine");
                                }

                                /* not enough money to pay fine */
                                if (input.toUpperCase().equals("Y") && !player.isEnoughMoneyToPay(150)){
                                    System.out.println("Sorry, you don't have enough money to pay");
                                }

                            }

                            boolean repeatMenuFlag;
                            do {
                                repeatMenuFlag = false;
                                printMainGameOption();
                                input = scanInput();

                                if (input.equals("1")) {
                                    /* player throws dice */
                                    step = game.dice.throwDice();
                                    game.dice.printValue();

                                    /* player in jail gets double */
                                    if (game.dice.isDouble() && player.isInJail()) {
                                        player.releaseFromJail("Double");
                                    } else {
                                        /* player in jail (not third turn) doesn't throw a double */
                                        if (player.isHaveChoicePayJail()) {
                                            player.setInJailCount(player.getInJailCount() + 1);
                                        }
                                        /* player in jail (third turn) doesn't throw a double */
                                        if (player.isInJail() && player.getInJailCount() == 3) {
                                            System.out.println("This is your third turn in jail. You have to pay HKD150 to get out of jail.");
                                            if (player.isEnoughMoneyToPay(150)) {
                                                player.deductMoney(150, "Fine");
                                                player.releaseFromJail("Fine");
                                            } else {
                                                System.out.println("You do not have enough money.");
                                                player.deductMoney(150, "Fine");
                                                game.endPlayer(player);
                                            }
                                        }
                                    }

                                    if (!player.isInJail()) {
                                        player.move(step);
                                    }

                                } else if (input.equals("2")) {
                                    System.out.println("\nRound " + game.getGameRound() + "\t " + player.getName() + "'s turn\tPosition:" + game.board.findSquare(player.getPos()).getName() + "(" + player.getPos() + ")" + "\t Balacne(HKD):" + player.getMoney());
                                    System.out.println("Your property:");
                                    if (player.getProperties().isEmpty())
                                        System.out.println("NONE");
                                    else {
                                        System.out.printf("%-5s %-20s %-8s %-8s\n", "POS", "NAME", "PRICE", "RENT");
                                        for (Square property : player.getProperties()) {
                                            System.out.printf("%-5s %-20s %-8s %-8s\n", property.getPos(), property.getName(), property.getPrice(), property.getRent());
                                        }
                                    }
                                    System.out.println(" ");
                                } else if (input.equals("3")) {
                                    game.board.printBoard();
                                } else if (input.equals("4")) {
                                    //Save record
                                    do {
                                        printSaveGameOption();
                                        input = scanInput();
                                        if (input.equals("1"))
                                            break;
                                        if (input.equals("2")) {
                                            break;
                                        } else
                                            printInvalidMsg();
                                    } while (!input.equals("1") && !input.equals("2"));

                                    if (input.equals("2")) {
                                        repeatMenuFlag = true;
                                        continue;
                                    }

                                    printRecords();
                                    File directory = new File(".\\game records");
                                    if (!directory.exists() && !directory.isDirectory())
                                        if (directory.mkdir())
                                            System.out.println("Directory for game record is created.");

                                    System.out.println("\nWhat would you like to name your record?");
                                    input = scanInput();

                                    String recordName = input;
                                    if (isUniqueNameRecords(recordName) && countRecords()<5){
                                        saveGame(recordName, game, player);
                                        repeatMenuFlag = true;
                                    }
                                    else{
                                        if (countRecords()==5){
                                            do {
                                                System.out.println("Please choose one record to overwrite." );
                                                input = scanInput();
                                                if (!isVaildForSaveInput(input))
                                                    printInvalidMsg();
                                            }while(!isVaildForSaveInput(input));

                                            deleteRecords(Integer.parseInt(input));
                                            saveGame(recordName, game, player);
                                            repeatMenuFlag = true;
                                        }else{
                                            do {
                                                System.out.println("There is a existing record with the same name, do you want to overwrite it? (Y/N)" );
                                                input = scanInput();
                                                if (!input.toUpperCase().equals("Y") && !input.toUpperCase().equals("N"))
                                                    printInvalidMsg();
                                            }while(!input.toUpperCase().equals("Y") && !input.toUpperCase().equals("N"));

                                            if (input.toUpperCase().equals("Y")){
                                                deleteRecords(recordName);
                                                saveGame(recordName, game, player);
                                                repeatMenuFlag = true;
                                            }else{
                                                repeatMenuFlag = true;
                                            }
                                        }

                                    }


                                } else if (input.equals("5")) {
                                    break outerLoop;
                                } else
                                    printInvalidMsg();

                            } while (!input.equals("1") || repeatMenuFlag);

                            /* print player's pos after moving */
                            game.printDetailedPos(player.getPos());

                            /* move to next point */
                            int newPlayerPos = player.getPos();
                            if (game.board.findSquare(newPlayerPos).getName().equals("Go to Jail")){
                                System.out.println("You are going to jail(6).");
                                player.setPos(6);
                                player.setInJail(true);
                            }

                            if (game.board.findSquare(newPlayerPos).getName().equals("Income tax")){
                                int tax;
                                tax = (int) ((player.getMoney()*0.01/10)*10);
                                System.out.println("You have to pay income tax for 10% of your money. (HKD"+ tax + ")");
                                player.deductMoney(tax, "Tax");
                            }

                            if (game.board.findSquare(newPlayerPos).getName().equals("Chance")){
                                Random random = new Random();
                                int rand=0;
                                while(rand==0) {
                                    rand = (random.nextInt(200 + 300) - 300) / 10 * 10;
                                }
                                if (rand > 0)
                                    System.out.println("You won a bonus of HKD"+ rand);
                                else {
                                    System.out.println("You lost HKD" + rand);
                                    if (player.isEnoughMoneyToPay(rand)){
                                        player.deductMoney(rand, "Fee");
                                    }else{
                                        System.out.println("You do not have enough money.");
                                        player.deductMoney(rand, "Fee");
                                        game.endPlayer(player);
                                    }
                                }
                            }

                            if (game.board.findSquare(newPlayerPos).isProperty()){
                                Square square;
                                square = game.board.findSquare(newPlayerPos);
                                int rent = square.getRent();
                                int price = square.getPrice();
                                Player owner;
                                owner = square.belongsTo();
                                /* owned */
                                if (owner!=null){
                                    /* The property does not belong to current player */
                                    if (player!=owner ){
                                        System.out.println("You have to pay for the rent HKD" + rent);
                                        if (player.isEnoughMoneyToPay(rent)){
                                            player.deductMoney(rent, "Rent");
                                            owner.addMoney(rent);
                                        }else{
                                            System.out.println("You do not have enough money.");
                                            System.out.println("The landlord will take over all your remaining money.");
                                            int moneyBeforeEnd = player.getMoney();
                                            player.deductMoney(rent, "Rent");
                                            owner.addMoney(player.getMoney());
                                            game.endPlayer(player);
                                        }
                                    }

                                }else{  /* The property is available */
                                    do {
                                        System.out.println("Do you want to buy this property for HKD" + price +
                                                "\n1 : Purchase" +
                                                "\n2 : No, thank you.");
                                        input = scanInput();

                                        if (input.equals("1")){
                                            /* have enough money to buy */
                                            if (player.isEnoughMoneyToPay(price)){
                                                player.purchaseProperty(square);
                                                square.setOwner(player);
                                                System.out.println("You have successfully owned " + square.getName());
                                            }else {
                                                System.out.println("You have not enough money to purchase.");
                                            }
                                        }
                                    } while (!input.equals("1") && !input.equals("2"));
                                }
                                System.out.println();
                            }


                        }
                        game.setGameRound(game.getGameRound() + 1);
                    }
                    break;

                case "2":
                    printRecords();
                    int chosenRecordNo=1;
                    do {
                        System.out.println("Which record would you want to load?");
                        input = scanInput();
                        if (!isVaildForSaveInput(input)){
                            printInvalidMsg();
                            continue;
                        }

                        chosenRecordNo = Integer.parseInt(input);

                        GameRecord  loadedGameRecord = loadGame(findRecords(chosenRecordNo));
                        Game loadedGame = loadedGameRecord.getGame();
                        int loadedGameRound;
                        loadedGameRound = loadedGame.getGameRound();
                        boolean playerFoundInFirstRound = false;

                        while(loadedGame.getGameRound() <=100 && loadedGame.playersList.size() != 1){
                            for( Player player : loadedGame.playersList ){
                                if (player!=loadedGameRecord.getPlayerTurn() && loadedGame.getGameRound()==loadedGameRound && !playerFoundInFirstRound){
                                    continue;
                                }
                                else
                                    playerFoundInFirstRound = true;

                                System.out.println("\n"+ANSI_YELLOW+"####\t\tRound " + loadedGame.getGameRound()  +"\t " + player.getName() +"'s turn\tPosition:" + loadedGame.board.findSquare(player.getPos()).getName() + "(" + player.getPos() + ")"+ "\t Balacne(HKD):" + player.getMoney() + "\t\t#### "+ ANSI_RESET +"\n");

                                int step=0;

                                /* Payment option for players in jail */
                                if (player.isHaveChoicePayJail()){
                                    do {
                                        System.out.println("Do you want to pay a fine of HKD 150 to get free? (Y/N)");
                                        input = scanInput();
                                        if (!input.toUpperCase().equals("Y") && !input.toUpperCase().equals("N"))
                                            printInvalidMsg();
                                    }while(!input.toUpperCase().equals("Y") && !input.toUpperCase().equals("N"));

                                    /* pay fine to go */
                                    if (input.toUpperCase().equals("Y") && player.isEnoughMoneyToPay(150)){
                                        player.deductMoney(150, "Fine");
                                        player.releaseFromJail("Fine");
                                    }

                                    /* not enough money to pay fine */
                                    if (input.toUpperCase().equals("Y") && !player.isEnoughMoneyToPay(150)){
                                        System.out.println("Sorry, you don't have enough money to pay");
                                    }

                                }


                                boolean repeatMenuFlag;
                                do {
                                    repeatMenuFlag = false;
                                    printMainGameOption();
                                    input= scanInput();

                                    if (input.equals("1")){
                                        /* player throws dice */
                                        step = loadedGame.dice.throwDice();
                                        loadedGame.dice.printValue();

                                        /* player in jail gets double */
                                        if (loadedGame.dice.isDouble() && player.isInJail()){
                                            player.releaseFromJail("Double");
                                        }else{
                                            /* player in jail (not third turn) doesn't throw a double */
                                            if (player.isHaveChoicePayJail()){
                                                player.setInJailCount(player.getInJailCount() + 1);
                                            }
                                            /* player in jail (third turn) doesn't throw a double */
                                            if (player.isInJail() && player.getInJailCount()==3){
                                                System.out.println("This is your third turn in jail. You have to pay HKD150 to get out of jail.");
                                                if (player.isEnoughMoneyToPay(150)){
                                                    player.deductMoney(150, "Fine");
                                                    player.releaseFromJail("Fine");
                                                }else{
                                                    System.out.println("You do not have enough money.");
                                                    player.deductMoney(150, "Fine");
                                                    loadedGame.endPlayer(player);
                                                }
                                            }
                                        }

                                        if(!player.isInJail()){
                                            player.move(step);
                                        }

                                    }


                                    else if (input.equals("2")){
                                        System.out.println("\nRound " + loadedGame.getGameRound()  +"\t " + player.getName() +"'s turn\tPosition:" + loadedGame.board.findSquare(player.getPos()).getName() + "(" + player.getPos() + ")"+ "\t Balacne(HKD):" + player.getMoney());
                                        System.out.println("Your property:");
                                        if (player.getProperties().isEmpty())
                                            System.out.println("NONE");
                                        else {
                                            System.out.printf("%-5s %-20s %-8s %-8s\n","POS","NAME", "PRICE", "RENT");
                                            for (Square property : player.getProperties()){
                                                System.out.printf("%-5s %-20s %-8s %-8s\n",property.getPos(),property.getName(), property.getPrice(), property.getRent());
                                            }
                                        }
                                        System.out.println(" ");
                                    }
                                    else if (input.equals("3")){
                                        loadedGame.board.printBoard();
                                    }
                                    else if (input.equals("4")){
                                        //Save record

                                        do {
                                            printSaveGameOption();
                                            input = scanInput();
                                            if (input.equals("1"))
                                                break;
                                            if (input.equals("2")) {
                                                break;
                                            } else
                                                printInvalidMsg();
                                        } while (!input.equals("1") && !input.equals("2"));

                                        if (input.equals("2")) {
                                            repeatMenuFlag = true;
                                            continue;
                                        }

                                        printRecords();
                                        File directory = new File(".\\game records");
                                        if (!directory.exists() && !directory.isDirectory())
                                            if (directory.mkdir())
                                                System.out.println("Directory for game record is created.");
                                        System.out.println("\nWhat would you like to name your record?");
                                        input = scanInput();

                                        String recordName = input;
                                        if (isUniqueNameRecords(recordName) && countRecords()<5){
                                            saveGame(recordName, loadedGame, player);
                                            repeatMenuFlag = true;
                                        }
                                        else{
                                            if (countRecords()==5){
                                                do {
                                                    System.out.println("Please choose one record to overwrite." );
                                                    input = scanInput();
                                                    if (!isVaildForSaveInput(input))
                                                        printInvalidMsg();
                                                }while(!isVaildForSaveInput(input));

                                                deleteRecords(Integer.parseInt(input));
                                                saveGame(recordName, loadedGame, player);
                                                repeatMenuFlag = true;
                                            }else{
                                                do {
                                                    System.out.println("There is a existing record with the same name, do you want to overwrite it? (Y/N)" );
                                                    input = scanInput();
                                                    if (!input.toUpperCase().equals("Y") && !input.toUpperCase().equals("N"))
                                                        printInvalidMsg();
                                                }while(!input.toUpperCase().equals("Y") && !input.toUpperCase().equals("N"));

                                                if (input.toUpperCase().equals("Y")){
                                                    deleteRecords(recordName);
                                                    saveGame(recordName, loadedGame, player);
                                                    repeatMenuFlag = true;
                                                }else{
                                                    repeatMenuFlag = true;
                                                }
                                            }

                                        }


                                    } else if (input.equals("5")) {
                                        break outerLoop;
                                    } else
                                        printInvalidMsg();

                                } while (!input.equals("1") || repeatMenuFlag);

                                /* print player's pos after moving */
                                loadedGame.printDetailedPos(player.getPos());

                                /* move to next point */
                                int newPlayerPos = player.getPos();
                                if (loadedGame.board.findSquare(newPlayerPos).getName().equals("Go to Jail")){
                                    System.out.println("You are going to jail(6).");
                                    player.setPos(6);
                                    player.setInJail(true);
                                }

                                if (loadedGame.board.findSquare(newPlayerPos).getName().equals("Income tax")){
                                    int tax;
                                    tax = (int) ((player.getMoney()*0.01/10)*10);
                                    System.out.println("You have to pay income tax for 10% of your money. (HKD"+ tax + ")");
                                    player.deductMoney(tax, "Tax");
                                }

                                if (loadedGame.board.findSquare(newPlayerPos).getName().equals("Chance")){
                                    Random random = new Random();
                                    int rand=0;
                                    while(rand==0) {
                                        rand = (random.nextInt(200 + 300) - 300) / 10 * 10;
                                    }
                                    if (rand > 0)
                                        System.out.println("You won a bonus of HKD"+ rand);
                                    else {
                                        System.out.println("You lost HKD" + rand);
                                        if (player.isEnoughMoneyToPay(rand)){
                                            player.deductMoney(rand, "Fee");
                                        }else{
                                            System.out.println("You do not have enough money.");
                                            player.deductMoney(rand, "Fee");
                                            loadedGame.endPlayer(player);
                                        }
                                    }
                                }

                                if (loadedGame.board.findSquare(newPlayerPos).isProperty()){
                                    Square square;
                                    square = loadedGame.board.findSquare(newPlayerPos);
                                    int rent = square.getRent();
                                    int price = square.getPrice();
                                    Player owner;
                                    owner = square.belongsTo();
                                    /* owned */
                                    if (owner!=null){
                                        /* The property does not belong to current player */
                                        if (player!=owner ){
                                            System.out.println("You have to pay for the rent HKD" + rent);
                                            if (player.isEnoughMoneyToPay(rent)){
                                                player.deductMoney(rent, "Rent");
                                                owner.addMoney(rent);
                                            }else{
                                                System.out.println("You do not have enough money.");
                                                System.out.println("The landlord will take over all your remaining money.");
                                                int moneyBeforeEnd = player.getMoney();
                                                player.deductMoney(rent, "Rent");
                                                owner.addMoney(player.getMoney());
                                                loadedGame.endPlayer(player);
                                            }
                                        }

                                    }else{  /* The property is available */
                                        do {
                                            System.out.println("Do you want to buy this property for HKD" + price +
                                                    "\n1 : Purchase" +
                                                    "\n2 : No, thank you.");
                                            input = scanInput();

                                            if (input.equals("1")){
                                                /* have enough money to buy */
                                                if (player.isEnoughMoneyToPay(price)){
                                                    player.purchaseProperty(square);
                                                    square.setOwner(player);
                                                    System.out.println("You have successfully owned " + square.getName());
                                                }else {
                                                    System.out.println("You have not enough money to purchase.");
                                                }
                                            }
                                        } while (!input.equals("1") && !input.equals("2"));
                                    }
                                    System.out.println();
                                }


                            }
                            loadedGame.setGameRound(loadedGame.getGameRound() + 1);
                        }


                    }while(!isVaildForSaveInput(input));

                    break;

                case "3":
                    end();
                    break;

                default:
                    System.out.println("Sorry, please enter the correct number.");
                    break;
            }
        }while(!input.equals("1") && !input.equals("2") && !input.equals("3") );

    }

    public void end(){
        System.out.println("*** \tThank you for playing. Bye Bye! \t***");
        System.exit(0);
    }

    /* read next line (user's input) */
    public String scanInput(){
        inputField();
        String input = "";
        try{
            input = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return input;
    }

    /* To place the user input */
    public void inputField(){
        /* coloring of the user input line */
        System.out.print(ANSI_GREEN + "-> " +ANSI_RESET);
    }

    public void printMainMenu(){
        System.out.println("\n***\t\tGAME MENU\t\t***" +
                "\n1. \t NEW GAME" +
                "\n2. \t LOAD GAME" +
                "\n3. \t EXIT" +
                "\nPlease enter the number to choose");
    }

    public void printMainGameOption(){
        System.out.println("***\t\tGAME OPTION\t\t***" +
                "\n1. \t Throw the dice" +
                "\n2. \t Check status" +
                "\n3. \t Print Board" +
                "\n4. \t Save" +
                "\n5. \t Back to main menu" +
                "\nPlease enter the number to choose");
    }

    public void printSaveGameOption(){
        System.out.println("***\t\tSAVE OPTION\t\t***" +
                "\n1. \t Save record" +
                "\n2. \t Back" +
                "\nPlease enter the number to choose");
    }

    public void printInvalidMsg(){
        System.out.println("Invalid input, please try again.");
    }


    public void printRecords(){
        File recordsDir = new File(".\\game records\\");
        File[] directoryListing = recordsDir.listFiles();
        ArrayList<File> gameRecords = new ArrayList<>();
        if (directoryListing!=null) {
            for (File file : directoryListing) {
                if (file.getName().endsWith(".ser"))
                    gameRecords.add(file);
            }

            System.out.println("\n***\t\tGAME RECORDS\t\t***\t(5 RECORDS MAXIMUM)");
            for (int i =0;i<gameRecords.size();i++){
                String name = gameRecords.get(i).getName();
                long time = gameRecords.get(i).lastModified();
                Date date = new Date(time);
                System.out.println(
                        i+1 + ". \t Name: " + name +
                        "  \t Created Time: " + date);
            }
        }else{
            System.out.println("There is no record saved.");
        }
    }

    public boolean isUniqueNameRecords(String name){
        File recordsDir = new File(".\\game records\\");
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

    public int countRecords(){
        File recordsDir = new File(".\\game records\\");
        File[] directoryListing = recordsDir.listFiles();
        if (directoryListing!=null) {
            return directoryListing.length;
        }else
            return 0;
    }

    public void deleteRecords(int number){
        File recordsDir = new File(".\\game records\\");
        File[] directoryListing = recordsDir.listFiles();
        if (directoryListing!=null) {
            for (int i=0; i<directoryListing.length;i++) {
                if (i == (number-1))
                    if(directoryListing[i].delete())
                        System.out.println("The selected record is deleted.");
            }
        }
    }

    public void deleteRecords(String name){
        File recordsDir = new File(".\\game records\\");
        File[] directoryListing = recordsDir.listFiles();
        if (directoryListing!=null) {
            for (File file : directoryListing) {
                if (file.getName().equals(name+".ser")) {
                    if (file.delete())
                        System.out.println("The selected record is deleted.");
                }
            }
        }
    }

    public String findRecords(int number){
        File recordsDir = new File(".\\game records\\");
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

    public boolean isExistDir(String name){
        File dir = new File(".\\"+ name);
        if (dir.exists())
            return true;
        else
            return false;
    }


    public void printLoadGameOption(){
        System.out.println("***\t\tLOAD OPTION\t\t***" +
                "\n1. \t Load record" +
                "\n2. \t Back" +
                "\nPlease enter the number to choose");
    }

    public boolean isNumeric(String input){
        return input != null && input.matches("[0-9.]+");
    }

    public boolean isVaildForSaveInput(String input){
        if (isNumeric(input)){
            return  Integer.parseInt(input)>=1 && Integer.parseInt(input)<=countRecords();
        }else
            return false;
    }

    public void saveGame(String name, Game game, Player player) {
        GameRecord recordToSave = new GameRecord(name, game, player);
        try{
            FileOutputStream fos = new FileOutputStream(".\\game records\\"+name+".ser");
            ObjectOutputStream out =new ObjectOutputStream(fos);
            out.writeObject(recordToSave);
            out.close();
            fos.close();
            System.out.println("File saved successfully");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public GameRecord loadGame(String fileName){
        GameRecord recordToLoad = new GameRecord();
        try{
            FileInputStream fis = new FileInputStream(".\\game records\\"+fileName+".ser");
            ObjectInputStream in =new ObjectInputStream(fis);
            recordToLoad = (GameRecord)in.readObject();
            in.close();
            fis.close();
            System.out.println(fileName+".ser loaded successfully.");
        } catch (IOException | ClassNotFoundException e) {
            printInvalidMsg();
            return null;
        }

        return recordToLoad;
    }
















}

