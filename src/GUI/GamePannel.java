package GUI;

import Base.Game;
import Base.Observers.GUIObserver;
import Base.Player;
import Base.Observers.GUISubject;
import Board.Move;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Map;

public class GamePannel extends JPanel implements ActionListener, GUISubject {
    Button menuButton, resignButton;
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

    GUIObserver GUIObserver;

    public GamePannel() {
        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weighty = 1.0;
        constraints.gridx = 0;
        constraints.weightx = 0.2;

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
        historyPannel.setLayout(new BorderLayout());
        JPanel capturedPannel = new JPanel(){
            @Override
            public void paintComponent(Graphics g){
                super.paintComponent(g);
                g.drawImage(table.getImage(),0,0,getWidth(),getHeight(),this);
            }
        };
        capturedPannel.setLayout(new GridBagLayout());


        title = new JLabel("The Game");
        points = new  JLabel("0");
        color = new JPanel();
        board = new GUIBoard(true,200);

        color.setBackground(Color.BLACK);
        color.setPreferredSize(new Dimension(10,10));
        title.setFont(new Font("SansSerif", Font.PLAIN,30));
        points.setFont(new Font("SansSerif", Font.PLAIN,30));



        menuButton = new Button("Menu");
        menuButton.addActionListener(this);
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
                .addGap(20, 50, Short.MAX_VALUE)));

        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGap(0,0,Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup()
                        .addComponent(color)
                        .addComponent(title)
                        .addComponent(points))
                .addGap(0,0,Short.MAX_VALUE)
                .addComponent(board, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0,0,Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup()
                        .addComponent(resignButton)
                        .addComponent(menuButton))
        );

        history = new JTextArea(20,20);
        history.setEditable(false);
        history.setLineWrap(true);
        history.setWrapStyleWord(true);
        scroll = new JScrollPane(history);

        historyPannel.add(scroll, BorderLayout.CENTER);

        capturedPieces = new JLabel("PIECES");
        capturedPannel.add(capturedPieces);
        capturedPannel.setPreferredSize(new Dimension(200,200));

        book.add(historyPannel);
        book.add(mainPannel);

        add(capturedPannel,constraints);
        constraints.gridx=1;
        constraints.weightx=0.8;
        add(book,constraints);


        mainPannel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e){
                int width = mainPannel.getWidth();
                int height = mainPannel.getHeight();
                int size = Math.min(width, height) / 2;
                board.setSize(size);
                mainPannel.revalidate();
            }
        });

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
        if(e.getSource() == menuButton){
            notifyObservers(1);
        }
        //todo resign
    }
}
