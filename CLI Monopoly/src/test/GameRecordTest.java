import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameRecordTest {

    @Test // To test whether the getName() can get the name of the GameRecord
    void getName() {
        Game game  = new Game(5);
        Player player1 = game.playersList.get(0); //player 1
        GameRecord gameRecord = new GameRecord("test", game , player1);
        assertEquals("test",gameRecord.getName());
    }

    @Test // To test whether the getGame() can get the name of the Game
    void getGame() {
        Game game  = new Game(5);
        Player player1 = game.playersList.get(0); //player 1

        GameRecord gameRecord = new GameRecord("test", game , player1);
        assertEquals(game,gameRecord.getGame());
    }

    @Test // To test whether the getPlayerTurn() can get the player name of current turn of a  GameRecord
    void getPlayersTurn() {
        Game game  = new Game(5);
        Player player1 = game.playersList.get(0); //player 1

        GameRecord gameRecord = new GameRecord("test", game , player1);
        assertEquals(player1,gameRecord.getPlayerTurn());
    }
}
