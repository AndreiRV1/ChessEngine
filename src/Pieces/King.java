package Pieces;

import Board.Board;
import Board.Position;

import java.util.List;

public class King extends Piece {
    public King(Colors color, Position position) {
        super(color, position);
    }

    public King(String color, String position) {
        super(color, position);
    }

    @Override
    public List<Position> getPossibleMoves(Board board) {
        int[][] directions = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}, {1, 1}, {-1, -1}, {-1, 1}, {1, -1}};
        return getPossibleMovesPrototype(board, directions, false);
    }

    public List<Position> getPossibleMoves(Board board,boolean check) {
        int[][] directions = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}, {1, 1}, {-1, -1}, {-1, 1}, {1, -1}};
        return getPossibleMovesPrototype(board, directions, false,check);
    }

    @Override
    public char type() {
        return 'K';
    }

    public String toString() {
        return "" + 'K' + '-' + ((getColor() == Colors.WHITE) ? "W" : "B");
    }
}
