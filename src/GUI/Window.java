package GUI;

import Base.Observer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Window extends JFrame implements ActionListener, Observer {
    CardLayout cardLayout;
    LoginPanel loginPanel;
    JPanel gamePanel;
    MenuPanel menuPanel;
    JPanel winPanel;
    JPanel exitPanel;

    public Window(String title) {
        super(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(500,500));
        cardLayout = new CardLayout();
        setLayout(cardLayout);

        loginPanel = new LoginPanel();
        loginPanel.addObserver(this);

        menuPanel = new MenuPanel();
        menuPanel.addObserver(this);


        gamePanel = new JPanel();
        winPanel = new JPanel();
        exitPanel = new JPanel();

        add("Login", loginPanel);
        add("Menu", menuPanel);
        add("Base.Game", gamePanel);
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
                cardLayout.next(getContentPane());
                menuPanel.updateContent();
                break;
            default:
                break;
        }
        repaint();
    }
}
