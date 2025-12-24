package Pieces;

import Board.Board;
import Board.Position;

import java.util.List;

public class Knight extends Piece {
    public Knight(Colors color, Position position) {
        super(color, position);
    }

    public Knight(String color, String position) {
        super(color, position);
    }

    @Override
    public List<Position> getPossibleMoves(Board board) {
        int[][] directions = {{-2, 1}, {-2, -1}, {2, 1}, {2, -1}, {1, 2}, {-1, 2}, {1, -2}, {-1, -2}};
        return getPossibleMovesPrototype(board, directions, false);
    }

    public List<Position> getPossibleMoves(Board board,boolean check) {
        int[][] directions = {{-2, 1}, {-2, -1}, {2, 1}, {2, -1}, {1, 2}, {-1, 2}, {1, -2}, {-1, -2}};
        return getPossibleMovesPrototype(board, directions, false, check);
    }
    @Override
    public char type() {
        return 'N';
    }

    public String toString() {
        return "" + 'N' + '-' + ((getColor() == Colors.WHITE) ? "W" : "B");
    }
}
