package GUI;

import Base.Game;
import Base.Observer;
import Base.Player;
import Base.Subject;
import Board.Move;

import javax.imageio.plugins.tiff.GeoTIFFTagSet;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class GamePannel extends JPanel implements ActionListener, Subject {
    Button menuButton, exitButton, resignButton;
    JLabel title;
    JLabel points;
    JPanel color;
    JScrollPane scroll;
    JTextArea history;
    JLabel capturedPieces;
    GUIBoard board;
    Game game;
    ImageIcon table = new ImageIcon("Images/table.png");
    ImageIcon leftPage = new ImageIcon("Images/left.png");
    ImageIcon rightPage = new ImageIcon("Images/right.png");

    Observer observer;

    public GamePannel() {
        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weighty = 1.0;
        constraints.gridx = 0;
        constraints.weightx = 1.0;

        JPanel book = new JPanel();
        book.setLayout(new GridLayout(1,2));
        JPanel mainPannel = new JPanel(){
            @Override
            public void paintComponent(Graphics g){
                super.paintComponent(g);
                g.drawImage(rightPage.getImage(),0,0,getWidth(),getHeight(),this);
            }
        };
        GroupLayout layout = new GroupLayout(mainPannel);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        mainPannel.setLayout(layout);
        JPanel historyPannel = new JPanel(){
            @Override
            public void paintComponent(Graphics g){
                super.paintComponent(g);
                g.drawImage(leftPage.getImage(),0,0,getWidth(),getHeight(),this);
            }
        };
        JPanel capturedPannel = new JPanel(){
            @Override
            public void paintComponent(Graphics g){
                super.paintComponent(g);
                g.drawImage(table.getImage(),0,0,getWidth(),getHeight(),this);
            }
        };


        title = new JLabel("The Game");
        points = new  JLabel("0");
        color = new JPanel();
        board = new GUIBoard(true,500);

        color.setBackground(Color.BLACK);
        color.setPreferredSize(new Dimension(10,10));
        title.setFont(new Font("SansSerif", Font.PLAIN,30));
        points.setFont(new Font("SansSerif", Font.PLAIN,30));



        menuButton = new Button("Menu");
        menuButton.addActionListener(this);
        exitButton = new Button("Exit");
        exitButton.addActionListener(this);
        resignButton = new Button("Resign");
        resignButton.addActionListener(this);

        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGap(20, 50, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(color)
                        .addComponent(resignButton))
                .addGap(20,50,Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(title)
                        .addComponent(board)
                        .addComponent(menuButton))
                .addGap(20,50,Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(points)
                        .addComponent(exitButton))
                .addGap(20, 50, Short.MAX_VALUE
                ));

        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGap(0,0,Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup()
                        .addComponent(color)
                        .addComponent(title)
                        .addComponent(points))
                .addGap(0,0,Short.MAX_VALUE)
                .addComponent(board)
                .addGap(0,0,Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup()
                        .addComponent(resignButton)
                        .addComponent(menuButton)
                        .addComponent(exitButton))
        );

        history = new JTextArea(200,200);
        history.setEditable(false);
        scroll = new JScrollPane(history);

        historyPannel.add(scroll);

        capturedPieces = new JLabel("PIECES");
        capturedPannel.add(capturedPieces);
        capturedPannel.setPreferredSize(new Dimension(200,200));

        book.add(historyPannel);
        book.add(mainPannel);
        book.setMaximumSize(new Dimension(1000,1000));

        add(capturedPannel,constraints);
        constraints.gridx=1;
        constraints.weightx=3.0;
        add(book,constraints);
    }

    void updateContent(){
        board.updateContent(game);
        history.setText("");
        for (Map.Entry<Move, Player> set : game.moveHistory.entrySet()){
            history.append(set.getValue().name + ": " + set.getKey().toString() + "\n");
        }
        points.setText(game.getHumanPlayer().getPoints() + "");
        //todo captured pieces
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
        if(e.getSource() == menuButton){
            notifyObservers(1);
        }
        if(e.getSource() == exitButton){
            notifyObservers(0); //todo change to exit panel
        }
        //todo resign
    }
}
