package Pieces;

import Board.Board;
import Board.Position;

import java.util.List;

public class Bishop extends Piece {
    public Bishop(Colors color, Position position) {
        super(color, position);
    }

    public Bishop(String color, String position) {
        super(color, position);
    }

    @Override
    public List<Position> getPossibleMoves(Board board) {
        int[][] directions = {{1, 1}, {1, -1}, {-1, -1}, {-1, 1}};
        return getPossibleMovesPrototype(board, directions, true);
    }

    public List<Position> getPossibleMoves(Board board,boolean check) {
        int[][] directions = {{1, 1}, {1, -1}, {-1, -1}, {-1, 1}};
        return getPossibleMovesPrototype(board, directions, true,check);
    }

    @Override
    public char type() {
        return 'B';
    }

    public String toString() {
        return "" + 'B' + '-' + ((getColor() == Colors.WHITE) ? "W" : "B");
    }
}
