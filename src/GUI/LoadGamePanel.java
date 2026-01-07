package GUI;

import Base.Game;
import Base.Main;
import Base.Player;
import Base.User;
import Board.Move;
import Pieces.Colors;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class LoadGamePanel extends JPanel implements ActionListener {
    JComboBox<Long> games;
    JLabel player1, player2;
    JScrollPane scroll;
    JTextArea history;
    JButton load;
    Game game;
    GUIBoard GUIBoard;

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

        GUIBoard = new GUIBoard(false,400);

        add(games);
        add(player1);
        add(player2);
        add(GUIBoard);
        add(scroll);
        add(load);
    }

    public void updateContent(){
        User currentUser = Main.getInstance().getCurrentUser();
        games.removeAllItems();
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
            GUIBoard.updateContent(game);
            history.setText("");
            for (Map.Entry<Move, Player> set : game.moveHistory.entrySet()){
                history.append(set.getValue().name + ": " + set.getKey().toString() + "\n");
            }
        }
        if(e.getSource() == load){
            Main.getInstance().loadGame(game);
        }
    }
}
