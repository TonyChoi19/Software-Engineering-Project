package main;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {

    /* This is to test whether findSquare() can return correct Square by the position */
    @Test
    public void findSquare() {
        Board board = new Board();
        //Checking corresponding square
        assertEquals(board.findSquare(1), board.squares.get(0));
        assertEquals(board.findSquare(2), board.squares.get(1));
        assertEquals(board.findSquare(3), board.squares.get(2));
        assertEquals(board.findSquare(4), board.squares.get(3));
        assertEquals(board.findSquare(5), board.squares.get(4));
        assertEquals(board.findSquare(6), board.squares.get(5));
        assertEquals(board.findSquare(7), board.squares.get(6));
        assertEquals(board.findSquare(8), board.squares.get(7));
        assertEquals(board.findSquare(9), board.squares.get(8));
        assertEquals(board.findSquare(10), board.squares.get(9));
        assertEquals(board.findSquare(11), board.squares.get(10));
        assertEquals(board.findSquare(12), board.squares.get(11));
        assertEquals(board.findSquare(13), board.squares.get(12));
        assertEquals(board.findSquare(14), board.squares.get(13));
        assertEquals(board.findSquare(15), board.squares.get(14));
        assertEquals(board.findSquare(16), board.squares.get(15));
        assertEquals(board.findSquare(17), board.squares.get(16));
        assertEquals(board.findSquare(18), board.squares.get(17));
        assertEquals(board.findSquare(19), board.squares.get(18));
        assertEquals(board.findSquare(20), board.squares.get(19));
    }
}