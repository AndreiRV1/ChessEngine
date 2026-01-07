package Pieces.Moves;

import Board.Board;
import Board.Position;
import Pieces.Moves.MoveStrategy;
import Pieces.Piece;

import java.util.List;

public class BishopMoveStrategy implements MoveStrategy {
    @Override
    public List<Position> getPossibleMoves(Board board, Position from, boolean recursiveCheck) {
        int[][] directions = {{1, 1}, {1, -1}, {-1, -1}, {-1, 1}};
        Piece p = board.getPieceAt(from);
        if (p == null) return new java.util.ArrayList<>();
        return p.getPossibleMovesPrototype(board, directions, true, recursiveCheck);
    }
}
