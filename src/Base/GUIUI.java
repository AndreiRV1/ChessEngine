package Base;

import Base.Observers.GameObserver;
import Board.Move;
import GUI.Window;
import Pieces.Piece;

import javax.security.auth.login.LoginException;

public class GUIUI implements UIElement {
    private static GUIUI instance = null;
    Window window;
    private GUIUI() {}


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
        window.setGame(game);
        window.updatePage(2);
    }

    @Override
    public char changePiece() {
        return window.changePiece();
    }


    @Override
    public void onMoveMade(Move move) {
        window.updatePage(5);
    }

    @Override
    public void onPieceCaptured(Piece piece) {
        window.updatePage(4);
    }

    @Override
    public void onPlayerSwitch(Player currentPlayer) {
        System.out.println("Player switched to: " + currentPlayer.name + " " + currentPlayer.color);
        window.updatePage(3);
    }

    @Override
    public void onGameEnd(GameState result, Game game) {
        int points = Main.getInstance().currentUser.getPoints() + game.getHumanPlayer().getPoints();
        Main.getInstance().currentUser.setPoints(points);
        Main.getInstance().currentUser.removeGame(game);
        Main.getInstance().games.remove(game.getId(), game);
        try {
            Main.getInstance().write();
        }catch (Exception ignored){}
        window.showWinPage(result,game);
    }
}
