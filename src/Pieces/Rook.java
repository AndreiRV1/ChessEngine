package Pieces;

import Board.Board;
import Board.Position;
import Pieces.Moves.RookMoveStrategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Rook extends Piece {
    public Rook(Colors color, Position position) {
        super(color, position);
        setMoveStrategy(new RookMoveStrategy());
    }

    public Rook(String color, String position) {
        super(color, position);
        setMoveStrategy(new RookMoveStrategy());
    }


    @Override
    public char type() {
        return 'R';
    }

    public String toString() {
        return "" + 'R' + '-' + ((getColor() == Colors.WHITE) ? "W" : "B");
    }
}
