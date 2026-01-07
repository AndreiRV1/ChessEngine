package Pieces;

import Board.Board;
import Board.Position;
import Pieces.Moves.QueenMoveStrategy;

import java.util.ArrayList;
import java.util.List;

public class Queen extends Piece {
    public Queen(Colors color, Position position) {
        super(color, position);
        setMoveStrategy(new QueenMoveStrategy());
    }

    public Queen(String color, String position) {
        super(color, position);
        setMoveStrategy(new QueenMoveStrategy());
    }

    @Override
    public char type() {
        return 'Q';
    }

    public String toString() {
        return "" + 'Q' + '-' + ((getColor() == Colors.WHITE) ? "W" : "B");
    }
}
