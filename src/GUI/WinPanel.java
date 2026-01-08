package GUI;

import Base.Game;
import Base.GameState;
import Base.Main;
import Base.Observers.GUIObserver;
import Base.Observers.GUISubject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class WinPanel extends JPanel implements ActionListener, GUISubject {
    JLabel resultLabel;
    JLabel pointsEarned;
    JLabel totalPoints;
    Button playAgain, exit;
    GUIObserver observer;

    ImageIcon table = new ImageIcon("Images/table.png");
    ImageIcon cover = new ImageIcon("Images/cover.png");

    public WinPanel(){
        super(new GridLayout(1,2));
        resultLabel = new JLabel();
        pointsEarned = new JLabel();
        totalPoints = new JLabel();

        Font oldEnglish, libre;
        try {
            oldEnglish = Font.createFont(Font.TRUETYPE_FONT, new File("Fonts/oldEnglish.ttf")).deriveFont(100f);
            libre = Font.createFont(Font.TRUETYPE_FONT, new File("Fonts/libre.ttf")).deriveFont(30f);
            resultLabel.setFont(oldEnglish);
            pointsEarned.setFont(libre);
            totalPoints.setFont(libre);

        }catch (Exception ignored){}
        resultLabel.setForeground(Color.decode("#1A0F00"));

        playAgain = new Button("Play Again");
        exit = new Button("Exit");
        playAgain.addActionListener(this);
        exit.addActionListener(this);

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

        GroupLayout layout = new GroupLayout(loginPanel);
        loginPanel.setLayout(layout);

        layout.setAutoCreateGaps(true);

        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGap(0,0,Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(resultLabel)
                        .addComponent(pointsEarned)
                        .addComponent(totalPoints)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(playAgain)
                                .addComponent(exit)))
                .addGap(0,0,Short.MAX_VALUE)
        );

        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGap(100)
                .addComponent(resultLabel)
                .addGap(0,0,Short.MAX_VALUE)
                .addComponent(pointsEarned)
                .addGap(0,0,Short.MAX_VALUE)
                .addComponent(totalPoints)
                .addGap(0,0,Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(playAgain)
                        .addComponent(exit))
                .addGap(100)
        );

        loginPanel.add(resultLabel);
        loginPanel.add(pointsEarned);
        loginPanel.add(totalPoints);
        loginPanel.add(playAgain);

        add(loginPanel);
        add(background);
    }



    public void updateContent(GameState state, Game game){
        switch (state){
            case GameState.WIN:
                resultLabel.setText("You won");
                break;
            case GameState.DRAW:
                resultLabel.setText("Draw");
                break;
            case GameState.LOSE:
                resultLabel.setText("You lost");
                break;
            case GameState.RESIGN:
                resultLabel.setText("You resigned");
                break;
        }
        pointsEarned.setText("You earned " + game.getHumanPlayer().getPoints() + " points");
        totalPoints.setText("You have" + Main.getInstance().getCurrentUser().getPoints() + " points");

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == playAgain){
            notifyObservers(1);
        }
        if(e.getSource() == exit){
            notifyObservers(0);
        }
    }

    @Override
    public void addObserver(GUIObserver GUIObserver) {
        observer = GUIObserver;
    }

    @Override
    public void removeObserver(GUIObserver GUIObserver) {
        observer = null;
    }

    @Override
    public void notifyObservers(int event) {
        observer.updatePage(event);
    }
}
