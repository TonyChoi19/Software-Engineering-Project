package main;

import java.io.Serializable;
import java.sql.Timestamp;

public class GameRecord implements Serializable
{
    private String name;
    private Game game;
    private Timestamp timestamp;
    private boolean isWritten;
    private Player playerTurn;

    public GameRecord(String name, Game game, Player playerTurn) {
        this.name = name;
        this.game = game;
        this.timestamp = new Timestamp(System.currentTimeMillis());
        this.isWritten = true;
        this.playerTurn = playerTurn;
    }


    public GameRecord() {
        this.name = "Available";
        this.game = null;
        isWritten = false;
    }

    public String getName() {
        return name;
    }

    public Game getGame() {
        return game;
    }

    public boolean isWritten() {
        return isWritten;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public Player getPlayerTurn() {
        return playerTurn;
    }
}
