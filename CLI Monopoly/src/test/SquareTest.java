import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SquareTest {

    // This test aims to check can setOwner() correctly set the owner of the property squares.
    @Test
    void setOwner() {
        Player player1 = new Player("Ken");
        Player player2 = new Player("Ryan");
        Square Central = new Property(2, "Central",800,90);
        Square Shatin = new Property(12, "Shatin", 700, 75);
        Central.setOwner(player1);
        Shatin.setOwner(player2);
        assertEquals(player1, Central.getOwner());
        assertEquals(player2, Shatin.getOwner());
    }

    // This test aims to check can getPos() get the required property square's position in the board correctly.
    @Test
    void getPos() {
        Square Stanley = new Property(5, "Stanley",600,60);
        assertEquals(5,Stanley.getPos());
    }

    // This test aims to check can getName() get the required property square's name correctly.
    @Test
    void getName() {
        Square WanChai = new Property(3, "Wan Chai",700,65);
        assertEquals("Wan Chai", WanChai.getName());
    }

    // This test aims to check can getPrice() get the required price of the property square correctly.
    @Test
    void getPrice() {
        Square ShekO = new Property(7, "Shek O", 400, 10);
        assertEquals(400, ShekO.getPrice());
    }

    // This test aims to check can getRent() get the required Rent of the property square correctly.
    @Test
    void getRent() {
        Square MongKok = new Property(8, "Mong Kok", 500, 40);
        assertEquals(40,MongKok.getRent());
    }
}