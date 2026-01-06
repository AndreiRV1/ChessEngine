package GUI;

import Base.Game;
import Base.Observer;
import Base.Player;
import Base.Subject;
import Board.Move;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class GamePannel extends JPanel implements ActionListener, Subject {
    Button menuButton, exitButton, resignButton;
    JLabel title;
    JLabel points;
    JPanel titlePannel;
    JPanel buttonPannel;
    JScrollPane scroll;
    JTextArea history;
    JPanel capturedPieces;
    GUIBoard board;
    Game game;

    Observer observer;

    public GamePannel() {
        setLayout(new BorderLayout());
        titlePannel = new JPanel();
        titlePannel.setLayout(new BorderLayout());
        title = new JLabel("The Game");
        points = new  JLabel("0");
        titlePannel.add(title, BorderLayout.CENTER);
        titlePannel.add(points, BorderLayout.EAST);
        add(titlePannel, BorderLayout.NORTH);
        history = new JTextArea();
        history.setEditable(false);
        scroll = new JScrollPane(history);
        capturedPieces = new JPanel();
        add(capturedPieces, BorderLayout.EAST);
        add(scroll, BorderLayout.WEST);
        board = new GUIBoard(true);
        menuButton = new Button("Menu");
        menuButton.addActionListener(this);
        exitButton = new Button("Exit");
        exitButton.addActionListener(this);
        resignButton = new Button("Resign");
        resignButton.addActionListener(this);

        buttonPannel = new JPanel();
        buttonPannel.setLayout(new GridLayout(1, 3));
        buttonPannel.add(resignButton);
        buttonPannel.add(menuButton);
        buttonPannel.add(exitButton);
        add(buttonPannel, BorderLayout.SOUTH);
        add(board, BorderLayout.CENTER);
    }

    void updateContent(){
        board.updateContent(game);
        history.setText("");
        for (Map.Entry<Move, Player> set : game.moveHistory.entrySet()){
            history.append(set.getValue().name + ": " + set.getKey().toString() + "\n");
        }
        points.setText(game.getHumanPlayer().getPoints() + "");
        //todo captured pieces
    }

    @Override
    public void addObserver(Observer observer) {
        this.observer = observer;
    }

    @Override
    public void removeObserver(Observer observer) {
        this.observer = null;
    }

    @Override
    public void notifyObservers(int event) {
        observer.update(event);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == menuButton){
            notifyObservers(1);
        }
        if(e.getSource() == exitButton){
            notifyObservers(0); //todo change to exit panel
        }
        //todo resign
    }
}
