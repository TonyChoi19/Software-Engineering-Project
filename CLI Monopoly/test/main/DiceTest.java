package main;

import org.junit.jupiter.api.RepeatedTest;

import static org.junit.jupiter.api.Assertions.*;

public class DiceTest {
    // According to the Project Description PDF:
    // The Monopoly Game has a pair of four-sided (tetrahedral) dice
    // The rolled value should be in range of 2-8
    static final int  MAX_VALUE = 8;
    static final int  MIN_VALUE = 2;

    // throwDiceTest1() aims to test whether the rolled dice has an expected result,
    // which should be smaller than/equals to 8 and larger than/equals to 2
    // test for 100 times to make sure the randomness is correctly in the expected range
    @RepeatedTest(100)
    void throwDiceTest() {
        Dice dice = new Dice();
        int moveSteps = dice.throwDice();
        assertTrue(moveSteps <= MAX_VALUE);
        assertTrue(moveSteps >= MIN_VALUE);
    }
}