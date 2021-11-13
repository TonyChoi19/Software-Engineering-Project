package main;

import java.io.Serializable;
import java.util.ArrayList;

public class Board implements Serializable {
    protected ArrayList<Square> squares = new ArrayList<>();

    /* Constructor */
    public Board() {
        //adding all the squares
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

    public ArrayList<Square> getSquares() {
        return squares;
    }

    /**
     * Fine the square by pos
     * @param pos The position of the square
     * @return Square
     */
    public Square findSquare(int pos){
        Square ret = null;
        for (Square square : squares){
            if (square.getPos() == pos)
                ret = square;
        }
        return ret;
    }



}
