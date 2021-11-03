package main;

import java.io.Serializable;
import java.util.ArrayList;

public class Game implements Serializable {
    private int gameRound;
    protected ArrayList<Player> playersList = new ArrayList<>();
    protected Board board = new Board();
    protected Dice dice = new Dice();

    public Game(int playerCount) {
        this.gameRound = 1;

        /* add the players to playersList */
        for (int i = 1 ; i<playerCount+1 ; i++){
            String playerName = "Player " + i;
            Player player = new Player(playerName);
            playersList.add(player);
        }
    }

    public ArrayList<Player> getPlayersList(){return this.playersList;}

    public void setGameRound(int gameRound) {
        this.gameRound = gameRound;
    }

    public int getGameRound() {
        return gameRound;
    }

    public void printDetailedPos(int pos){
        Board tempBoard = new Board();
        if (board.findSquare(pos).isProperty()){
            //the property is owned
            if (board.findSquare(pos).belongsTo()!=null)
                System.out.println("\nYou moved to the Position " + pos + ".\tProperty:" + tempBoard.findSquare(pos).getName() + "\tPrice(HKD):" + board.findSquare(pos).getPrice() + "\tRent(HKD):" +board.findSquare(pos).getRent() + "\tOwnedBy:\u001B[31m" + board.findSquare(pos).getOwner().getName() + "\u001B[0m" );
            //the property is not owned
            else{
                System.out.println("\nYou moved to the Position " + pos + ".\tProperty:" + tempBoard.findSquare(pos).getName() + "\tPrice(HKD):" + board.findSquare(pos).getPrice() + "\tRent(HKD):" +board.findSquare(pos).getRent() + "\tOwnedBy:None\u001B[32m (Available)\u001B[0m" );
            }
        }else{
            System.out.println("\nYou moved to the Position " + pos + ".\t" + tempBoard.findSquare(pos).getName());
        }


    }





}
