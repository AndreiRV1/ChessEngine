package GUI;

import Pieces.Colors;
import Pieces.Piece;

import javax.swing.*;

public class PieceImageFactory {
    static ImageIcon whitePawn, blackPawn;
    static ImageIcon whiteRook, blackRook;
    static ImageIcon whiteKnight, blackKnight;
    static ImageIcon whiteQueen, blackQueen;
    static ImageIcon whiteKing, blackKing;
    static ImageIcon whiteBishop, blackBishop;
    static {
        whitePawn = new ImageIcon("Images/pawnW2.png");
        blackPawn = new ImageIcon("Images/pawnB2.png");
        whiteRook = new ImageIcon("Images/rookW2.png");
        blackRook = new ImageIcon("Images/rookB2.png");
        whiteKnight = new ImageIcon("Images/knightW2.png");
        blackKnight = new ImageIcon("Images/knightB2.png");
        whiteQueen = new ImageIcon("Images/queenW2.png");
        blackQueen = new ImageIcon("Images/queenB2.png");
        whiteKing = new ImageIcon("Images/kingW2.png");
        blackKing = new ImageIcon("Images/kingB2.png");
        whiteBishop = new ImageIcon("Images/bishopW2.png");
        blackBishop = new ImageIcon("Images/bishopB2.png");
    }

    public static ImageIcon factory(Piece p){
        if(p.getColor() == Colors.WHITE){
            switch (p.type()) {
                case 'R':
                    return whiteRook;
                case 'B':
                    return whiteBishop;
                case 'Q':
                    return whiteQueen;
                case 'K':
                    return whiteKing;
                case 'P':
                    return whitePawn;
                case 'N':
                    return whiteKnight;
            }
        }else {
            switch (p.type()) {
                case 'R':
                    return blackRook;
                case 'B':
                    return blackBishop;
                case 'Q':
                    return blackQueen;
                case 'K':
                    return blackKing;
                case 'P':
                    return blackPawn;
                case 'N':
                    return blackKnight;
            }
        }
        return null;
    }
}
