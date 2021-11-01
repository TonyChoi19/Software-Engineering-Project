import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;

public class Controller {
    //For reading input
    private InputStreamReader isr;
    private BufferedReader br;

    public Controller() {
        this.isr = new InputStreamReader(System.in);
        this.br = new BufferedReader(isr);
    }

    public void start() throws InterruptedException, URISyntaxException {
        //Play styled game intro
        Welcome welcome = new Welcome();
        welcome. playWelcome();

        String input;

        do {
            printMainMenu();
            input = scanInput();

            outerLoop:
            switch (input) {
                /* NEW GAME */
                case "1":
                    /* Set the number of players for new game */
                    do {
                        System.out.println("\nPlease enter the number of players: (Minimum=2, Maximum=6)");
                        input = scanInput();
                        if (Integer.parseInt(input) < 2 || Integer.parseInt(input) > 6) {
                            printInvalidMsg();
                        }else
                            break;
                    } while (true);


                    Game game = new Game(Integer.parseInt(input));

                    /* Loop until the game rpund has reached 100 / only one player left */
                    while(game.getGameRound() <=100 && game.getPlayersList().size() != 1){
                        Iterator<Player> it = game.getPlayersList().iterator();
                        while( it.hasNext()){
                            Player player = it.next();
                            System.out.println("\n"+Constant.ANSI_YELLOW+"####\t\tRound " + game.getGameRound()  +"\t " + player.getName() +"'s turn\tPosition:" + game.board.findSquare(player.getPos()).getName() + "(" + player.getPos() + ")"+ "\t Balance(HKD):" + player.getMoney() + "\t\t#### "+ Constant.ANSI_RESET);

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

                                /* Throw the dice */
                                if (input.equals("1")) {
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
                                        /* player in jail (third turn) doesn't throw a double --> must to pay the fine */
                                        if (player.isInJail() && player.getInJailCount() == 3) {
                                            System.out.println("This is your third turn in jail. You have to pay HKD150 to get out of jail.");
                                            if (player.isEnoughMoneyToPay(150)) {
                                                player.deductMoney(150, "Fine");
                                                player.releaseFromJail("Fine");
                                            } else {
                                                System.out.println("You do not have enough money.");
                                                player.deductMoney(150, "Fine");
                                                player.freeProperty();
                                            }
                                        }
                                    }

                                    if (!player.isInJail()) {
                                        player.move(step);
                                    }
                                /* Check all the info of the player */
                                } else if (input.equals("2")) {
                                    System.out.println("\nRound " + game.getGameRound() + "\t " + player.getName() + "'s turn\tPosition:" + game.board.findSquare(player.getPos()).getName() + "(" + player.getPos() + ")" + "\t Balance(HKD):" + player.getMoney());
                                    System.out.println("Your property:");
                                    if (player.getProperties().isEmpty())
                                        System.out.println("NONE");
                                    else {
                                        System.out.printf("%-5s %-20s %-8s %-8s\n", "POS", "NAME", "PRICE", "RENT");
                                        for (Square property : player.getProperties()) {
                                            System.out.printf("%-5s %-20s %-8s %-8s\n", property.getPos(), property.getName(), property.getPrice(), property.getRent());
                                        }
                                    }

                                /* Print the details of the board */
                                } else if (input.equals("3")) {
                                    game.board.printBoard();

                                /* Save a record */
                                } else if (input.equals("4")) {
                                    do {
                                        printSaveGameOption();
                                        input = scanInput();
                                        if (input.equals("1"))
                                            break;
                                        if (input.equals("2"))
                                            break;
                                        else
                                            printInvalidMsg();
                                    } while (true);

                                    if (input.equals("2")) {
                                        repeatMenuFlag = true;
                                        continue;
                                    }

                                    printRecords();
                                    String path = Constant.CWD+"game records";
                                    File directory = new File(path);

                                    if (!directory.exists() && !directory.isDirectory())
                                        if (directory.mkdir())
                                            System.out.println("Directory for game record is created.");

                                    System.out.println("\nWhat would you like to name your record?");
                                    input = scanInput();

                                    String recordName = input;
                                    if (isUniqueNameRecords(recordName) && countRecords()<5){
                                        saveGame(recordName, game, player);
                                    }
                                    else{
                                        if (countRecords()==5){
                                            do {
                                                System.out.println("Please choose one record to overwrite." );
                                                input = scanInput();
                                                if (!isInRangeOfRecords(input))
                                                    printInvalidMsg();
                                            }while(!isInRangeOfRecords(input));

                                            deleteRecords(Integer.parseInt(input));
                                            saveGame(recordName, game, player);
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
                                            }
                                        }

                                    }
                                    repeatMenuFlag = true;


                                /* Back to main menu */
                                } else if (input.equals("5")) {
                                    break outerLoop;
                                } else
                                    printInvalidMsg();

                            } while (!input.equals("1") || repeatMenuFlag);

                            /* print player's pos after moving */
                            game.printDetailedPos(player.getPos());


                            /*
                            After the move
                             */


                            /* Move to next position
                            * Do the action due to the new position */
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
                                player.deductMoney(Math.abs(tax), "Tax");
                            }

                            if (game.board.findSquare(newPlayerPos).getName().equals("Chance")){
                                Random random = new Random();
                                int rand=0;
                                while(rand==0) {
                                    rand = (random.nextInt(200 + 300) - 300) / 10 * 10;
                                }
                                if (rand > 0){
                                    System.out.println("You won a bonus of HKD"+ rand);
                                    player.addMoney(rand);
                                }
                                else {
                                    System.out.println("You lost HKD" + Math.abs(rand));
                                    if (player.isEnoughMoneyToPay(Math.abs(rand))){
                                        player.deductMoney(Math.abs(rand), "Fee");
                                    }else{
                                        System.out.println("You do not have enough money.");
                                        player.deductMoney(Math.abs(rand), "Fee");
                                        player.freeProperty();
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
                                            player.deductMoney(Math.abs(rent), "Rent");
                                            owner.addMoney(rent);
                                        }else{
                                            System.out.println("\nYou do not have enough money.");
                                            System.out.println("The landlord will take over all your remaining money.");
                                            player.setMoney(player.getMoney() - Math.abs(rent));
                                            owner.addMoney(player.getMoney()+Math.abs(rent));
                                            player.freeProperty();
                                            it.remove();
                                        }
                                    }

                                }else{
                                    /* The property is available */
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

                            }


                        }
                        game.setGameRound(game.getGameRound() + 1);
                    }

                    /* announce game result when game ends */
                    if (game.getGameRound()==101 || game.getPlayersList().size() == 1){
                        if (game.getPlayersList().size() == 1)
                            System.out.println("\n"+Constant.ANSI_GREEN+"####\t\t"+game.getPlayersList().get(0).getName()+" is the winner!!!\t\t####"+Constant.ANSI_RESET);
                        else{
                            System.out.println("\n"+Constant.ANSI_GREEN+"####\t\tTie!!!\t\t####");
                            for (Player player : game.getPlayersList()){
                                System.out.print(player.getName()+" ");
                            }
                            System.out.println("are the winners!!!\n"+Constant.ANSI_RESET);
                        }
                        input = "999";
                        break;
                    }
                    break;




                /* Load game records */
                case "2":
                    printRecords();

                    /* Directory not found */
                    File gameRecordsDir = new File(Constant.CWD+"game records");
                    if (!gameRecordsDir.exists() || countRecords()==0){
                        input = "999";
                        break;
                    }


                    /* Load record and start playing */
                    int chosenRecordNo;
                    do {
                        System.out.println("\nWhich record would you want to load? (Type \"back\" to return)");
                        input = scanInput();
                        if (!isInRangeOfRecords(input)){
                            if (input.toUpperCase().equals("BACK"))
                                break;
                            printInvalidMsg();
                            continue;
                        }

                        chosenRecordNo = Integer.parseInt(input);

                        GameRecord  loadedGameRecord = loadGame(findRecords(chosenRecordNo));
                        Game loadedGame = loadedGameRecord.getGame();
                        int loadedGameRound;
                        loadedGameRound = loadedGame.getGameRound();
                        boolean playerFoundInFirstRound = false;

                        while(loadedGame.getGameRound() <=100 && loadedGame.getPlayersList().size() != 1){
                            Iterator<Player> it = loadedGame.getPlayersList().iterator();
                            while( it.hasNext()){
                                Player player = it.next();
                                if (player!=loadedGameRecord.getPlayerTurn() && loadedGame.getGameRound()==loadedGameRound && !playerFoundInFirstRound){
                                    continue;
                                }
                                else
                                    playerFoundInFirstRound = true;

                                System.out.println("\n"+Constant.ANSI_YELLOW+"####\t\tRound " + loadedGame.getGameRound()  +"\t " + player.getName() +"'s turn\tPosition:" + loadedGame.board.findSquare(player.getPos()).getName() + "(" + player.getPos() + ")"+ "\t Balance(HKD):" + player.getMoney() + "\t\t#### "+ Constant.ANSI_RESET);

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
                                                    player.freeProperty();
                                                }
                                            }
                                        }

                                        if(!player.isInJail()){
                                            player.move(step);
                                        }

                                    }


                                    else if (input.equals("2")){
                                        System.out.println("\nRound " + loadedGame.getGameRound()  +"\t " + player.getName() +"'s turn\tPosition:" + loadedGame.board.findSquare(player.getPos()).getName() + "(" + player.getPos() + ")"+ "\t Balance(HKD):" + player.getMoney());
                                        System.out.println("Your property:");
                                        if (player.getProperties().isEmpty())
                                            System.out.println("NONE");
                                        else {
                                            System.out.printf("%-5s %-20s %-8s %-8s\n","POS","NAME", "PRICE", "RENT");
                                            for (Square property : player.getProperties()){
                                                System.out.printf("%-5s %-20s %-8s %-8s\n",property.getPos(),property.getName(), property.getPrice(), property.getRent());
                                            }
                                        }

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
                                            else if (input.equals("2"))
                                                break;
                                             else
                                                printInvalidMsg();
                                        } while (true);

                                        if (input.equals("2")) {
                                            repeatMenuFlag = true;
                                            continue;
                                        }

                                        printRecords();
                                        File directory = new File(Constant.CWD+"game records");
                                        if (!directory.exists() && !directory.isDirectory())
                                            if (directory.mkdir())
                                                System.out.println("Directory for game record is created.");
                                        System.out.println("\nWhat would you like to name your record?");
                                        input = scanInput();

                                        String recordName = input;
                                        if (isUniqueNameRecords(recordName) && countRecords()<5){
                                            saveGame(recordName, loadedGame, player);
                                        }
                                        else{
                                            if (countRecords()==5){
                                                do {
                                                    System.out.println("Please choose one record to overwrite." );
                                                    input = scanInput();
                                                    if (!isInRangeOfRecords(input))
                                                        printInvalidMsg();
                                                }while(!isInRangeOfRecords(input));

                                                deleteRecords(Integer.parseInt(input));
                                                saveGame(recordName, loadedGame, player);
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
                                                }
                                            }

                                        }
                                        repeatMenuFlag = true;


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
                                    player.deductMoney(Math.abs(tax), "Tax");
                                }

                                if (loadedGame.board.findSquare(newPlayerPos).getName().equals("Chance")){
                                    Random random = new Random();
                                    int rand=0;
                                    while(rand==0) {
                                        rand = (random.nextInt(200 + 300) - 300) / 10 * 10;
                                    }
                                    if (rand > 0){
                                        System.out.println("You won a bonus of HKD"+ rand);
                                        player.addMoney(rand);
                                    }
                                    else {
                                        System.out.println("You lost HKD" + Math.abs(rand));
                                        if (player.isEnoughMoneyToPay(Math.abs(rand))){
                                            player.deductMoney(Math.abs(rand), "Fee");
                                        }else{
                                            System.out.println("You do not have enough money.");
                                            player.deductMoney(Math.abs(rand), "Fee");
                                            player.freeProperty();
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
                                            if (player.isEnoughMoneyToPay(Math.abs(rent))){
                                                player.deductMoney(Math.abs(rent), "Rent");
                                                owner.addMoney(Math.abs(rent));
                                            }else{
                                                System.out.println("\nYou do not have enough money.");
                                                System.out.println("The landlord will take over all your remaining money.");
                                                int moneyBeforeEnd = player.getMoney();
                                                player.setMoney(player.getMoney() - Math.abs(rent));
                                                owner.addMoney(player.getMoney()+Math.abs(rent));
                                                player.freeProperty();
                                                it.remove();
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
                                }

                            }
                            loadedGame.setGameRound(loadedGame.getGameRound() + 1);
                        }

                        if (loadedGame.getGameRound()==101 || loadedGame.getPlayersList().size() == 1){
                            if (loadedGame.getPlayersList().size() == 1)
                                System.out.println("\n"+Constant.ANSI_GREEN+"####\t\t"+loadedGame.getPlayersList().get(0).getName()+" is the winner!!!\t\t####"+Constant.ANSI_RESET);
                            else{
                                System.out.println("\n"+Constant.ANSI_GREEN+"####\t\tTie!!!\t\t####");
                                for (Player player : loadedGame.getPlayersList()){
                                    System.out.print(player.getName()+" ");
                                }
                                System.out.println("are the winners!!!\n"+Constant.ANSI_RESET);
                            }
                            input = "999";
                            break;
                        }


                    }while(!isInRangeOfRecords(input));

                    break;



                /* Manage game records
                * (View and delete) */
                case "3":
                    printRecords();
                    File directory = new File(Constant.CWD+"game records");
                    if (directory.exists() && countRecords()!=0){
                        do {
                            System.out.println("\nWhich record would you like to delete? (Type \"back\" to return)");
                            input = scanInput();
                            if (isInRangeOfRecords(input)) {
                                deleteRecords(Integer.parseInt(input));
                                break;
                            }else if(input.toUpperCase().equals("BACK")){
                                break;
                            }else{
                                printInvalidMsg();
                            }
                        }while (!isInRangeOfRecords(input));
                    }
                    input = "999";
                    break;

                case "4":
                    end();
                    break;

                default:
                    System.out.println("Sorry, please enter the correct number.");
                    break;
            }
        }while(!input.equals("1") && !input.equals("2") && !input.equals("3") && !input.equals("4") );

    }


    /**
     * Play "Thank you" and close application
     */
    public void end(){
        System.out.println("*** \tThank you for playing. Bye Bye! \t***");
        System.exit(0);
    }


    /**
     * Read next line (user's input)
     *
     * @return  input   the string of player's input
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
    public void printRecords() throws URISyntaxException {

        File recordsDir = new File(Constant.CWD+"game records/");
        File[] directoryListing = recordsDir.listFiles();
        ArrayList<File> gameRecords = new ArrayList<>();

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
     * Distinguish the name if it is used
     *
     * @param name  the typed name for the record
     * @return      True if unique, False if unique
     */
    public boolean isUniqueNameRecords(String name){
        File recordsDir = new File(Constant.CWD+"game records/");
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
     * @return the number of saved records
     */
    public int countRecords(){
        File recordsDir = new File(Constant.CWD+"game records/");
        File[] directoryListing = recordsDir.listFiles();
        if (directoryListing!=null) {
            return directoryListing.length;
        }else
            return 0;
    }


    /**
     * Delete saved record
     *
     * @param number    the number that represents the certain record
     */
    public void deleteRecords(int number){
        File recordsDir = new File(Constant.CWD+"game records/");
        File[] directoryListing = recordsDir.listFiles();
        if (directoryListing!=null) {
            for (int i=0; i<directoryListing.length;i++) {
                if (i == (number-1))
                    if(directoryListing[i].delete())
                        System.out.println("The selected record is deleted.");
            }
        }
    }

    /**
     * Delete saved record
     *
     * @param name      the name of the record
     */
    public void deleteRecords(String name){
        File recordsDir = new File(Constant.CWD+"game records/");
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

    /**
     * Get the name of record by number
     *
     * @param number    the number that represents the certain record
     * @return          the name of the record
     */
    public String findRecords(int number){
        File recordsDir = new File(Constant.CWD+"game records/");
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
     * @param input     player's input
     * @return          numeric --> true, not numeric --> false,
     */
    public boolean isNumeric(String input){
        return input != null && input.matches("[0-9.]+");
    }

    /**
     * Distinguish if the input is in the range of the records existed
     *
     * @param input     number
     * @return          in the range --> true, not the range --> false
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
     * @param name      the record name
     * @param game      the game to be saved
     * @param player    the turn of player
     */
    public void saveGame(String name, Game game, Player player) {
        GameRecord recordToSave = new GameRecord(name, game, player);
        try{
            FileOutputStream fos = new FileOutputStream(Constant.CWD+"game records/"+name+".ser");
            ObjectOutputStream out =new ObjectOutputStream(fos);
            out.writeObject(recordToSave);
            out.close();
            fos.close();
            System.out.println("File saved successfully");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Load a game record by the file name
     *
     * @param fileName      the name of the game record
     * @return              the record to be loaded
     */
    public GameRecord loadGame(String fileName){
        GameRecord recordToLoad;
        try{
            FileInputStream fis = new FileInputStream(Constant.CWD+"game records/"+fileName+".ser");
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

