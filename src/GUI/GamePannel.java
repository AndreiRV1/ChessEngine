package GUI;

import Base.Game;
import Base.Main;
import Base.Observers.GUIObserver;
import Base.Player;
import Base.Observers.GUISubject;
import Board.ChessPair;
import Board.Move;
import Board.Position;
import Pieces.Colors;
import Pieces.Piece;
import Pieces.PieceFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.text.ListFormat;
import java.util.Map;

public class GamePannel extends JPanel implements ActionListener, GUISubject {
    PaperButton menuButton, resignButton, exitButton;
    JLabel title;
    JLabel points;
    JLabel color;
    JScrollPane scroll;
    JTextArea history;
    JLabel context = new JLabel();
    JTextPane capturedPiecesWhite,capturedPiecesBlack;
    GUIBoard board;
    Game game;
    ImageIcon book = new ImageIcon("Images/full.png");
    JLabel changePiece;
    JTextField typeChange;

    GUIObserver GUIObserver;

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(book.getImage(),0,0,getWidth(),getHeight(),this);
    }

    public void checkCheck(){
        System.out.println(game.isInCheck + "Is in check");
        if(game.isInCheck != null){
            context.setText(game.isInCheck + " is in check");
            context.setVisible(true);
        }else{
            context.setVisible(false);
        }
    }

    public GamePannel() {
        setLayout(new GridLayout(1,2));

        JPanel mainPannel = new JPanel();
        mainPannel.setOpaque(false);
        GroupLayout layout = new GroupLayout(mainPannel);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        mainPannel.setLayout(layout);

        JPanel contextPanel = new JPanel();
        contextPanel.setOpaque(false);

        title = new JLabel("The Game");
        Font oldEnglish, libre;
        try {
            oldEnglish = Font.createFont(Font.TRUETYPE_FONT, new File("Fonts/oldEnglish.ttf")).deriveFont(80f);
            title.setFont(oldEnglish);
        }catch (Exception ignored){}
        title.setForeground(Color.decode("#18161B"));


        points = new  JLabel("0");
        color = new JLabel("?");

        try {
            libre = Font.createFont(Font.TRUETYPE_FONT, new File("Fonts/libre.ttf")).deriveFont(30f);
            points.setFont(libre);
            color.setFont(libre);
        }catch (Exception ignored){}

        board = new GUIBoard(true,400);

        menuButton = new PaperButton("Menu");
        menuButton.addActionListener(this);

        resignButton = new PaperButton("Resign");
        resignButton.addActionListener(this);

        exitButton = new PaperButton("Exit");
        exitButton.addActionListener(this);

        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGap(20, 50, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(board)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(20, 50, Short.MAX_VALUE)
                                .addComponent(color)
                                .addGap(20, 50, Short.MAX_VALUE)
                                .addComponent(title)
                                .addGap(20, 50, Short.MAX_VALUE)
                                .addComponent(points)
                                .addGap(20, 50, Short.MAX_VALUE))
                        .addGroup(layout.createSequentialGroup()
                                .addGap(20, 50, Short.MAX_VALUE)
                                .addComponent(resignButton)
                                .addGap(20, 50, Short.MAX_VALUE)
                                .addComponent(menuButton)
                                .addGap(20, 50, Short.MAX_VALUE)
                                .addComponent(exitButton)
                                .addGap(20, 50, Short.MAX_VALUE)))
                .addGap(100));

        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGap(100)
                .addGroup(layout.createParallelGroup()
                        .addComponent(color)
                        .addComponent(title)
                        .addComponent(points))
                .addGap(0,0,Short.MAX_VALUE)
                .addComponent(board, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0,0,Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup()
                        .addComponent(resignButton)
                        .addComponent(menuButton)
                        .addComponent(exitButton))
                .addGap(100)
        );

        history = new JTextArea(10,10);
        history.setOpaque(false);
        history.setFont(new Font("Serif", Font.ITALIC, 16));
        history.setForeground(new Color(40, 35, 30));
        history.setEditable(false);

        scroll = new JScrollPane(history);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(92, 64, 51),5));
        scroll.setPreferredSize(new Dimension(100,200));
        scroll.setMinimumSize(new Dimension(100,200));

        scroll.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0));
        scroll.getVerticalScrollBar().setBackground(new Color(245, 245, 242));


        capturedPiecesWhite = new JTextPane();
        capturedPiecesBlack = new JTextPane();

        capturedPiecesBlack.setEditable(false);
        capturedPiecesWhite.setEditable(false);
        capturedPiecesBlack.setOpaque(false);
        capturedPiecesWhite.setOpaque(false);

        capturedPiecesWhite.setFont(new Font("SansSerif", Font.PLAIN, 15));
        capturedPiecesBlack.setFont(new Font("SansSerif", Font.PLAIN, 15));

        capturedPiecesWhite.setContentType("text/plain");
        capturedPiecesBlack.setContentType("text/plain");

        JScrollPane whiteScroll = new JScrollPane(capturedPiecesWhite);
        whiteScroll.setOpaque(false);
        whiteScroll.getViewport().setOpaque(false);
        whiteScroll.setBorder(BorderFactory.createEmptyBorder());
        whiteScroll.setPreferredSize(new Dimension(200, 60));
        whiteScroll.setMinimumSize(new Dimension(200, 40));

        JScrollPane blackScroll = new JScrollPane(capturedPiecesBlack);
        blackScroll.setOpaque(false);
        blackScroll.getViewport().setOpaque(false);
        blackScroll.setBorder(BorderFactory.createEmptyBorder());
        blackScroll.setPreferredSize(new Dimension(200, 60));
        blackScroll.setMinimumSize(new Dimension(200, 40));

        try {
            libre = Font.createFont(Font.TRUETYPE_FONT, new File("Fonts/libre.ttf")).deriveFont(30f);
            context.setFont(libre);
        }catch (Exception ignored){}



        typeChange = new JTextField(15);

        changePiece = new JLabel();
        changePiece.setOpaque(false);
        changePiece.setLayout(new FlowLayout());
        changePiece.add(typeChange);

        changePiece.setVisible(false);


        GroupLayout layout2 = new GroupLayout(contextPanel);
        layout2.setAutoCreateGaps(true);
        layout2.setAutoCreateContainerGaps(true);
        contextPanel.setLayout(layout2);

        layout2.setHorizontalGroup(layout2.createSequentialGroup()
                .addGap(0,0,Short.MAX_VALUE)
                .addGroup(layout2.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(context)
                        .addComponent(changePiece)
                        .addComponent(scroll)
                        .addComponent(whiteScroll)
                        .addComponent(blackScroll))
                .addGap(0,0,Short.MAX_VALUE)
        );

        layout2.setVerticalGroup(layout2.createSequentialGroup()
                .addGap(0,0,Short.MAX_VALUE)
                .addComponent(context)
                .addComponent(changePiece)
                .addComponent(scroll)
                .addGap(100)
                .addComponent(whiteScroll)
                .addComponent(blackScroll)
                .addGap(0,0,Short.MAX_VALUE)
        );

        add(contextPanel);
        add(mainPannel);
    }

    public void setColor(){
        color.setText((game.getCurrentPlayer().color == Colors.WHITE) ? "White" : "Black");
        System.out.println(color.getText());
        repaint();
    }

    public void setPoints(){
        points.setText(game.getHumanPlayer().getPoints() + "p");
    }

    public void setHistory(){
        history.setText("");
        int i = 1;
        for (Map.Entry<Move, Player> set : game.moveHistory.entrySet()){
            history.append(i + ". " + set.getKey().toString() + "\n");
            i++;
        }
    }

    public void setCapturedPieces(){
        StringBuffer st = new StringBuffer();
        st.append("Black pieces taken: ");
        try {
            for (Piece p : game.getPlayer(Colors.WHITE).getCapturedPieces()) {
                st.append(PieceFactory.pieceUnicode(p.type())).append(" ");
            }
        }catch (Exception ignored){}
        st.append(" .");
        capturedPiecesWhite.setText(st.toString());
        capturedPiecesWhite.setPreferredSize(new Dimension(200, 50));

        st = new StringBuffer();
        st.append("White pieces taken: ");
        try {
            for (Piece p : game.getPlayer(Colors.BLACK).getCapturedPieces()) {
                st.append(PieceFactory.pieceUnicode(p.type())).append(" ");
            }
        }catch (Exception ignored){}
        st.append(" .");
        capturedPiecesBlack.setText(st.toString());
        capturedPiecesBlack.setPreferredSize(new Dimension(200, 50));
    }

    void updateContent(){
        if(!game.getCurrentPlayer().equals(game.getHumanPlayer())){
            ChessPair< Position,Position> choice = game.choseComputerMove();
            game.makeMove(choice.getKey(), choice.getValue());
        }

        board.updateContent(game);
        setHistory();
        setPoints();
        setColor();
        setCapturedPieces();
        checkCheck();
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
        if(e.getSource() == exitButton){
            try {
                Main.getInstance().write();
            }catch (Exception ignored){}
            notifyObservers(1);
        }
        if(e.getSource() == resignButton){
            game.resign();
        }
    }

    public char changePiece(){
        changePiece.setVisible(true);
        typeChange.setText("");
        typeChange.requestFocusInWindow();
        try {
            synchronized (this) {
                wait();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        while(!"QNBR".contains(typeChange.getText().toUpperCase())){
            // i WANT TO WAIT UNTIL USER GIVES ME THAT INPUT(ONE OF THOSE LETTERS)
        }
        return typeChange.getText().charAt(0);
    }
}
