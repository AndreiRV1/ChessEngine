package Base;

import Base.Observers.GameObserver;
import Base.Observers.GameSubject;
import Base.Strategy.ChessScoreStrategy;
import Base.Strategy.ScoreStrategy;
import Board.Board;
import Board.ChessPair;
import Board.Position;
import Board.Move;
import Exceptions.InvalidCommandException;
import Pieces.Colors;
import Pieces.Piece;

import java.util.*;
import java.util.List;

public class Game implements GameSubject {
    static long last_identifier = 0;
    private final long identifier;
    public Colors isInMate;
    public Colors isInCheck;
    Board board;
    List<Player> players;
    int crtPlayer;
    public Map<Move, Player> moveHistory;
    boolean loaded = false;
    List<GameObserver> observers = new ArrayList<>();
    ScoreStrategy scoreStrategy = new ChessScoreStrategy();

    public Game(long identifier, List<Player> players) {
        if (identifier > last_identifier) {
            last_identifier = identifier;
        }
        this.identifier = identifier;
        this.players = players;
        board = new Board();
        moveHistory = new LinkedHashMap<Move, Player>();
        crtPlayer = 0;
        isInMate = null;
        isInCheck = null;
    }

    public Game(List<Player> players) {
        this(++last_identifier, players);
    }

    public void start() {
        board.initialize();
        crtPlayer = (players.getFirst().color == Colors.WHITE) ? 0 : 1;
    }

    public void setCurrentPlayerColor(String color) {
        if (players.getFirst().color == (color.equals("WHITE") ? Colors.WHITE : Colors.BLACK)) {
            crtPlayer = 0;
        } else {
            crtPlayer = 1;
        }
    }

    public Player getCurrentPlayer(){
        return players.get(crtPlayer);
    }

    public Player getPlayer(Colors color){
        for(Player player : players){
            if(player.color == color)
                return player;
        }
        return null;
    }

    public void resign(){
        getHumanPlayer().addPoints(scoreStrategy.getScoreForResign());
        System.out.println(getHumanPlayer().getPoints());
        notifyObservers(EventType.GAME_END.ordinal(), GameState.RESIGN);
    }

    public Player getHumanPlayer(){
        for(Player player : players){
            if(!player.name.equalsIgnoreCase("computer"))
                return player;
        }
        return null;
    }

    public void init(){
        Colors lastMove = null;
        for (Player player : players) {
            player.setPieces(board.getPieces(player.color));
        }
        int i = moveHistory.size();
        for (Move m : moveHistory.keySet()) {
            if(m.getCapturedPiece() != null && !loaded){
                getPlayer(m.getColor()).addPoints(scoreStrategy.getScoreForCapture(m.getCapturedPiece()));
                getPlayer(m.getColor()).addCapturedPiece(m.getCapturedPiece());
            }
            if(i == 1){
                lastMove = m.getColor();
            }
            i--;
        }
        loaded = true;
        if(lastMove == null){
            lastMove = Colors.BLACK;
        }
        crtPlayer = (lastMove == players.getFirst().color)?0:1;
        switchPlayer();
        checkEndOfGame();
    }

