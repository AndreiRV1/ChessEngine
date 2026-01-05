package Board;

import Pieces.Colors;
import Pieces.Piece;

public class Move {
    private Colors color;
    private Position lastPosition;
    private Position nextPosition;
    private Piece capturedPiece;
    private String specialMove;

    public Move(Colors color, Position lastPosition, Position nextPosition, Piece capturedPiece) {
        this(color, lastPosition, nextPosition, capturedPiece, null);
    }

    public Move(Colors color, Position lastPosition, Position nextPosition, Piece capturedPiece, String specialMove) {
        this.color = color;
        this.lastPosition = lastPosition;
        this.nextPosition = nextPosition;
        this.capturedPiece = capturedPiece;
        this.specialMove = specialMove;
    }

    public Move(Colors color, Position lastPosition, Position nextPosition) {
        this(color, lastPosition, nextPosition, null, null);
    }

    public Colors getColor() {
        return color;
    }

    public Position getLastPosition() {
        return lastPosition;
    }

    public Position getNextPosition() {
        return nextPosition;
    }

    public Piece getCapturedPiece() {
        return capturedPiece;
    }

    public String getSpecialMove() {
        return specialMove;
    }

    @Override
    public String toString() {
        return "Color: " + color + " ,last position: " + lastPosition + " ,next position: " + nextPosition + ((capturedPiece != null) ? (" Caputred piece" + capturedPiece) : "") + ((specialMove != null)?specialMove:"");
    }
}
