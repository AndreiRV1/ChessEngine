package Pieces;

import Board.Board;
import Board.Position;

import java.util.List;

public interface ChessPiece {
    List<Position> getPossibleMoves(Board board);

    boolean checkForCheck(Board board, Position kingPosition);

    char type();
}
