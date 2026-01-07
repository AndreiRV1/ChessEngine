package Pieces.Moves;


import Board.Board;
import Board.Position;
import Pieces.Piece;

import java.util.List;

public class KnightMoveStrategy implements MoveStrategy {
    @Override
    public List<Position> getPossibleMoves(Board board, Position from, boolean recursiveCheck) {
        int[][] directions = {{-2, 1}, {-2, -1}, {2, 1}, {2, -1}, {1, 2}, {-1, 2}, {1, -2}, {-1, -2}};
        Piece p = board.getPieceAt(from);
        if (p == null) return new java.util.ArrayList<>();
        return p.getPossibleMovesPrototype(board, directions, false, recursiveCheck);
    }
}
