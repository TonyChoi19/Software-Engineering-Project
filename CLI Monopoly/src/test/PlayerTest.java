import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    /* This test aims to check if move() can correctly count the steps and adding money after they reach square 1 again */
    @Test
    void move() {
        // player's position is 1 (init)

        //normal move
        Player player1 = new Player("Ken");
        player1.move(7);
        assertEquals(8, player1.getPos());

        //move to 1 again
        Player player2 = new Player("Nick");
        player2.move(20);
        assertEquals(1, player2.getPos());
        assertEquals(3000, player2.getMoney());

        //move pass 1
        Player player3 = new Player("Steven");
        player3.move(21);
        assertEquals(2, player3.getPos());
        assertEquals(3000, player3.getMoney());
    }

    /* This test aims to check if isEnoughMoneyToPay() can correctly detect whether players have enough money to pay their fee */
    @Test
    void isEnoughMoneyToPay() {
        Player player = new Player("Alex");

        //not enough to pay
        player.setMoney(50);
        assertFalse(player.isEnoughMoneyToPay(90));

        //enough to pay
        player.setMoney(100);
        assertTrue(player.isEnoughMoneyToPay(90));
    }

    /* This test aims to check if addMoney() can add the correct amount of money form the players */
    @Test
    void addMoney() {
        Player player = new Player("Jack");
        //the money is HKD1500 ( init )

        player.addMoney(500);
        assertEquals(2000, player.getMoney());

        player.addMoney(90);
        assertEquals(2090, player.getMoney());
    }

    /* This test aims to check if deductMoney() can deduct the correct amount of money form the players */
    @Test
    void deductMoney() {
        Player player = new Player("Ryan");
        //the money is HKD1500 ( init )

        player.deductMoney(150, "Fine");
        assertEquals(1350, player.getMoney());

        player.deductMoney(135, "Tax");
        assertEquals(1215, player.getMoney());
    }

    /* This test aims to check if purchasePropertyTest() can add the property purchased by player and deduct players' money */
    @Test
    void purchaseProperty(){
        Square Central = new Property(2, "Central",800,90);
        Player player = new Player("Peter");

        player.purchaseProperty(Central);
        int ownedProperties = player.getProperties().size();

        //check the owned properties and its name
        assertEquals(1, ownedProperties);
        assertEquals("Central", player.getProperties().get(0).getName());

        assertEquals(700,player.getMoney());
    }

    /* This test aims to check if releaseFromJailTest() can correctly reset player's conditions after they are release from the jail */
    @Test
    void releaseFromJail() {
        Player player = new Player("Rein");
        player.releaseFromJail("Double");
        assertEquals(0, player.getInJailCount());
        assertFalse(player.isInJail());
    }

    /* This test aims to check if isHaveChoicePayJail() can detect and give a boolean to tell whether a player can choose to pay the fine and leave the jail */
    @Test
    void isHaveChoicePayJail() {
        //First round in jail --> have the option to pay and leave directly
        Player player1 = new Player("Ana");
        player1.setInJail(true);
        player1.setInJailCount(1);
        assertTrue(player1.isHaveChoicePayJail());

        //Second round in jail --> have the option to pay and leave directly
        Player player2 = new Player("Maya");
        player2.setInJail(true);
        player2.setInJailCount(2);
        assertTrue(player2.isHaveChoicePayJail());


        //Third round in jail --> don't have the option to pay (forced to pay)
        Player player3 = new Player("Dog");
        player3.setInJail(true);
        player3.setInJailCount(3);
        assertFalse(player3.isHaveChoicePayJail());

        //Not even in jail
        Player player4 = new Player("Cat");
        player4.setInJail(false);
        assertFalse(player4.isHaveChoicePayJail());
    }
}
