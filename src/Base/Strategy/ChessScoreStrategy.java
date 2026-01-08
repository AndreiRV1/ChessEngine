package Base.Strategy;

import Pieces.Piece;

public class ChessScoreStrategy implements ScoreStrategy {
    @Override
    public int getScoreForCapture(Piece capturedPiece) {
        if (capturedPiece == null) {
            return 0;
        }
        switch (Character.toUpperCase(capturedPiece.type())) {
            case 'P': return 10;
            case 'N': return 30;
            case 'B': return 30;
            case 'R': return 50;
            case 'Q': return 90;
            default: return 0;
        }
    }

    @Override
    public int getScoreForWin() {
        return 300;
    }

    @Override
    public int getScoreForLoss() {
        return -300;
    }

    @Override
    public int getScoreForDraw() {
        return 150;
    }

    @Override
    public int getScoreForResign() {
        return -150;
    }
}