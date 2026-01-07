package Pieces;

import Board.Board;
import Board.Position;
import Pieces.Moves.PawnMoveStrategy;

import java.util.ArrayList;
import java.util.List;


public class Pawn extends Piece {
    public Pawn(Colors color, Position position) {
        super(color, position);
        setMoveStrategy(new PawnMoveStrategy());
    }

    public Pawn(String color, String position) {
        super(color, position);
        setMoveStrategy(new PawnMoveStrategy());
    }

    @Override
    public char type() {
        return 'P';
    }

    public String toString() {
        return "" + 'P' + '-' + ((getColor() == Colors.WHITE) ? "W" : "B");
    }
}
