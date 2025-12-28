import javax.security.auth.login.LoginException;
import javax.swing.*;
import java.awt.event.ActionListener;

public class GUIUI implements UIElement {
    private static GUIUI instance = null;

    private GUIUI() {
    }


    public static GUIUI getInstance() {
        if (instance == null) {
            instance = new GUIUI();
        }
        return instance;
    }

    @Override
    public void run() throws LoginException {
        System.out.println("Welcome to the GUI!");
    }
}
