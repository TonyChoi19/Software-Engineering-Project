package main;

import java.io.Serializable;
import java.sql.Timestamp;

public class GameRecord implements Serializable
{
    private String name;
    private Game game;
    private Player playerTurn;

    /**
     * Constructor
     * @param name The name of the record
     * @param game
     * @param playerTurn The turn of player when it is saved
     */
    public GameRecord(String name, Game game, Player playerTurn) {
        this.name = name;
        this.game = game;
        this.playerTurn = playerTurn;
    }

    public String getName() {
        return name;
    }

    public Game getGame() {
        return game;
    }

    public Player getPlayerTurn() {
        return playerTurn;
    }
}
