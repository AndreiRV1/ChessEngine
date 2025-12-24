package Pieces;

import Board.Board;
import Board.Position;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Rook extends Piece {
    public Rook(Colors color, Position position) {
        super(color, position);
    }

    public Rook(String color, String position) {
        super(color, position);
    }


    public List<Position> getPossibleMoves(Board board) {
        int[][] directions = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
        return getPossibleMovesPrototype(board, directions, true);
    }

    public List<Position> getPossibleMoves(Board board, boolean check) {
        int[][] directions = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
        return getPossibleMovesPrototype(board, directions, true, check);
    }

    @Override
    public char type() {
        return 'R';
    }

    public String toString() {
        return "" + 'R' + '-' + ((getColor() == Colors.WHITE) ? "W" : "B");
    }
}
