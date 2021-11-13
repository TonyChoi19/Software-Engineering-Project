package main;

import java.io.Serializable;
import java.util.ArrayList;

public class Game implements Serializable {
    private int gameRound;
    protected ArrayList<Player> playersList = new ArrayList<>();
    protected Board board = new Board();
    protected Dice dice = new Dice();

    /**
     * Constructor
     * @param playerCount The amount of players
     */
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

    public Board getBoard() {
        return board;
    }

    public Dice getDice() {
        return dice;
    }







}
