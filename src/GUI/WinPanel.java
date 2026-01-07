package GUI;

import Base.Game;
import Base.GameState;

import javax.swing.*;
import java.awt.*;

public class WinPanel extends JPanel {
    JLabel resultLabel;
    JLabel pointsEarned;
    JLabel totalPoints;
    Button playAgain;

    ImageIcon table = new ImageIcon("Images/table.png");
    ImageIcon cover = new ImageIcon("Images/cover.png");

    public WinPanel(){
        super(new GridLayout(1,2));
        resultLabel = new JLabel();
        pointsEarned = new JLabel();
        totalPoints = new JLabel();

        playAgain = new Button("Play Again");

        JPanel background = new JPanel(){
            @Override
            public void paintComponent(Graphics g){
                super.paintComponent(g);
                g.drawImage(table.getImage(),0,0,this);
            }
        };

        JPanel loginPanel = new JPanel(){
            @Override
            public void paintComponent(Graphics g){
                super.paintComponent(g);
                g.drawImage(cover.getImage(),0,0,getWidth(),getHeight(),this);
            }
        };

        loginPanel.setLayout(new BoxLayout(loginPanel,BoxLayout.Y_AXIS));

        loginPanel.add(resultLabel);
        loginPanel.add(pointsEarned);
        loginPanel.add(totalPoints);
        loginPanel.add(playAgain);

        add(loginPanel);
        add(background);
    }



    public void updateContent(GameState state, Game game){

    }
}
