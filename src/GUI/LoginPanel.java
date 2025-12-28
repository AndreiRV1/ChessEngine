package GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Base.Main;
import Base.Observer;
import Base.Subject;
import Base.User;
import Exceptions.InvalidLoginCredentials;


public class LoginPanel extends JPanel implements ActionListener, Subject {
    Observer observer;
    TextField email;
    PasswordField password;
    JLabel Title;
    Button login, createAccount;
    public LoginPanel(){
        super(new GridLayout(1,2));
        email = new TextField(20);
        email.setSize(new Dimension(100,30));
        password = new PasswordField(20);

        login = new Button("Login");
        createAccount = new Button("Create Account");
        Title = new JLabel("WELCOME");


        JPanel background = new JPanel();
        background.setBackground(Color.MAGENTA);

        JPanel loginPanel = new JPanel();
        GroupLayout layout = new GroupLayout(loginPanel);
        loginPanel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(Title)
                        .addComponent(email, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(password, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
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
                .addComponent(email, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addComponent(password, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addGap(20)
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
        loginPanel.add(email);
        loginPanel.add(login);
        loginPanel.add(password);
        loginPanel.add(createAccount);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == login){
            try {
                Main.getInstance().login(email.getText(), String.valueOf(password.getPassword()));
                notifyObservers(1);
            }catch (InvalidLoginCredentials exception){
                System.out.println(exception.getMessage());
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
}
