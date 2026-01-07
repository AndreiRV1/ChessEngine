package Base.Observers;

import Base.Game;
import Base.GameState;
import Base.Player;
import Board.Move;
import Pieces.Piece;

public interface GameObserver {
    void onMoveMade(Move move);
    void onPieceCaptured(Piece piece);
    void onPlayerSwitch(Player currentPlayer);
    void onGameEnd(GameState result, Game game);
}
