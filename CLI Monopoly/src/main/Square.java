package main;

import java.io.Serializable;

public abstract class Square implements Serializable {
    protected int pos;
    protected String name;
    protected int price;
    protected int rent;
    protected Player owner;

    protected boolean isProperty(){ return false; }

    protected  boolean isFunction(){ return false; }

    protected Player getOwner(){ return null; }

    public int getPos() {
        return pos;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getRent() {
        return rent;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public void setRent(int rent) {
        this.rent = rent;
    }
}
