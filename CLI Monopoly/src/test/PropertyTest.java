import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PropertyTest {


    // This test aims to check if the getName() can check the name of the property
    @Test
    void nameTest() {
        Square Central = new Property(2, "Central", 800, 90);
        assertEquals("Central", Central.getName());


    }

    // This test aims to check if the getPos() can check the position of the property
    @Test
    void posTest() {
        Square Central = new Property(2, "Central", 800, 90);
        assertEquals(2, Central.getPos());


    }

    // This test aims to check if the getPrice() can check the price of the property
    @Test
    void priceTest() {
        Square Central = new Property(2, "Central", 800, 90);
        assertEquals(800, Central.getPrice());
    }

    // This test aims to check if the getRent() can check the rent of the property
    @Test
    void rentTest() {
        Square Central = new Property(2, "Central", 800, 90);
        assertEquals(90, Central.getRent());

    }

    // This test aims to check if the isProperty can check the whether the is a property or not
    @Test
    void isPropertyTest() {
        Function Jail = new Function(6, "Jail/Just Visiting");
        assertFalse(Jail.isProperty());
        Square Central = new Property(2, "Central", 800, 90);
        assertTrue(Central.isProperty());


    }


    // This test aims to check if the setOwner() and getOwner() can work for setting the owner of the property
    @Test
    void ownerTest() {
        Square Central = new Property(2, "Central", 800, 90);
        Player player = new Player("Peter");
        Central.setOwner(player);
        assertEquals(player, Central.getOwner());
        assertEquals(player,Central.belongsTo());
    }

}

