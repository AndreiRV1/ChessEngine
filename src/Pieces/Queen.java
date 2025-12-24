package Pieces;

import Board.Board;
import Board.Position;

import java.util.ArrayList;
import java.util.List;

public class Queen extends Piece {
    public Queen(Colors color, Position position) {
        super(color, position);
    }

    public Queen(String color, String position) {
        super(color, position);
    }

    @Override
    public List<Position> getPossibleMoves(Board board) {
        int[][] directions = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}, {1, 1}, {-1, -1}, {-1, 1}, {1, -1}};
        return getPossibleMovesPrototype(board, directions, true);
    }


    public List<Position> getPossibleMoves(Board board, boolean check) {
        int[][] directions = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}, {1, 1}, {-1, -1}, {-1, 1}, {1, -1}};
        return getPossibleMovesPrototype(board, directions, true, check);
    }

    @Override
    public char type() {
        return 'Q';
    }

    public String toString() {
        return "" + 'Q' + '-' + ((getColor() == Colors.WHITE) ? "W" : "B");
    }
}
