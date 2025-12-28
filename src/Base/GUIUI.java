package Base;

import GUI.Window;

import javax.security.auth.login.LoginException;

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
        Window window = new Window("Chess engine");
    }
}
