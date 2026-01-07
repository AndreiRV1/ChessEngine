package Pieces;

import Board.Board;
import Board.Position;
import Pieces.Moves.BishopMoveStrategy;

import java.util.List;

public class Bishop extends Piece {
    public Bishop(Colors color, Position position) {
        super(color, position);
        setMoveStrategy(new BishopMoveStrategy());
    }

    public Bishop(String color, String position) {
        super(color, position);
        setMoveStrategy(new BishopMoveStrategy());
    }

    @Override
    public char type() {
        return 'B';
    }

    public String toString() {
        return "" + 'B' + '-' + ((getColor() == Colors.WHITE) ? "W" : "B");
    }
}
