package Pieces;

import Board.Board;
import Board.Position;
import Pieces.Moves.KingMoveStrategy;

import java.util.List;

public class King extends Piece {
    public King(Colors color, Position position) {
        super(color, position);
        setMoveStrategy(new KingMoveStrategy());
    }

    public King(String color, String position) {
        super(color, position);
        setMoveStrategy(new KingMoveStrategy());
    }

    @Override
    public char type() {
        return 'K';
    }

    public String toString() {
        return "" + 'K' + '-' + ((getColor() == Colors.WHITE) ? "W" : "B");
    }
}
