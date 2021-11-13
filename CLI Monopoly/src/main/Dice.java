package main;

import java.io.Serializable;

public class Dice implements Serializable {
        private int die1;
        private int die2;
        private boolean isDouble;

    /* Constructor */
    public Dice() {
        this.die1 = 1;
        this.die2 = 1;
        this.isDouble = false;
    }

    /**
     * To determine it is a double or not
     * @return isDouble
     */
    public boolean isDouble() {
        return isDouble;
    }

    public void setDie1(int die1) {
        this.die1 = die1;
    }

    public void setDie2(int die2) {
        this.die2 = die2;
    }

    public void setDouble(boolean aDouble) {
        isDouble = aDouble;
    }

    public int getDie1() {
        return die1;
    }

    public int getDie2() {
        return die2;
    }

    /**
     * Throwing two dice
     * @return The sum of the values of two dice
     */
    public int throwDice(){
        die1 = (int) (1+ Math.floor(Math.random()*4));
        die2 = (int) (1+ Math.floor(Math.random()*4));
        this.isDouble = die1 == die2;
        return (die1 + die2);
    }

}
