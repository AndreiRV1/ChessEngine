package GUI;

import Base.Main;
import Base.Observer;
import Base.Subject;
import Base.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuPanel extends JPanel implements ActionListener, Subject {
    Observer observer;
    JLabel title;
    JLabel activeGames, points;
    JLabel activeUser;
    JButton newGame, loadGame, logout;
    NewGamePanel newGamePanel;
    LoadGamePanel loadGamePanel;
    JPanel context;

    public MenuPanel() {
        setLayout(new GridLayout(1,2));
        JPanel menu = new JPanel();
        context = new JPanel();
        context.setBackground(Color.MAGENTA);
        GroupLayout layout = new GroupLayout(menu);
        title = new JLabel("The Menu");
        activeGames = new JLabel(" Games");
        points = new JLabel(" Points");
        activeUser = new JLabel("null");
        newGame = new JButton("New Game");
        loadGame = new JButton("Continue");
        logout = new JButton("Logout");
        menu.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setVerticalGroup(layout.createSequentialGroup()
                .addComponent(title)
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(activeUser)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(activeGames, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(points, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                )
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(newGame)
                .addComponent(loadGame)
                .addComponent(logout)
                .addGap(0, 0, Short.MAX_VALUE)
        );

        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(title)
                        .addComponent(activeUser)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(activeGames)
                                .addGap(10)
                                .addComponent(points))
                        .addComponent(newGame)
                        .addComponent(loadGame)
                        .addComponent(logout))
                .addGap(0, 0, Short.MAX_VALUE)
        );

        logout.addActionListener(this);
        newGame.addActionListener(this);
        loadGame.addActionListener(this);

        newGamePanel = new NewGamePanel();
        loadGamePanel = new LoadGamePanel();

        add(context);
        add(menu);

    }


    public void updateContent(){
        User currentUser = Main.getInstance().getCurrentUser();
        activeGames.setText(currentUser.getActiveGames().size() + " Games");
        points.setText(currentUser.getPoints() + " Points");
        activeUser.setText(currentUser.getEmail());
    }

    @Override
    public void addObserver(Observer observer) {
        this.observer = observer;
    }

    @Override
    public void removeObserver(Observer observer) {
        this.observer = null;
    }

    @Override
    public void notifyObservers(int event) {
        observer.update(event);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == newGame){
            remove(0);
            add(newGamePanel,0);
            revalidate();
            repaint();
        }
        if(e.getSource() == loadGame){
            remove(0);
            add(loadGamePanel,0);
            loadGamePanel.updateContent();
            revalidate();
            repaint();
        }
        if(e.getSource() == logout){
            notifyObservers(0);
        }
    }
}