    public void checkEndOfGame(){
        isInCheck = null;
        Player currentPlayer = players.get(crtPlayer);

        //Check for check
        for(ChessPair<Position,Piece> p : board.getPieces(Colors.WHITE)){
            if(p.getValue().checkForCheck(board,board.blackKing)){
                isInCheck = Colors.BLACK;
                break;
            }
        }
        for(ChessPair<Position,Piece> p : board.getPieces(Colors.BLACK)){
            if(p.getValue().checkForCheck(board,board.whiteKing)){
                isInCheck = Colors.WHITE;
            }
        }

        if(checkForMate()){
            if(getHumanPlayer().color == isInMate){
                getHumanPlayer().addPoints(scoreStrategy.getScoreForLoss());
                notifyObservers(EventType.GAME_END.ordinal(), GameState.LOSE);
            }else{
                currentPlayer.addPoints(scoreStrategy.getScoreForWin());
                notifyObservers(EventType.GAME_END.ordinal(), GameState.WIN);
            }
        }
        if(moveHistory.size() >= 6 && currentPlayer.name.equals("computer")) {
            List<Move> moves =  moveHistory.keySet().stream().skip(moveHistory.size()-6).toList();
            System.out.println(moves);
            if(moves.get(0).getNextPosition().toString().equals(moves.get(2).getLastPosition().toString()) && moves.get(2).getNextPosition().toString().equals( moves.get(4).getLastPosition().toString())){
                if(moves.get(1).getNextPosition().toString().equals(moves.get(3).getLastPosition().toString()) && moves.get(3).getNextPosition().toString().equals(moves.get(5).getLastPosition().toString())){
                    getHumanPlayer().addPoints(scoreStrategy.getScoreForDraw());
                    notifyObservers(EventType.GAME_END.ordinal(), GameState.DRAW);
                }
            }
        }

    }

    public void makeMove(Position oldPosition, Position newPosition){
        Player currentPlayer = players.get(crtPlayer);
        ChessPair<String,Piece> output = currentPlayer.makeMove(oldPosition,newPosition,board);
        checkEndOfGame();
        notifyObservers(EventType.MOVE_MADE.ordinal(), new Move(currentPlayer.color, oldPosition, newPosition, output.getValue(),output.getKey()));
        currentPlayer.addPoints(scoreStrategy.getScoreForCapture(output.getValue()));
        currentPlayer.addCapturedPiece(output.getValue());
        addMove(currentPlayer,oldPosition,newPosition,output.getValue(),output.getKey());
        checkEndOfGame();
        switchPlayer();
    }

    public ChessPair<Position,Position> choseComputerMove(){
        Player currentPlayer = players.get(crtPlayer);
        Position oldPosition = null, newPosition = null;
        TreeSet<ChessPair<Position, Piece>> pieces = board.getPieces(currentPlayer.color);
        Random rand = new Random();
        List<ChessPair<Position, Piece>> movablePieces = new ArrayList<>();

        for (ChessPair<Position, Piece> pair : pieces) {
            if (!pair.getValue().getPossibleMoves(board,true).isEmpty()) {
                movablePieces.add(pair);
            }
        }

        if (!movablePieces.isEmpty()) {
            ChessPair<Position, Piece> selectedPair = movablePieces.get(rand.nextInt(movablePieces.size()));
            Piece randomPiece = selectedPair.getValue();
            List<Position> moves = randomPiece.getPossibleMoves(board);

            oldPosition = randomPiece.getPosition();
            newPosition = moves.get(rand.nextInt(moves.size()));

            System.out.println("Computer chose: " + oldPosition + "-" + newPosition);
        } else {
            System.out.println("No legal moves available (Checkmate or Stalemate).");
        }
        return new ChessPair<Position, Position>(oldPosition,newPosition);
    }

