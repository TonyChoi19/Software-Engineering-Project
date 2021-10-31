import org.junit.BeforeClass;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    /* Get the players list in a game */
    @Test
    void getPlayersList() {
        Game game = new Game(6);
        assertEquals(game.getPlayersList(), game.playersList);
    }

    /* set the game round */
    @Test
    void setGameRound() {
        Game game = new Game(6);
        //set the game round to 50 ( init: 1 )
        game.setGameRound(50);
        assertEquals(50, game.getGameRound());
    }

    /* get the game round */
    @Test
    void getGameRound() {
        Game game = new Game(6);
        // gameRound is initialized by 1
        assertEquals(1, game.getGameRound());

        game.setGameRound(50);
        assertEquals(50, game.getGameRound());
    }
}