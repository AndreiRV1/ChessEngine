package GUI;

import Base.Game;
import Base.Observer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Window extends JFrame implements ActionListener, Observer {
    CardLayout cardLayout;
    LoginPanel loginPanel;
    GamePannel gamePanel;
    MenuPanel menuPanel;
    JPanel winPanel;
    JPanel exitPanel;

    public Window(String title) {
        super(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1600,1024));
        cardLayout = new CardLayout();
        setLayout(cardLayout);

        loginPanel = new LoginPanel();
        loginPanel.addObserver(this);

        menuPanel = new MenuPanel();
        menuPanel.addObserver(this);


        gamePanel = new GamePannel();
        gamePanel.addObserver(this);

        winPanel = new JPanel();
        exitPanel = new JPanel();

        add("Login", loginPanel);
        add("Menu", menuPanel);
        add("Game", gamePanel);
        add("Exit", exitPanel);
        add("Win", winPanel);
        pack();
        setVisible(true);
    }
    //todo The menu is on the right page, furher contex displayed on the left page(for game creation, etc)

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void update(int event) {
        switch (event) {
            case 0:
                cardLayout.show(getContentPane(), "Login");
                loginPanel.updateContent();
                break;
            case 1:
                cardLayout.show(getContentPane(),"Menu");
                menuPanel.updateContent();
                break;
            case 2:
                cardLayout.show(getContentPane(), "Game");
                gamePanel.updateContent();
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
