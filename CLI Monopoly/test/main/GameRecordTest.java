package main;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameRecordTest {

    @Test
    // To test whether the getName() can get the name of the GameRecord
    public void getName() {
        Game game  = new Game(5);
        Player player1 = game.playersList.get(0); //player 1
        GameRecord gameRecord = new GameRecord("main/test", game , player1);
        assertEquals("main/test",gameRecord.getName());
    }

    @Test
     // To test whether the getGame() can get the name of the Game
    public void getGame() {
        Game game  = new Game(5);
        Player player1 = game.playersList.get(0); //player 1

        GameRecord gameRecord = new GameRecord("main/test", game , player1);
        assertEquals(game,gameRecord.getGame());
    }

    @Test
    // To test whether the getPlayerTurn() can get the player name of current turn of a  GameRecord
    public void getPlayersTurn() {
        Game game  = new Game(5);
        Player player1 = game.playersList.get(0); //player 1

        GameRecord gameRecord = new GameRecord("main/test", game , player1);
        assertEquals(player1,gameRecord.getPlayerTurn());
    }
}
