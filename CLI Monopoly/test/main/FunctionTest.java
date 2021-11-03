package main;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class FunctionTest {

    // This test aims to check if the isFunction() can check the whether the is a place with function or not
    @Test
    public void isFunction() {
        Function Chance = new Function(9, "Chance");
        Assertions.assertTrue(Chance.isFunction());

        Square Central = new Property(2, "Central", 800, 90);
        Assertions.assertFalse(Central.isFunction());
    }
}