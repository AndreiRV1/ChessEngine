package Base;

import Base.Observers.GameObserver;

import javax.security.auth.login.LoginException;

public interface UIElement extends GameObserver {
    public void run() throws LoginException;
    public void runGame(Game game);
}
