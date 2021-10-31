import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameRecordTest {

    @Test // To test whether the getName() can get the name of the GameRecord
    void getNameTest() {
        Game game  = new Game(5);
        Player player = new Player("Peter");

        GameRecord gameRecord = new GameRecord("test", game , player);
        assertEquals("test",gameRecord.getName());


    }
    @Test // To test whether the getGame() can get the name of the Game
    void getGameTest() {
        Game game  = new Game(5);
        Player player = new Player("Peter");

        GameRecord gameRecord = new GameRecord("test", game , player);
        assertEquals(game,gameRecord.getGame());


    }
    @Test // To test whether the getPlayerTurn() can get the playername of current turn of a  GameRecord
    void getPlayersTurnTest() {
        Game game  = new Game(5);
        Player player = new Player("Peter");

        GameRecord gameRecord = new GameRecord("test", game , player);
        assertEquals(player,gameRecord.getPlayerTurn());


    }
}
