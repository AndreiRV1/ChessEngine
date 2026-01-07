package Pieces;

import Board.Board;
import Board.Position;
import Pieces.Moves.KnightMoveStrategy;

import java.util.List;

public class Knight extends Piece {
    public Knight(Colors color, Position position) {
        super(color, position);
        setMoveStrategy(new KnightMoveStrategy());
    }

    public Knight(String color, String position) {
        super(color, position);
        setMoveStrategy(new KnightMoveStrategy());
    }


    @Override
    public char type() {
        return 'N';
    }

    public String toString() {
        return "" + 'N' + '-' + ((getColor() == Colors.WHITE) ? "W" : "B");
    }
}
