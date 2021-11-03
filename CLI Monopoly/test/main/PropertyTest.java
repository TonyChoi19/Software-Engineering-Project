package main;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PropertyTest {


    // This test aims to check if the getName() can check the name of the property
    @Test
    public void getName() {
        Square Central = new Property(2, "Central", 800, 90);
        assertEquals("Central", Central.getName());
    }

    // This test aims to check if the getPos() can check the position of the property
    @Test
    public void getPos() {
        Square Central = new Property(2, "Central", 800, 90);
        assertEquals(2, Central.getPos());
    }

    // This test aims to check if the getPrice() can check the price of the property
    @Test
    public void getPrice() {
        Square Central = new Property(2, "Central", 800, 90);
        assertEquals(800, Central.getPrice());
    }

    // This test aims to check if the getRent() can check the rent of the property
    @Test
    public void getRent() {
        Square Central = new Property(2, "Central", 800, 90);
        assertEquals(90, Central.getRent());
    }

    // This test aims to check if the isProperty can check the whether the is a property or not
    @Test
    public void isProperty() {
        Function Jail = new Function(6, "Jail/Just Visiting");
        assertFalse(Jail.isProperty());
        Square Central = new Property(2, "Central", 800, 90);
        assertTrue(Central.isProperty());
    }


    // This test aims to check if the belongsTo() can check the owner of the property
    @Test
    public void belongsTo() {
        Square Central = new Property(2, "Central", 800, 90);
        Player player = new Player("Peter");
        Central.setOwner(player);
        assertEquals(player,Central.belongsTo());
    }

}

