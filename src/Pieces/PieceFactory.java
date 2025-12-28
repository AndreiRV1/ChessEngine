package Pieces;

import Board.Position;
import Exceptions.InvalidCommandException;
import Exceptions.InvalidMoveException;


public class PieceFactory {

    public static Piece factory(String pieceType, String color, String position) throws IllegalArgumentException{
        color = color.toUpperCase();
        switch (pieceType){
            case "N":
                return new Knight(color, position);
            case "K":
                return new King(color, position);
            case "Q":
                return new Queen(color, position);
            case "R":
                return new Rook(color, position);
            case "P":
                return new Pawn(color, position);
            case "B":
                return new Bishop(color, position);
            default:
                throw new IllegalArgumentException("Illegal piece");
        }
    }

    public static Piece factory(String pieceType, Colors color, Position position) throws InvalidMoveException {
        switch (pieceType){
            case "N":
                return new Knight(color, position);
            case "K":
                return new King(color, position);
            case "Q":
                return new Queen(color, position);
            case "R":
                return new Rook(color, position);
            case "P":
                return new Pawn(color, position);
            case "B":
                return new Bishop(color, position);
            default:
                throw new InvalidMoveException("Illegal piece");
        }
    }
}
