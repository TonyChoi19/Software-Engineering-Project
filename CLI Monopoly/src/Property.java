public class Property extends Square{

    public Property(int pos, String name, int price, int rent) {
        this.pos = pos;
        this.name = name;
        this.price = price;
        this.rent = rent;
    }

    @Override
    protected boolean isProperty() {
        return true;
    }

    @Override
    protected Player belongsTo() {
        return this.owner;
    }


}
