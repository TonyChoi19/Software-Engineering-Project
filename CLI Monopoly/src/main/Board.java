package main;

import java.io.Serializable;
import java.util.ArrayList;

public class Board implements Serializable {
    protected ArrayList<Square> squares = new ArrayList<>();

    public Board() {
        squares.add(new Function(1, "Go"));
        squares.add(new Property(2, "Central", 800, 90));
        squares.add(new Property(3, "Wan Chai", 700, 65));
        squares.add(new Function(4, "Income tax"));
        squares.add(new Property(5, "Stanley", 600, 60));
        squares.add(new Function(6, "Jail/Just Visiting"));
        squares.add(new Property(7, "Shek O", 400, 10));
        squares.add(new Property(8, "Mong Kok", 500, 40));
        squares.add(new Function(9, "Chance"));
        squares.add(new Property(10, "Tsing Yi", 400, 15));
        squares.add(new Function(11, "Free parking"));
        squares.add(new Property(12, "Shatin", 700, 75));
        squares.add(new Function(13, "Chance"));
        squares.add(new Property(14, "Tuen Mun", 400, 20));
        squares.add(new Property(15, "Tai Po", 500, 25));
        squares.add(new Function(16, "Go to Jail"));
        squares.add(new Property(17, "Sai Kung", 400, 10));
        squares.add(new Property(18, "Yuen Long", 400, 25));
        squares.add(new Function(19, "Chance"));
        squares.add(new Property(20, "Tai O", 600, 25));
    }

    public void printBoard(){
        for (Square square : squares){
            if (square.isProperty() && square.belongsTo() == null)
                System.out.printf("%-3d %-18s  \u001B[32mAvailable\u001B[0m\n", square.getPos(), square.getName());
            else if (square.isProperty() && square.belongsTo() != null)
                System.out.printf("%-3d %-18s  \u001B[31mOccupied\u001B[0m  %s's property\n", square.getPos(), square.getName(), square.getOwner().getName());
            else
                System.out.printf("%-3d %-18s\n", square.getPos(), square.getName());
        }
    }

    public Square findSquare(int pos){
        Square ret = null;
        for (Square square : squares){
            if (square.getPos() == pos)
                ret = square;
        }
        return ret;
    }



}
