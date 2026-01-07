package Pieces;

import Board.Board;
import Board.Position;
import Board.ChessPair;
import Pieces.Moves.MoveStrategy;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

public abstract class Piece implements ChessPiece {
    public boolean hasMoved;
    private final Colors color;
    Position position;
    MoveStrategy moveStrategy;

    public Piece(Colors color, Position position) {
        this.color = color;
        this.position = position;
        hasMoved = false;
    }

    public Piece(String color, String position) {
        this.color = Pieces.Colors.valueOf(color);
        this.position = new Position(position);
        hasMoved = false;
    }

    public List<Position> getPossibleMovesPrototype(Board board, int[][] directions, boolean repeat) {
        return getPossibleMovesPrototype(board, directions, repeat, true);
    }

    public List<Position> getPossibleMovesPrototype(Board board, int[][] directions, boolean repeat, boolean deepCheck) {
        List<Position> moves = new ArrayList<>();
        Position pos;
        char x;
        int y;
        Position nextPosition;
        Piece piece;
        // Get basic moves without taking into account if it leaves
        // King i check
        for (int[] dir : directions) {
            x = position.x;
            y = position.y;
            do {
                x = (char) (x + dir[0]);
                y += dir[1];
                nextPosition = new Position(x, y);
                if (!nextPosition.isValid()) {
                    break;
                }
                if ((piece = board.getPieceAt(nextPosition)) != null) {
                    if (piece.getColor() != getColor())
                        moves.add(nextPosition);
                    break;
                }
                moves.add(nextPosition);
            } while (repeat);
        }
        // Check if it leaves King in check
        if (deepCheck) {
            Iterator<Position> it = moves.iterator();
            while (it.hasNext()) {
                Position m = it.next();
                // Copy of board to make move
                TreeSet<ChessPair<Position, Piece>> piecesCopy = new TreeSet<>(board.pieces);
                Board boardCopy = new Board(piecesCopy);

                // Capture piece if applicable
                Piece capturedPiece = boardCopy.getPieceAt(m);
                if (capturedPiece != null) {
                    ChessPair<Position, Piece> capturedPair = new ChessPair<>(m, capturedPiece);
                    boardCopy.pieces.remove(capturedPair);
                }

                // Make move
                ChessPair<Position, Piece> chessPair = new ChessPair<>(getPosition(), this);
                boardCopy.pieces.remove(chessPair);
                chessPair.setKey(m);
                boardCopy.pieces.add(chessPair);

                if (type() == 'K') {
                    if (getColor() == Colors.WHITE) {
                        boardCopy.whiteKing = m;
                    } else {
                        boardCopy.blackKing = m;
                    }
                }

                if (boardCopy.checkForCheck(getColor())) {
                    it.remove();
                }
            }
        }
        return moves;
    }

    public List<Position> getPossibleMoves(Board board, boolean check) {
        return moveStrategy.getPossibleMoves(board, position, check);
    }

    public List<Position> getPossibleMoves(Board board) {
        return getPossibleMoves(board,true);
    }

    @Override
    public boolean checkForCheck(Board board, Position kingPosition) {
        return getPossibleMoves(board, false).contains(kingPosition);
    }

    public Colors getColor() {
        return color;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position newPosition) {
        position = newPosition;
    }

    public void setMoveStrategy(MoveStrategy moveStrategy) {
        this.moveStrategy = moveStrategy;
    }
}
