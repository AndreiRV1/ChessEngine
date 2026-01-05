package GUI;

import Pieces.Colors;
import Pieces.Piece;
import Board.*;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;

public class GUIBoard extends JPanel {
    JButton[][] buttons = new JButton[8][8];
    Colors crtColor = Colors.WHITE;
    ImageIcon whiteTile, blackTile;

    private void setColor(JButton button){
        button.setBackground(Color.WHITE);
        if(crtColor == Colors.WHITE){
            crtColor = Colors.BLACK;
        }else{
            button.setBackground(Color.BLACK);
            crtColor = Colors.WHITE;
        }
    }

    public GUIBoard(boolean isInteractable) {
        setLayout(new GridLayout(8, 8,0,0));
        setMaximumSize(new Dimension(512,512));
        setMinimumSize(new Dimension(512,512));
        for (int i = 0; i < buttons.length; i++) {
            if(i % 2 == 0)
                crtColor = Colors.WHITE;
            else
                crtColor = Colors.BLACK;
            for (int j = 0; j < buttons[i].length; j++) {
                buttons[i][j] = new JButton();
                buttons[i][j].setEnabled(isInteractable);
                setColor(buttons[i][j]);
                this.add(buttons[i][j]);
            }
        }
    }

    public void updateContent(Board board){
        Iterator<ChessPair<Position, Piece>> iterator = board.pieces.iterator();
        ChessPair<Position, Piece> chessPair;
        Position pos;
        for(int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                buttons[i][j].setIcon(null);
            }
        }
        while (iterator.hasNext()) {
            chessPair = iterator.next();
            pos = chessPair.getKey();
            buttons[pos.y - 1][(int) (pos.x - 'A')].setIcon(PieceImageFactory.factory(chessPair.getValue()));
            buttons[pos.y - 1][(int) (pos.x - 'A')].setDisabledIcon(PieceImageFactory.factory(chessPair.getValue()));
        }
    }

}
