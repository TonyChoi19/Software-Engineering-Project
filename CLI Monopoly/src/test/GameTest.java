import org.junit.BeforeClass;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    @Test
    void getPlayersList() {
        Game game = new Game(6);
        assertEquals(game.getPlayersList(), game.playersList);
    }

    @Test
    void setGameRound() {
        Game game = new Game(6);
        game.setGameRound(50);
        assertEquals(50, game.getGameRound());
    }

    @Test
    void getGameRound() {
        Game game = new Game(6);
        // gameRound is initialized by 1
        assertEquals(1, game.getGameRound());

        game.setGameRound(50);
        assertEquals(50, game.getGameRound());
    }
}