package GUI;

import Base.Game;
import Pieces.Colors;
import Pieces.Piece;
import Board.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;

public class GUIBoard extends JPanel implements ActionListener {
    JButton[][] buttons_copy = new JButton[8][8];
    JButton[][] buttons = new JButton[8][8];
    Colors crtColor = Colors.WHITE;
    ImageIcon whiteTile, blackTile;
    Board board;
    Game game;
    Position selected;
    java.util.List<Position> positions;

    private void setColor(JButton button){
        button.setBackground(Color.WHITE);
        if(crtColor == Colors.WHITE){
            crtColor = Colors.BLACK;
        }else{
            button.setBackground(Color.BLACK);
            crtColor = Colors.WHITE;
        }
    }

    private void resetColor(){
        for (int i = 0; i < buttons_copy.length; i++) {
            if(i % 2 == 0)
                crtColor = Colors.WHITE;
            else
                crtColor = Colors.BLACK;
            for (int j = 0; j < buttons_copy[i].length; j++) {
                setColor(buttons_copy[i][j]);
            }
        }
    }

    public void setSize(int size){
        setMaximumSize(new Dimension(size,size));
        setMinimumSize(new Dimension(size,size));
        setPreferredSize(new Dimension(size,size));
        revalidate();
    }

    public GUIBoard(boolean isInteractable, int size) {
        setLayout(new GridLayout(8, 8,0,0));
        setSize(size);
        for (int i = 0; i < buttons_copy.length; i++) {
            if(i % 2 == 0)
                crtColor = Colors.WHITE;
            else
                crtColor = Colors.BLACK;
            for (int j = 0; j < buttons_copy[i].length; j++) {
                buttons_copy[i][j] = new JButton();
                buttons_copy[i][j].setEnabled(isInteractable);
                buttons_copy[i][j].addActionListener(this);
                setColor(buttons_copy[i][j]);
            }
        }
    }

    public void updateContent(Game game){
        this.board = game.getBoard();
        this.game = game;

        removeAll();

        if(game.getHumanPlayer().color == Colors.WHITE) {
            JButton[][] flippedButtons = new JButton[8][8];
            for (int i = 0; i < 8; i++) {
                flippedButtons[i] = buttons_copy[7 - i];
            }
            buttons = flippedButtons;
        } else {
            buttons = buttons_copy;
        }
        for (int i = 0; i < buttons_copy.length; i++) {
            for (int j = 0; j < buttons_copy[i].length; j++) {
                this.add(buttons[i][j]);
            }
        }

        game.checkEndOfGame();
        Iterator<ChessPair<Position, Piece>> iterator = board.pieces.iterator();
        ChessPair<Position, Piece> chessPair;
        Position pos;
        for(int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                buttons_copy[i][j].setIcon(null);
            }
        }
        while (iterator.hasNext()) {
            chessPair = iterator.next();
            pos = chessPair.getKey();
            buttons_copy[pos.y - 1][(int) (pos.x - 'A')].setIcon(PieceImageFactory.factory(chessPair.getValue()));
            buttons_copy[pos.y - 1][(int) (pos.x - 'A')].setDisabledIcon(PieceImageFactory.factory(chessPair.getValue()));
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int x = 0, y = 0;
        resetColor();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (buttons_copy[i][j].equals(e.getSource())) {
                    x = j;
                    y = i;
                    buttons_copy[i][j].setBackground(Color.GREEN);
                }
            }
        }
        if (selected == null) {
            System.out.println(new Position((char) (x + 'A'), y + 1).toString());
            try {
                Piece piece = board.getPieceAt(new Position((char) (x + 'A'), y + 1));
                if (piece.getColor() == game.getHumanPlayer().color) {
                    positions = piece.getPossibleMoves(board);
                    selected = new Position((char) (x + 'A'), y + 1);
                } else {
                    positions = null;
                }

                for (Position pos : positions) {
                    buttons_copy[pos.y - 1][(int) (pos.x - 'A')].setBackground(Color.GREEN);
                }
            } catch (Exception ex) {
            }
        } else {
            if (positions.contains(new Position((char) (x + 'A'), y + 1))) {
                game.makeMove(selected, new Position((char) (x + 'A'), y + 1));
                updateContent(game);
                selected = null;
                if (game.isInMate == null && !game.getCurrentPlayer().equals(game.getHumanPlayer())) {
                    ChessPair<Position, Position> computerChoice = game.choseComputerMove();
                    if (computerChoice.getKey() != null && computerChoice.getValue() != null) {
                        game.makeMove(computerChoice.getKey(), computerChoice.getValue());
                        updateContent(game);
                    }
                }
            } else {
                resetColor();
                selected = null;
            }
        }
    }
}
