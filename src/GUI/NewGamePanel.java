package GUI;

import Base.Game;
import Base.Main;
import Base.Player;
import Base.User;
import Pieces.Colors;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class NewGamePanel extends JPanel implements ActionListener {
    JLabel title,nick;
    TextField nickname;
    RadioButton black, white;
    PaperButton startGame;
    NewGamePanel(){
        super();
        setBorder(new EmptyBorder(100,100,100,50));
        title = new JLabel("New Game");

        Font oldEnglish, libre;
        try {
            oldEnglish = Font.createFont(Font.TRUETYPE_FONT, new File("Fonts/oldEnglish.ttf")).deriveFont(80f);
            title.setFont(oldEnglish);
        }catch (Exception ignored){}
        title.setForeground(Color.decode("#18161B"));

        nick= new JLabel("Nickname:");
        nickname = new TextField(15){
            @Override
            protected void paintBorder(Graphics g) {
                int radius = 20;
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);

                g2.dispose();
            }
        };
        nickname.setMaximumSize(nickname.getPreferredSize());

        try {
            libre = Font.createFont(Font.TRUETYPE_FONT, new File("Fonts/libre.ttf")).deriveFont(30f);
            nick.setFont(libre);
        }catch (Exception ignored){}

        black = new RadioButton("Black");
        black.setSelectedBg(new Color(0, 0, 0, 60));
        white = new RadioButton("White");
        startGame = new PaperButton("Start");
        ButtonGroup colorSelection = new ButtonGroup();

        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        black.setAlignmentX(Component.CENTER_ALIGNMENT);
        white.setAlignmentX(Component.CENTER_ALIGNMENT);
        startGame.setAlignmentX(Component.CENTER_ALIGNMENT);
        nick.setAlignmentX(Component.CENTER_ALIGNMENT);
        nickname.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.setOpaque(false);
        buttonPanel.add(white);
        buttonPanel.add(black);


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