    public void resume()  {
        Position oldPosition, newPosition;
        Scanner sc = new Scanner(System.in);
        String command;
        while(true) {
            Player currentPlayer = players.get(crtPlayer);
            System.out.print(board.toString(currentPlayer.color));

           checkEndOfGame();

            if(isInCheck != null)
                System.out.println(isInCheck + " is in check.");

            if (!Objects.equals(currentPlayer.name, "computer")) {
                while (true) {
                    try {
                        System.out.println("Chose a piece to show moves, chose move, resign or exit:");
                        command = sc.nextLine();
                        if (command.equals("exit")) {
                            return;
                        }
                        if (command.length() == 2) {
                            Position pos = new Position(command);
                            if(!pos.isValid())
                                throw new InvalidCommandException("Invalid position");
                            Piece piece = board.getPieceAt(pos);
                            if(piece == null || piece.getColor() != currentPlayer.color){
                                System.out.println("[]");
                            }else {
                                System.out.println(board.getPieceAt(pos).getPossibleMoves(board));
                            }
                        } else if (command.length() == 5) {
                            oldPosition = new Position(command.substring(0,2));
                            newPosition = new Position(command.substring(3,5));
                            if(command.charAt(2) != '-')
                                throw new InvalidCommandException("Invalid position format. Please input like 'A1-A2'");
                            List<Piece> taken = currentPlayer.getCapturedPieces();
                            break;
                        } else if(command.equalsIgnoreCase("resign")) {
                            currentPlayer.addPoints(scoreStrategy.getScoreForResign());
                            notifyObservers(EventType.GAME_END.ordinal(), GameState.RESIGN);
                        }else{
                            throw new InvalidCommandException("Invalid command. Write position as A1 or move as A1-A2");
                        }
                    } catch (InvalidCommandException | IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                }
            } else {
                // Player is computer
                ChessPair<Position,Position> choice = choseComputerMove();
                oldPosition = choice.getKey();
                newPosition = choice.getValue();

            }
            makeMove(oldPosition,newPosition);
        }
    }


    public void switchPlayer() {
        crtPlayer = crtPlayer == 0 ? 1 : 0;
        notifyObservers(EventType.PLAYER_SWITCH.ordinal(), players.get(crtPlayer));
    }

    public boolean checkForMate() {
        if(isInMate != null)
            return true;
        boolean hasMoreMoves = false;
        for(ChessPair<Position,Piece> p : board.getPieces(Colors.WHITE)){
            if(!p.getValue().getPossibleMoves(board).isEmpty()) {
                hasMoreMoves = true;
                break;
            }
        }
        if(!hasMoreMoves){
            isInMate = Colors.WHITE;
            return true;
        }
        hasMoreMoves = false;
        for(ChessPair<Position,Piece> p : board.getPieces(Colors.BLACK)){
            if(!p.getValue().getPossibleMoves(board).isEmpty()) {
                hasMoreMoves = true;
                break;
            }
        }
        if(!hasMoreMoves){
            isInMate = Colors.BLACK;
            return true;
        }
        return false;
    }

    public void addMove(Player p, Position from, Position to,Piece capturedPiece,String specialMove) {
        Move move = new Move(p.color, from, to, capturedPiece,specialMove);
        if(capturedPiece != null)
            notifyObservers(EventType.PIECE_CAPTURED.ordinal(), capturedPiece);
        moveHistory.put(move, p);
    }

    public void setMoves(List<Move> moves) {
        for (Move move : moves) {
            Player player;
            if (players.getFirst().color == move.getColor())
                player = players.getFirst();
            else
                player = players.getLast();
            moveHistory.put(move, player);
        }
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public Board getBoard(){
        return board;
    }

    public long getId() {
        return identifier;
    }
    @Override
    public String toString() {
        StringBuilder  sb = new StringBuilder();
        sb.append("Players:\n");
        for(Player player : players){
            sb.append(player.name).append(" ").append(player.color).append("\n");
        }
        sb.append("Board:\n");
        sb.append(board.toString()).append("\n");
        sb.append("Moves:\n");
        for (Move m : moveHistory.keySet()) {
            sb.append(m.toString()).append("\n");
        }
        return sb.toString();
    }

    @Override
    public void addObserver(GameObserver Observer) {
        if(!observers.contains(Observer))
            observers.add(Observer);
    }

    @Override
    public void notifyObservers(int event, Object context) {
        if(event == EventType.MOVE_MADE.ordinal()) {
            for (GameObserver observer : observers) {
                observer.onMoveMade((Move)context);
            }
        }
        if(event == EventType.PIECE_CAPTURED.ordinal()) {
            for (GameObserver observer : observers) {
                observer.onPieceCaptured((Piece) context);
            }
        }
        if(event == EventType.PLAYER_SWITCH.ordinal()) {
            for (GameObserver observer : observers) {
                observer.onPlayerSwitch((Player)context);
            }
        }
        if(event == EventType.GAME_END.ordinal()) {
            for (GameObserver observer : observers) {
                observer.onGameEnd((GameState)context, this);
            }
        }
    }
}
