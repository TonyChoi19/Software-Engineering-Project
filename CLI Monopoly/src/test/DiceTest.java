import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DiceTest {
    // According to the Project Description PDF:
    // The Monopoly Game has a pair of four-sided (tetrahedral) dice
    // The rolled value should be in range of 2-8
    int MaxValue = 8;
    int MinValue = 2;

    // throwDiceTest1() aims to test whether the rolled dice has a expected result,
    // which should be smaller than/equals to 8 and larger than/equals to 2
    // test for 100 times to make sure the randomness is correctly in the expected range
    @Test
    void throwDiceTest() {
        int i;
        for(i = 0; i <=100; i++){
            Dice dice = new Dice();
            assertTrue(dice.throwDice() <= MaxValue);
            assertTrue(dice.throwDice() >= MinValue);
        }
    }
}