package Pieces;

import Board.Board;
import Board.Position;

import java.util.ArrayList;
import java.util.List;


public class Pawn extends Piece {
    public Pawn(Colors color, Position position) {
        super(color, position);
    }

    public Pawn(String color, String position) {
        super(color, position);
    }


    @Override
    public List<Position> getPossibleMoves(Board board) {
        List<Position> possibleMoves = new ArrayList<>();
        Position position = getPosition();
        int direction = 1;
        if (getColor() == Colors.BLACK)
            direction = -1;

        Position nextPosition = new Position(position.x, position.y + direction);

        // Move forward 1/2 spaces
        if (board.getPieceAt(nextPosition) == null)
            possibleMoves.add(new Position(position.x, position.y + direction));
        nextPosition.y += direction;
        if (!hasMoved && board.getPieceAt(nextPosition) == null)
            possibleMoves.add(new Position(position.x, position.y + 2 * direction));
        // Diagonally attacl
        nextPosition.x = position.x;
        nextPosition.x += 1;
        nextPosition.y = position.y + direction;
        if (board.getPieceAt(nextPosition) != null)
            possibleMoves.add(new Position((char) (position.x + 1), position.y + direction));
        nextPosition.x -= 2;
        if (board.getPieceAt(nextPosition) != null)
            possibleMoves.add(new Position((char) (position.x - 1), position.y + direction));
        return possibleMoves;
    }

    @Override
    public List<Position> getPossibleMoves(Board board, boolean check) {
        return getPossibleMoves(board);
    }

    @Override
    public char type() {
        return 'P';
    }

    public String toString() {
        return "" + 'P' + '-' + ((getColor() == Colors.WHITE) ? "W" : "B");
    }
}
