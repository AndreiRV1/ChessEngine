package Base.Strategy;

import Pieces.Piece;

public interface ScoreStrategy {
    int getScoreForCapture(Piece capturedPiece);

    int getScoreForWin();

    int getScoreForLoss();

    int getScoreForDraw();

    int getScoreForResign();
}
