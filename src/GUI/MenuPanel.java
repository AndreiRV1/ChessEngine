package GUI;

import Base.Main;
import Base.Observers.GUIObserver;
import Base.Observers.GUISubject;
import Base.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.Paper;
import java.io.File;

public class MenuPanel extends JPanel implements ActionListener, GUISubject {
    GUIObserver GUIObserver;
    JLabel title;
    JLabel activeGames, points;
    JLabel activeUser;
    PaperButton newGame, loadGame, logout;
    NewGamePanel newGamePanel;
    LoadGamePanel loadGamePanel;
    JPanel context;
    ImageIcon leftPage = new ImageIcon("Images/left.png");
    ImageIcon rightPage = new ImageIcon("Images/right.png");


    public MenuPanel() {
        setLayout(new GridLayout(1,2));
        JPanel menu = new JPanel(){
            @Override
            public void paintComponent(Graphics g){
                super.paintComponent(g);
                g.drawImage(rightPage.getImage(),0,0,getWidth(),getHeight(),this);
            }
        };
        context = new JPanel(){
            @Override
            public void paintComponent(Graphics g){
                super.paintComponent(g);
                g.drawImage(leftPage.getImage(),0,0,getWidth(),getHeight(),this);
            }
        };

        menu.setBorder(new EmptyBorder(0,0,0,50));
        title = new JLabel("The Menu");
        Font oldEnglish, libre;
        try {
            oldEnglish = Font.createFont(Font.TRUETYPE_FONT, new File("Fonts/oldEnglish.ttf")).deriveFont(80f);
            title.setFont(oldEnglish);
        }catch (Exception ignored){}
        title.setForeground(Color.decode("#18161B"));


        activeGames = new JLabel(" Games");
        points = new JLabel(" Points");
        activeUser = new JLabel("null");

        try {
            libre = Font.createFont(Font.TRUETYPE_FONT, new File("Fonts/libre.ttf")).deriveFont(30f);
            activeUser.setFont(libre);
            points.setFont(libre);
            activeGames.setFont(libre);
        }catch (Exception ignored){}

        newGame = new PaperButton("New Game");
        loadGame = new PaperButton("Continue");
        logout = new PaperButton("Logout");

        GroupLayout layout = new GroupLayout(menu);
        menu.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGap(100)
                .addComponent(title)
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(activeUser)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(activeGames, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(points, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                )
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(newGame)
                    .addComponent(loadGame)
                    .addComponent(logout))
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
                        .addGroup(layout.createSequentialGroup()
                        .addComponent(newGame)
                        .addComponent(loadGame)
                        .addComponent(logout)))
                .addGap(0, 0, Short.MAX_VALUE)
        );

        logout.addActionListener(this);
        newGame.addActionListener(this);
        loadGame.addActionListener(this);

        newGamePanel = new NewGamePanel(){
            @Override
            public void paintComponent(Graphics g){
                super.paintComponent(g);
                g.drawImage(leftPage.getImage(),0,0,getWidth(),getHeight(),this);
            }
        };
        loadGamePanel = new LoadGamePanel(){
            @Override
            public void paintComponent(Graphics g){
                super.paintComponent(g);
                g.drawImage(leftPage.getImage(),0,0,getWidth(),getHeight(),this);
            }
        };

        add(context);
        add(menu);

    }


    public void updateContent(){
        User currentUser = Main.getInstance().getCurrentUser();
        activeGames.setText(currentUser.getActiveGames().size() + " Games Active");
        points.setText(currentUser.getPoints() + " Points");
        activeUser.setText("Logged user: "+currentUser.getEmail());
        loadGamePanel.updateContent();
    }

    @Override
    public void addObserver(GUIObserver GUIObserver) {
        this.GUIObserver = GUIObserver;
    }

    @Override
    public void removeObserver(GUIObserver GUIObserver) {
        this.GUIObserver = null;
    }

    @Override
    public void notifyObservers(int event) {
        GUIObserver.updatePage(event);
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
            revalidate();
            repaint();
        }
        if(e.getSource() == logout){
            notifyObservers(0);
        }
    }
}
