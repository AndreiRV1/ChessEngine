package GUI;

import Base.Game;
import Base.Main;
import Base.Player;
import Base.User;
import Pieces.Colors;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class NewGamePanel extends JPanel implements ActionListener {
    JLabel title,nick;
    JTextField nickname;
    JRadioButton black, white;
    JButton startGame;
    NewGamePanel(){
        super();
        title = new JLabel("New Game");
        nick= new JLabel("Nickname:");
        nickname = new JTextField(15);
        nickname.setMaximumSize(nickname.getPreferredSize());

        nickname.setText("Nickname");
        black = new JRadioButton("Black");
        white = new JRadioButton("White");
        startGame = new JButton("Start");
        ButtonGroup colorSelection = new ButtonGroup();

        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        black.setAlignmentX(Component.CENTER_ALIGNMENT);
        white.setAlignmentX(Component.CENTER_ALIGNMENT);
        startGame.setAlignmentX(Component.CENTER_ALIGNMENT);
        nick.setAlignmentX(Component.CENTER_ALIGNMENT);
        nickname.setAlignmentX(Component.CENTER_ALIGNMENT);

        setBorder(BorderFactory.createEmptyBorder(15,5,15,5));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(black);
        buttonPanel.add(white);


        white.setSelected(true);
        colorSelection.add(white);
        colorSelection.add(black);
        setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        add(title);
        add(Box.createVerticalGlue());
        add(nick);
        add(Box.createRigidArea(new Dimension(10,10)));
        add(nickname);
        add(Box.createRigidArea(new Dimension(10,10)));
        add(buttonPanel);
        add(Box.createVerticalGlue());
        add(startGame);
        startGame.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==startGame){
            Colors colour = Colors.WHITE;
            if(black.isSelected()){
                colour = Colors.BLACK;
            }
            List<Player> players = new ArrayList<>();

            players.add(new Player(nickname.getText(), colour));

            players.add(new Player("computer", (colour == Colors.WHITE ? Colors.BLACK : Colors.WHITE)));
            Game newGame = new Game(players);
            Main.getInstance().createGame(newGame);
        }
    }
}
