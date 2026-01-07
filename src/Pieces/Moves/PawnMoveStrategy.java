package Pieces.Moves;

import Board.*;
import Pieces.Colors;
import Pieces.Piece;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

public class PawnMoveStrategy implements MoveStrategy{
    @Override
    public List<Position> getPossibleMoves(Board board, Position from, boolean recursiveCheck) {
        Piece p = board.getPieceAt(from);
        if (p == null) return new java.util.ArrayList<>();
        List<Position> possibleMoves = new ArrayList<>();

        int direction = 1;
        if (p.getColor() == Colors.BLACK)
            direction = -1;

        Position nextPosition = new Position(from.x, from.y + direction);

        // Move forward 1/2 spaces
        if (board.getPieceAt(nextPosition) == null)
            possibleMoves.add(new Position(from.x, from.y + direction));
        nextPosition.y += direction;
        if (!p.hasMoved && board.getPieceAt(nextPosition) == null)
            possibleMoves.add(new Position(from.x, from.y + 2 * direction));

        // Diagonally attack
        nextPosition.x = from.x;
        nextPosition.x += 1;
        nextPosition.y = from.y + direction;
        if (board.getPieceAt(nextPosition) != null && board.getPieceAt(nextPosition).getColor() != p.getColor())
            possibleMoves.add(new Position((char) (from.x + 1), from.y + direction));
        nextPosition.x -= 2;
        if (board.getPieceAt(nextPosition) != null && board.getPieceAt(nextPosition).getColor() != p.getColor())
            possibleMoves.add(new Position((char) (from.x - 1), from.y + direction));

        if (recursiveCheck) {
            Iterator<Position> it = possibleMoves.iterator();
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
                ChessPair<Position, Piece> chessPair = new ChessPair<>(p.getPosition(), p);
                boardCopy.pieces.remove(chessPair);
                chessPair.setKey(m);
                boardCopy.pieces.add(chessPair);

                if (p.type() == 'K') {
                    if (p.getColor() == Colors.WHITE) {
                        boardCopy.whiteKing = m;
                    } else {
                        boardCopy.blackKing = m;
                    }
                }

                if (boardCopy.checkForCheck(p.getColor())) {
                    it.remove();
                }
            }
        }

        return possibleMoves;
    }
}
