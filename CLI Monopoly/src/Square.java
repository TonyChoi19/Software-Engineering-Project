import java.io.Serializable;
import java.util.ArrayList;

public abstract class Square implements Serializable {
    protected int pos;
    protected String name;
    protected int price;
    protected int rent;
    protected Player owner;

    protected boolean isProperty(){return false;}

    protected  boolean isFunction(){return false;}

    protected Player belongsTo(){return null;}

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public int getPos() {
        return pos;
    }

    public String getName() {
        return name;
    }

    public Player getOwner() {
        return owner;
    }

    public int getPrice() {
        return price;
    }

    public int getRent() {
        return rent;
    }
}
