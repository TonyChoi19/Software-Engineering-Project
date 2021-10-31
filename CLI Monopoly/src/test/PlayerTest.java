import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    // This test aims to check if move() can correctly count the steps and adding money after they reach square 1 again
    @Test
    void moveTest() {
        Player player1 = new Player("Ken");
        player1.move(7);
        assertEquals(8, player1.getPos());

        Player player2 = new Player("Nick");
        player2.setPos(17);
        player2.move(8);
        assertEquals(5, player2.getPos());
        assertEquals(3000, player2.getMoney());
    }

    // This test aims to check if isEnoughMoneyToPay() can correctly detect whether players have enough money to pay their fee
    @Test
    void isEnoughMoneyToPayTest() {
        Player player = new Player("Alex");
        player.setMoney(50);
        assertFalse(player.isEnoughMoneyToPay(90));
        player.setMoney(100);
        assertTrue(player.isEnoughMoneyToPay(90));
    }

    // This test aims to check if addMoney() can add the correct amount of money form the players
    @Test
    void addMoneyTest() {
        Player player = new Player("Jack");
        player.addMoney(500);
        assertEquals(2000, player.getMoney());
        player.addMoney(90);
        assertEquals(2090, player.getMoney());
    }

    // This test aims to check if deductMoney() can deduct the correct amount of money form the players
    @Test
    void deductMoneyTest() {
        Player player = new Player("Ryan");
        player.deductMoney(150, "Fine");
        assertEquals(1350, player.getMoney());
        player.deductMoney(135, "Tax");
        assertEquals(1215, player.getMoney());
    }

    // This test aims to check if purchasePropertyTest() can add the property purchased by player and deduct players' money.
    @Test
    void purchasePropertyTest(){
        Square Central = new Property(2, "Central",800,90);
        Player player = new Player("Peter");
        int originalSize = player.getProperties().size();
        player.purchaseProperty(Central);
        assertTrue(player.getProperties().size()>originalSize);
        assertEquals(700,player.getMoney());
    }

    // This test aims to check if releaseFromJailTest() can correctly reset player's conditions after they are release from the jail
    @Test
    void releaseFromJailTest() {
        Player player = new Player("Rein");
        player.releaseFromJail("Double");
        assertEquals(0, player.getInJailCount());
        assertFalse(player.isInJail());
    }

    // This test aims to check if isHaveChoicePayJail() can detect and give a boolean to tell whether a player can choose to pay the fine and leave the jail
    @Test
    void isHaveChoicePayJailTest() {
        Player player1 = new Player("Ana");
        player1.setInJail(true);
        player1.setInJailCount(2);
        assertTrue(player1.isHaveChoicePayJail());

        Player player2 = new Player("Wick");
        player2.setInJail(false);
        assertFalse(player2.isHaveChoicePayJail());
    }
}
