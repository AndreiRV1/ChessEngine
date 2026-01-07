package GUI;

import Base.Game;
import Base.GameState;
import Base.Main;
import Base.Observers.GUIObserver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Window extends JFrame implements ActionListener, GUIObserver {
    CardLayout cardLayout;
    LoginPanel loginPanel;
    GamePannel gamePanel;
    MenuPanel menuPanel;
    WinPanel winPanel;

    public Window(String title) {
        super(title);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setMinimumSize(new Dimension(1600,1024));
        setPreferredSize(new Dimension(1600,1024));
        cardLayout = new CardLayout();
        setLayout(cardLayout);

        loginPanel = new LoginPanel();
        loginPanel.addObserver(this);

        menuPanel = new MenuPanel();
        menuPanel.addObserver(this);


        gamePanel = new GamePannel();
        gamePanel.addObserver(this);

        winPanel = new WinPanel();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    Main.getInstance().write();
                } catch (Exception ex) {
                    System.out.println("Something went wrong while saving, no progress saved.");
                }
                dispose();
            }
        });

        add("Login", loginPanel);
        add("Menu", menuPanel);
        add("Game", gamePanel);
        add("Win", winPanel);
        pack();
        setVisible(true);
    }


    @Override
    public void actionPerformed(ActionEvent e) {

    }

    public void showWinPage(GameState state, Game game){
        cardLayout.show(getContentPane(), "Win");
        winPanel.updateContent(state,game);
    }

    @Override
    public void updatePage(int event) {
        switch (event) {
            case 0:
                cardLayout.show(getContentPane(), "Login");
                loginPanel.updateContent();
                pack();
                break;
            case 1:
                cardLayout.show(getContentPane(),"Menu");
                menuPanel.updateContent();
                pack();
                break;
            case 2:
                cardLayout.show(getContentPane(), "Game");
                gamePanel.updateContent();
                pack();
                break;
            default:
                break;
        }
        repaint();
    }

    public void setGame(Game game){
        gamePanel.game = game;
    }
}
