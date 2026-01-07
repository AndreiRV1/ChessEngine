package GUI;

import javax.security.auth.login.LoginException;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import Base.Main;
import Base.Observers.GUIObserver;
import Base.Observers.GUISubject;
import Exceptions.InvalidLoginCredentials;
import Exceptions.NotFoundException;


public class LoginPanel extends JPanel implements ActionListener, GUISubject {
    GUIObserver GUIObserver;
    Font oldEnglish;
    TextField email;
    PasswordField password;
    JLabel Title;
    Button login, createAccount;
    ImageIcon table = new ImageIcon("Images/table.png");
    ImageIcon cover = new ImageIcon("Images/cover.png");
    JLabel error;
    public LoginPanel(){
        super(new GridLayout(1,2));
        email = new TextField(20);
        email.setSize(new Dimension(100,30));
        password = new PasswordField(20);

        login = new Button("Login");
        createAccount = new Button("Create Account");
        Title = new JLabel("Chess Book");
        error = new JLabel();
        error.setFont(new Font("SansSerif", Font.BOLD, 16));
        error.setForeground(new Color(190, 160, 80));

        LeatherPatchPanel pannel1 = new LeatherPatchPanel();
        LeatherPatchPanel pannel2 = new LeatherPatchPanel();
        pannel1.add(email);
        pannel2.add(password);


        try {
            oldEnglish = Font.createFont(Font.TRUETYPE_FONT, new File("Fonts/oldEnglish.ttf")).deriveFont(100f);
        }catch (Exception ignored){}
        Title.setFont(oldEnglish);
        Title.setForeground(Color.decode("#1A0F00"));



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
        layout.setAutoCreateContainerGaps(true);
        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(Title)
                        .addComponent(pannel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(pannel2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(error, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(createAccount)
                                .addGap(5)
                                .addComponent(login)))
                .addGap(0, 0, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(Title)
                    .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(pannel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addComponent(pannel2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addGap(20)
                .addComponent(error)
                        .addGap(10)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                    .addComponent(createAccount)
                    .addComponent(login))
                .addGap(0, 0, Short.MAX_VALUE)
        );

        loginPanel.setBorder(new EmptyBorder(20,20,20,20));

        add(background);
        add(loginPanel);


        login.addActionListener(this);
        createAccount.addActionListener(this);
        loginPanel.add(pannel1);
        loginPanel.add(login);
        loginPanel.add(pannel2);
        loginPanel.add(createAccount);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(email.getText().isEmpty() || password.getPassword().length == 0) {
            error.setText("Please provide credentials");
            return;
        }

        if(e.getSource() == login){
            try {
                Main.getInstance().login(email.getText(), String.valueOf(password.getPassword()));
                notifyObservers(1);
            }catch (InvalidLoginCredentials exception){
                System.out.println(exception.getMessage());
                error.setText("Invalid email-password combination");
            } catch (NotFoundException loginException) {
                System.out.println(loginException.getMessage());
                error.setText("User not found. Create a new account");
            }
        }
        if(e.getSource() == createAccount){
            try {
                Main.getInstance().newAccount(email.getText(), String.valueOf(password.getPassword()));
                notifyObservers(1);
            }catch (InvalidLoginCredentials exception){
                System.out.println(exception.getMessage());
            }
        }
    }

    public void updateContent(){
        email.setText("");
        password.setText("");
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
}
