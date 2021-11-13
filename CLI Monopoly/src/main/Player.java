package main;

import java.io.Serializable;
import java.util.ArrayList;

public class Player implements Serializable {
    private String name;
    private int money;
    private int pos;
    private int inJailCount;
    private boolean inJail;
    private ArrayList<Square> properties = new ArrayList<>();


    /**
     * Constructor
     * @param name Player's name
     */
    public Player(String name) {
        this.name = name;
        this.money = 1500;
        this.pos = 1;
        this.inJail = false;
        this. inJailCount = 0;
    }

    //getters & setters

    public String getName() {
        return name;
    }

    public int getMoney() {
        return money;
    }

    public int getPos() {
        return pos;
    }

    public boolean isInJail() {
        return inJail;
    }

    public int getInJailCount() {
        return inJailCount;
    }

    public ArrayList<Square> getProperties() {
        return properties;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public void setInJail(boolean inJail) {
        this.inJail = inJail;
    }

    public void setInJailCount(int inJailCount) {
        this.inJailCount = inJailCount;
    }

    public void setMoney(int money) {
        this.money = money;
    }


    /**
     * Move a player
     * @param step Step to move
     */
    public void move(int step){
        if (this.pos + step > 20){
            this.pos += step - 20;
            this.money+=1500;
            System.out.println("\nYou have received HKD1500 salary.");
        }else
            this.pos += step;
    }

    /**
     * Free(empty) player's properties
     */
    public void freeProperty(){
        View view = new View();
        view.printFreePropertyMsg(this);

        if (!this.properties.isEmpty())
            //removing the owner of the properties
            for (Square property : this.properties){
                property.setOwner(null);
            }
            properties = null;

    }


    /**
     * To determine if the player has enough money to pay
     * @param fee
     * @return True if the player has enough money to pay, False if the player has not enough money to pay
     */
    public boolean isEnoughMoneyToPay(int fee){
        return ((getMoney()-fee) >= 0);
    }

    /**
     * To deduct player's money
     * @param amount How much money to deduct
     * @param reason The event to deduct
     */
    public void deductMoney(int amount, String reason){
        this.money -= amount;

        View view = new View();
        view.printDeductMoneyMsg(this,amount,reason);
    }

    /**
     * To add player's money
     * @param amount How much money to add
     */
    public void addMoney(int amount){
        this.money += amount;

        View view = new View();
        view.printAddMoneyMsg(this,amount);
    }

    /**
     * To add player's property
     * @param property
     */
    public void purchaseProperty(Square property){
        this.properties.add(property);
        deductMoney(property.getPrice(), "Property");
    }

    /**
     * Set player free from jail
     * @param reason
     */
    public void releaseFromJail(String reason){
        setInJail(false);
        setInJailCount(0);
        View view = new View();
        view.printReleaseFromJailMsg(reason);
    }

    public boolean isHaveChoicePayJail(){
        return isInJail() && inJailCount<3;
    }






}

