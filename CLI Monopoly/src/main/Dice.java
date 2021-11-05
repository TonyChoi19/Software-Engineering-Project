package main;

import java.io.Serializable;

public class Dice implements Serializable {
        private int die1;
        private int die2;
        private boolean isDouble;

    /**
     * Constructor
      */
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

    public void printValue(){
        System.out.println("The value of die 1 is :" + this.die1);
        System.out.println("The value of die 2 is :" + this.die2);
    }




}
