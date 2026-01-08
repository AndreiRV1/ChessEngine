package Base;

import Board.Board;
import Board.ChessPair;
import Board.Position;
import Exceptions.InvalidCommandException;
import Pieces.*;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class Player {
    public String  name;
    public Colors color;
    private List<Piece> capturedPieces;
    private TreeSet<ChessPair<Position,Piece>> pieces;
    private int points;
    public Player(String name, Colors color) {
        this.name = name;
        this.color = color;
        capturedPieces = new ArrayList<>();
        points = 0;
    }

    public Player(String name, String color) {
        this(name,Colors.valueOf(color));
    }

    public ChessPair<String,Piece> makeMove(Position from, Position to, Board board) {
        if(board.getPieceAt(from) == null || board.getPieceAt(from).getColor() != color){
            throw new InvalidCommandException("Invalid piece");
        }
        if(!board.isValidMove(from, to)){
            throw new InvalidCommandException("Invalid move");
        }
        String specialMove="";
        if(board.movePiece(from,to,!name.equals("computer")) == 1){
            specialMove="P to "+board.getPieceAt(to);
        }
        // Captured pieces go to a0
        Piece capturedPiece = board.getPieceAt(new Position('a',0));

        if(capturedPiece != null){
            board.pieces.remove(new ChessPair<Position, Piece>(new Position('a',0),capturedPiece));
        }
        return new ChessPair<String, Piece>(specialMove,capturedPiece);
    }

    public void setPieces(TreeSet<ChessPair<Position,Piece>> pieces) {
        this.pieces = pieces;
    }

    public void addCapturedPiece(Piece p){
        capturedPieces.add(p);
    }

    public List<Piece> getCapturedPieces() {
        return capturedPieces;
    }
    public List<ChessPair<Position,Piece>> getOwnedPieces(Board board) {
        return board.getPieces(color).stream().toList();
    }

    public int getPoints() {
        return points;
    }
    public void setPoints(int points) {
        this.points = points;
    }
    public void addPoints(int points){
        this.points += points;
    }

    @Override
    public String toString() {
        return name + "      " + (color == Colors.WHITE ? "○ : " : "◉ : ")  + points + "p";
    }
}
