package main;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SquareTest {

    // This test aims to check can setOwner() correctly set the owner of the property squares.
    @Test
    public void setOwner() {
        Player player1 = new Player("Ken");
        Player player2 = new Player("Ryan");
        Square Central = new Property(2, "Central",800,90);
        Square Shatin = new Property(12, "Shatin", 700, 75);
        Central.setOwner(player1);
        Shatin.setOwner(player2);
        assertEquals(player1, Central.getOwner());
        assertEquals(player2, Shatin.getOwner());
    }

    // This test aims to check can setRent() correctly set the rent of the property squares.
    @Test
    public void setRent() {
        Square Central = new Property(2, "Central",800,90);

        //change the rent from 90 to 900
        Central.setRent(900);
        assertEquals(900, Central.getRent());
    }

    // This test aims to check can getPos() get the required property square's position in the board correctly.
    @Test
    public void getPos() {
        Square Stanley = new Property(5, "Stanley",600,60);
        assertEquals(5,Stanley.getPos());
    }

    // This test aims to check can getName() get the required property square's name correctly.
    @Test
    public void getName() {
        Square WanChai = new Property(3, "Wan Chai",700,65);
        assertEquals("Wan Chai", WanChai.getName());
    }

    // This test aims to check can getPrice() get the required price of the property square correctly.
    @Test
    public void getPrice() {
        Square ShekO = new Property(7, "Shek O", 400, 10);
        assertEquals(400, ShekO.getPrice());
    }

    // This test aims to check can getRent() get the required Rent of the property square correctly.
    @Test
    public void getRent() {
        Square MongKok = new Property(8, "Mong Kok", 500, 40);
        assertEquals(40,MongKok.getRent());
    }

    // This test aims to check can getOwner() correctly get the owner of the squares.
    @Test
    public void getOwner() {
        Player player1 = new Player("1");
        Square Central = new Property(2, "Central",800,90);
        Square incomeTax = new Function(4, "Income tax");

        //should be return null, because the square "Income tax" is not a property and cannot be owned
        assertNull(incomeTax.getOwner());

        //should be return null, because the square "Central" is not owned yet
        assertNull(Central.getOwner());

        //should be return player1, because the square "Central" is owned by player1
        Central.setOwner(player1);
        assertEquals(player1, Central.getOwner());
    }

}