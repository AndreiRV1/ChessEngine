package Pieces.Moves;

import Board.Board;
import Board.Position;

import java.util.List;

public interface MoveStrategy {
    List<Position> getPossibleMoves(Board board, Position from, boolean recursiveCheck);
}