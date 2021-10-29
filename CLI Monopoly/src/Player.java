import java.io.Serializable;
import java.util.ArrayList;

public class Player implements Serializable {
    private String name;

    private int money;
    private int pos;
    private int inJailCount;

    private boolean inJail;

    private ArrayList<Square> properties = new ArrayList<>();

    public Player(String name) {
        this.name = name;
        this.money = 1500;
        this.pos = 1;
        this.inJail = false;
        this. inJailCount = 0;
    }

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

    public void move(int step){
        if (this.pos + step > 20){
            this.pos += step - 20;
            this.money+=1500;
            System.out.println("\nYou have received HKD1500 salary.");
        }else
            this.pos += step;
    }

    public void freeProperty(){
        System.out.println("\n\u001B[31mYou have lost due to negative balance ($" + this.getMoney() + ")" +
                "\nYour propoties will be automatically free.\u001B[0m");

        if (this.properties.isEmpty())
            System.out.println("\nFreed Properties: NONE");
        else{
            System.out.println("\nFreed Properties:");
            for (Square property : this.properties){
                property.setOwner(null);
                System.out.println(property.name);
            }
            properties = null;
        }
    }

    public boolean isEnoughMoneyToPay(int fee){
        return ((getMoney()-fee) >= 0);
    }

    public void deductMoney(int amount, String reason){
        this.money -= amount;
            System.out.println(reason + " paid successfully. HKD" + amount + " is deducted from your account." +
                    "\nYour balance: HKD" + getMoney());
    }

    public void addMoney(int amount){
        this.money += amount;
        System.out.println("HKD"+ amount + " is added into " + name + "'s account.");
    }

    public void purchaseProperty(Square property){
        this.properties.add(property);
        deductMoney(property.getPrice(), "Property");
    }

    public void releaseFromJail(String reason){
        setInJail(false);
        setInJailCount(0);
        if (reason.equals("Fine"))
            System.out.println("\nFine paid, you are now free to go.");
        if (reason.equals("Double"))
            System.out.println("\nCongrats! It is a double. You are now free to go.");
    }

    public boolean isHaveChoicePayJail(){
        return isInJail() && inJailCount<3;
    }






}

