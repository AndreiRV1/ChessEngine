package Board;

import Base.Main;
import Exceptions.InvalidCommandException;
import Exceptions.InvalidMoveException;
import Pieces.*;

import java.awt.*;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;
import java.util.TreeSet;

public class Board {
    public final TreeSet<ChessPair<Position, Piece>> pieces;
    public Position blackKing;
    public Position whiteKing;

    public Board() {
        pieces = new TreeSet<>();
    }

    public Board(TreeSet<ChessPair<Position, Piece>> pieces) {
        this.pieces = pieces;
        for (ChessPair<Position, Piece> p : pieces) {
            if (p.getValue().type() == 'K') {
                if (p.getValue().getColor() == Colors.WHITE) {
                    whiteKing = p.getValue().getPosition();
                } else {
                    blackKing = p.getValue().getPosition();
                }
            }
        }
    }

    public ChessPair<Position, Piece> createPiece(String type, String color, String position) {
        Position pos = new Position(position);
        return new ChessPair<>(pos, PieceFactory.factory(type,color,position));
    }

    public void initialize() {
        ChessPair<Position, Piece> chessPair;
        Position position;
        for (int i = 0; i < 8; i++) {
            pieces.add(createPiece("P", "white", (char) ('A' + i) + "2"));
        }

        for (int i = 0; i < 8; i++) {
            pieces.add(createPiece("P", "black", (char) ('A' + i) + "7"));
        }

        pieces.add(createPiece("R", "white", "A1"));
        pieces.add(createPiece("N", "white", "B1"));
        pieces.add(createPiece("B", "white", "C1"));
        pieces.add(createPiece("Q", "white", "D1"));
        pieces.add(createPiece("K", "white", "E1"));
        pieces.add(createPiece("B", "white", "F1"));
        pieces.add(createPiece("N", "white", "G1"));
        pieces.add(createPiece("R", "white", "H1"));

        pieces.add(createPiece("R", "black", "A8"));
        pieces.add(createPiece("N", "black", "B8"));
        pieces.add(createPiece("B", "black", "C8"));
        pieces.add(createPiece("Q", "black", "D8"));
        pieces.add(createPiece("K", "black", "E8"));
        pieces.add(createPiece("B", "black", "F8"));
        pieces.add(createPiece("N", "black", "G8"));
        pieces.add(createPiece("R", "black", "H8"));

        blackKing = new Position('E', 8);
        whiteKing = new Position('D', 1);
    }

    public void addPiece(Piece piece) {
        ChessPair<Position, Piece> chessPair = new ChessPair(piece.getPosition(), piece);
        pieces.add(chessPair);
        if (piece.type() == 'K') {
            if (piece.getColor() == Colors.WHITE)
                whiteKing = piece.getPosition();
            else
                blackKing = piece.getPosition();
        }
    }

    public TreeSet<ChessPair<Position, Piece>> getPieces(Colors color) {
        TreeSet<ChessPair<Position, Piece>> playerPieces = new TreeSet<>();
        Iterator<ChessPair<Position, Piece>> iterator = pieces.iterator();
        ChessPair<Position, Piece> chessPair;
        while (iterator.hasNext()) {
            chessPair = iterator.next();
            if (chessPair.getValue().getColor() == color) {
                playerPieces.add(chessPair);
            }
        }
        return playerPieces;
    }

    public boolean checkForCheck(Colors color) {
        Colors opposite = (color == Colors.WHITE) ? Colors.BLACK : Colors.WHITE;
        Position kingPos = (color == Colors.WHITE) ? whiteKing : blackKing;
        for (ChessPair<Position, Piece> chessPair : getPieces(opposite)) {
            if (chessPair.getValue().checkForCheck(this, kingPos)) {
                return true;
            }
        }
        return false;
    }

    public Piece getPieceAt(Position position) {
        Iterator<ChessPair<Position, Piece>> iterator = pieces.iterator();
        ChessPair<Position, Piece> chessPair;
        while (iterator.hasNext()) {
            chessPair = iterator.next();
            if (chessPair.getKey().equals(position)) {
                return chessPair.getValue();
            }
        }
        return null;
    }

    public int movePiece(Position from, Position to,boolean player) throws InvalidMoveException {
        int ret = 0;
        if (!isValidMove(from, to)) {
            throw new InvalidMoveException("Invalid move: " + from + " - " + to);
        }
        Piece piece = getPieceAt(from);
        Piece capturedPiece = getPieceAt(to);
        if (capturedPiece != null && capturedPiece.getColor() != piece.getColor()) {
            //Capture
            ChessPair<Position, Piece> capturedPair = new ChessPair(to, capturedPiece);
            pieces.remove(capturedPair);
            capturedPair.setKey(new Position('a', 0));
            pieces.add(capturedPair);
        }

        piece.setPosition(to);
        piece.hasMoved = true;
        ChessPair<Position, Piece> chessPair = new ChessPair(from, piece);
        pieces.remove(chessPair);
        chessPair.setKey(to);

        // Change Pawn into other piece if it reached the other side
        if (piece.type() == 'P') {
            if (piece.getColor() == Colors.WHITE && to.y == 8 || piece.getColor() == Colors.BLACK && to.y == 1) {
                String newPiece = "";
                if(player) {
                    newPiece = String.valueOf(Main.getInstance().ui.changePiece());
                }else{
                    newPiece = String.valueOf("QNBR".charAt(new Random().nextInt(4)));
                    System.out.println("Computer chose to change P into " + newPiece);
                }
                piece = PieceFactory.factory(String.valueOf(newPiece.charAt(0)), piece.getColor(), piece.getPosition());
                chessPair.setValue(piece);
                ret = 1;
            }
        }
        pieces.add(chessPair);
        return ret;
    }

    public boolean isValidMove(Position from, Position to) {
        if (from == null || to == null) {
            throw new NullPointerException();
        }
        if (!from.isValid() || !to.isValid()) {
            return false;
        }
        if (getPieceAt(from) == null) {
            return false;
        }
        return getPieceAt(from).getPossibleMoves(this).contains(to);
    }

    public String toString(Colors color) {
        boolean reverse = color == Colors.WHITE;
        String[][] board = new String[8][8];
        StringBuilder output = new StringBuilder();
        Iterator<ChessPair<Position, Piece>> iterator = pieces.iterator();
        ChessPair<Position, Piece> chessPair;
        Position pos;
        while (iterator.hasNext()) {
            chessPair = iterator.next();
            pos = chessPair.getKey();
            board[pos.y - 1][(int) (pos.x - 'A')] = chessPair.getValue().toString();
        }
        output.append(" __________________________________\n");
        int i = (reverse ? 7 : 0);
        while (i >= 0 && i < 8) {
            output.append(i + 1);
            output.append("|");
            for (int j = 0; j < 8; j++) {
                if (board[i][j] != null)
                    output.append(board[i][j]).append(" ");
                else
                    output.append("... ");
            }
            output.append("|\n");
            i += (reverse ? -1 : 1);
        }
        output.append(" __________________________________\n");
        output.append("   A   B   C   D   E   F   G   H\n");
        return output.toString();
    }

    @Override
    public String toString() {
        return toString(Colors.WHITE);
    }
}
