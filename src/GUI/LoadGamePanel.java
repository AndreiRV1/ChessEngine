package GUI;

import Base.Game;
import Base.Main;
import Base.User;
import Pieces.Colors;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentListener;

public class LoadGamePanel extends JPanel implements ActionListener {
    JComboBox<Long> games;
    JLabel player1, player2;
    JScrollPane scroll;
    JTextArea history;
    JButton load;
    Game game;

    LoadGamePanel(){
        super();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        history = new JTextArea();
        scroll = new JScrollPane(history);
        load  = new JButton("Load");
        player1 = new JLabel("Player 1");
        player2 = new JLabel("Player 2");
        games = new JComboBox<>();

        games.addActionListener(this);
        load.addActionListener(this);
        games.setPreferredSize(new Dimension(50,25));
        games.setMaximumSize(games.getPreferredSize());

        games.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(games);
        add(player1);
        add(player2);
        add(scroll);
        add(load);
    }

    public void updateContent(){
        User currentUser = Main.getInstance().getCurrentUser();
        for(Game g:currentUser.getActiveGames()){
            games.addItem(g.getId());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == games) {
            User currentUser = Main.getInstance().getCurrentUser();
            for (Game g : currentUser.getActiveGames()) {
                if (g.getId() == (Long) games.getSelectedItem()) {
                    game = g;
                }
            }
            player1.setText(game.getPlayer(Colors.WHITE).toString());
            player2.setText(game.getPlayer(Colors.BLACK).toString());
        }
        if(e.getSource() == load){
            Main.getInstance().loadGame(game);
        }
    }
}
