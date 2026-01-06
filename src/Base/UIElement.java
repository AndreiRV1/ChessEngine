package Base;

import javax.security.auth.login.LoginException;

public interface UIElement {
    public void run() throws LoginException;
    public void runGame(Game game);
}
