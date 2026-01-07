package GUI;

import Base.Game;
import Base.Main;
import Base.Player;
import Base.User;
import Board.Move;
import Pieces.Colors;

import javax.swing.*;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Map;

public class LoadGamePanel extends JPanel implements ActionListener {
    ComboBox<Long> games;
    JLabel player1, player2;
    JScrollPane scroll;
    JTextArea history;
    PaperButton load;
    Game game;
    GUIBoard GUIBoard;

    LoadGamePanel(){
        super();
        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);

        JLabel title = new JLabel("Load Game");

        Font oldEnglish, libre;
        try {
            oldEnglish = Font.createFont(Font.TRUETYPE_FONT, new File("Fonts/oldEnglish.ttf")).deriveFont(80f);
            title.setFont(oldEnglish);
        }catch (Exception ignored){}
        title.setForeground(Color.decode("#18161B"));

        history = new JTextArea(10,10);
        history.setOpaque(false);
        history.setFont(new Font("Serif", Font.ITALIC, 16));
        history.setForeground(new Color(40, 35, 30));
        history.setEditable(false);

        scroll = new JScrollPane(history);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(92, 64, 51),5));
        scroll.setPreferredSize(new Dimension(100,200));
        scroll.setMinimumSize(new Dimension(100,200));

        scroll.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0));
        scroll.getVerticalScrollBar().setBackground(new Color(245, 245, 242));

        load  = new PaperButton("Load");
        player1 = new JLabel("Player 1");
        player1.setFont(new Font("Serif", Font.ITALIC, 16));
        player1.setForeground(new Color(40, 35, 30));


        player2 = new JLabel("Player 2");
        player2.setFont(new Font("Serif", Font.ITALIC, 16));
        player2.setForeground(new Color(40, 35, 30));

        games = new ComboBox<>();

        games.addActionListener(this);
        load.addActionListener(this);
        games.setPreferredSize(new Dimension(50,25));
        games.setMaximumSize(games.getPreferredSize());

        games.setAlignmentX(Component.CENTER_ALIGNMENT);

        GUIBoard = new GUIBoard(false,300);

        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGap(50)
                .addComponent(title)
                .addComponent(games)
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(player1)
                .addComponent(player2)
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(GUIBoard)
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(scroll)
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(load)
                .addGap(100)
        );
        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGap(0,0,Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(title)
                        .addComponent(games)
                        .addComponent(player1)
                        .addComponent(player2)
                        .addComponent(GUIBoard)
                        .addComponent(scroll)
                        .addComponent(load))
                .addGap(100)
        );
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
                try{
                if (g.getId() == (Long) games.getSelectedItem()) {
                    game = g;
                }}catch (Exception ignored){}
            }
            player1.setText(game.getPlayer(Colors.WHITE).toString());
            player2.setText(game.getPlayer(Colors.BLACK).toString());
            GUIBoard.updateContent(game);
            history.setText("");
            int i = 1;
            for (Map.Entry<Move, Player> set : game.moveHistory.entrySet()){
                history.append(i + ". " + set.getKey().toString() + "\n");
                i++;
            }
        }
        if(e.getSource() == load){
            Main.getInstance().loadGame(game);
        }
    }
}
