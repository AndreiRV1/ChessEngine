package Pieces.Moves;

import Board.Board;
import Board.Position;
import Pieces.Piece;

import java.util.List;

public class RookMoveStrategy implements MoveStrategy {
    @Override
    public List<Position> getPossibleMoves(Board board, Position from, boolean recursiveCheck) {
        int[][] directions = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
        Piece p = board.getPieceAt(from);
        if (p == null) return new java.util.ArrayList<>();
        return p.getPossibleMovesPrototype(board, directions, true, recursiveCheck);
    }
}
