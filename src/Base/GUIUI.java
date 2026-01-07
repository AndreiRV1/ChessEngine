package Base;

import GUI.Window;

import javax.security.auth.login.LoginException;

public class GUIUI implements UIElement {
    private static GUIUI instance = null;
    Window window;

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
        window = new Window("Chess engine");
    }

    @Override
    public void runGame(Game game) {
        game.init();
        window.setGame(game);
        window.update(2);
    }
}
