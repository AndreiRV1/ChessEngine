package Base;

import Base.Observers.GameObserver;
import Board.Move;
import Exceptions.InvalidCommandException;
import Exceptions.InvalidLoginCredentials;
import Exceptions.InvalidMoveException;
import Exceptions.NotFoundException;
import Pieces.Colors;
import Pieces.Piece;

import javax.security.auth.login.LoginException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CLIUI implements UIElement {
    private static CLIUI instance = null;

    private CLIUI() {};

    public static CLIUI getInstance() {
        if(instance == null) {
            instance = new CLIUI();
        }
        return instance;
    }

    private String getEmail(Scanner sc) {
        String email = "";
        while (true) {
            try {
                System.out.println("Please enter your email:");
                email = sc.nextLine();
                break;
            } catch (InvalidCommandException e) {
                System.out.println(e.getMessage());
            }
        }
        return email;
    }

    private boolean getLogin(Scanner sc) throws LoginException {
        boolean login = false;
        System.out.println("Welcome to my Chess Base.Game. Are you a returning user?(y/n) If you want to quit press q.");
        while (true) {
            try {
                String command = sc.nextLine();
                if (command.length() > 2)
                    throw new InvalidCommandException("Invalid Command");
                login = switch (command) {
                    case "y" -> true;
                    case "n" -> false;
                    case "q" -> throw new LoginException("Quitted");
                    default -> throw new InvalidCommandException("Invalid Command");
                };
                break;
            } catch (InvalidCommandException e) {
                System.out.println(e.getMessage());
                System.out.println("Please enter y or n:");
            }
        }
        return login;
    }

    private char getCommand(Scanner sc) {
        String command = "";
        while (true) {
            try {
                System.out.println("Do you want to load a game, start a new game or logout?(l/c/q)");
                command = sc.nextLine();
                if (command.length() > 1) {
                    throw new InvalidCommandException("Invalid Command");
                }
                if (!"clq".contains(command)) {
                    throw new InvalidCommandException("Invalid Command");
                }
                if (command.isEmpty())
                    throw new InvalidCommandException("Invalid Command");
                break;
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.print("Try again. ");
            }
        }
        return command.charAt(0);
    }

    void loadGame(Scanner sc) {
        User currentUser = Main.getInstance().currentUser;
        List<Long> gameIds = new ArrayList<>();
        long id = -1;
        for (Game game : currentUser.getActiveGames()) {
            gameIds.add(game.getId());
        }
        System.out.println("Chose what game you want to see:");
        System.out.println(gameIds);
        String command = sc.nextLine();
        try {
            id = Long.parseLong(command);
        } catch (NumberFormatException e) {
            throw new InvalidCommandException("Invalid Base.Game");
        }
        if (!gameIds.contains(id))
            throw new InvalidCommandException("Invalid Base.Game");
        Game selectedGame = null;
        for (Game game : currentUser.getActiveGames()) {
            if (game.getId() == id) {
                selectedGame = game;
            }
        }
        String comm = "";
        while (true) {
            try {
                System.out.println("Do you want to inspect, continue or delete game?(i/c/d) " + id);
                comm = sc.nextLine().toLowerCase();
                if (comm.length() != 1)
                    throw new InvalidCommandException("Invalid Command");
                if (comm.charAt(0) == 'i') {
                    System.out.println(selectedGame);
                } else if (comm.charAt(0) == 'c') {
                    Main.getInstance().loadGame(selectedGame);
                } else if (comm.charAt(0) == 'd') {
                    currentUser.removeGame(selectedGame);
                    Main.getInstance().games.remove(id, selectedGame);
                }

                break;
            } catch (InvalidCommandException e) {
                System.out.println(e.getMessage());
                System.out.print("Try again. ");
            }
        }
    }

    void createGame(Scanner sc) {
        System.out.println("Creating game.");
        System.out.println("Please enter a nickname:");
        String nickname = sc.nextLine();
        Colors colour = Colors.WHITE;
        while (true) {
            try {
                System.out.println("Please choose your color, white or black:");
                String color = sc.nextLine().toUpperCase();
                colour = Colors.valueOf(color);
                break;
            } catch (Exception e) {
                System.out.println("Invalid Color");
            }
        }
        List<Player> players = new ArrayList<>();

        players.add(new Player(nickname, colour));

        players.add(new Player("computer", (colour == Colors.WHITE ? Colors.BLACK : Colors.WHITE)));
        Game newGame = new Game(players);
        Main.getInstance().createGame(newGame);
        Main.getInstance().loadGame(newGame);
    }

    @Override
    public void run() throws LoginException {
        boolean login = false, load = false;
        String email = "", password, command;
        Scanner sc = new Scanner(System.in);
        int gameId;
        login = getLogin(sc);
        email = getEmail(sc);
        while (true) {
            try {
                System.out.println("Please enter your password:");
                password = sc.nextLine();
                if (!login && !password.matches("^(?=.*[A-Z])(?=.*[!@,.#$%^&*_]).*$")) {
                    throw new InvalidCommandException("Password must contain at least one upper character and one special character.");
                }
                if (login) {
                    Main.getInstance().login(email, password);
                } else {
                    Main.getInstance().newAccount(email, password);
                }
                break;
            } catch (InvalidCommandException e) {
                System.out.println(e.getMessage());
            } catch (NotFoundException | InvalidLoginCredentials e) {
                System.out.println(e.getMessage());
                System.out.print("Try again. ");
                login = getLogin(sc);
                email = getEmail(sc);
            }
        }
        User currentUser = Main.getInstance().currentUser;
        System.out.println("Hello " + currentUser.getEmail() + "! You have " + currentUser.getPoints() + " points.");
        char commandChar = getCommand(sc);
        while (commandChar != 'q') {
            if (commandChar == 'l') {
                try {
                    loadGame(sc);
                } catch (InvalidCommandException e) {
                    System.out.println(e.getMessage());
                }
            }
            if (commandChar == 'c')
                createGame(sc);
            commandChar = getCommand(sc);
        }
        run();
    }

    @Override
    public void runGame(Game game) {
        game.resume();
    }

    @Override
    public char changePiece() {
        String newPiece;
        while(true) {
            try {
                System.out.println("Chose piece to change pawn into:");
                Scanner in = new Scanner(System.in);
                newPiece = in.nextLine().toUpperCase();
                if (newPiece.length() > 2)
                    throw new InvalidMoveException("Invalid piece: " + newPiece);
                break;
            } catch (Exception e){
                System.out.print("Invalid piece. Try again. ");
            }
        }
        return newPiece.charAt(0);
    }

    @Override
    public void onMoveMade(Move move) {
        System.out.println("Move made: "+ move.getLastPosition()+"-"+move.getNextPosition());
    }

    @Override
    public void onPieceCaptured(Piece piece) {
        System.out.println("Piece captured: " + piece.type());
    }

    @Override
    public void onPlayerSwitch(Player currentPlayer) {
        System.out.println(currentPlayer.name + "'s turn.");
    }

    @Override
    public void onGameEnd(GameState result,Game game) {
        int points = Main.getInstance().currentUser.getPoints() + game.getHumanPlayer().getPoints();
        Main.getInstance().currentUser.setPoints(points);
        Main.getInstance().currentUser.removeGame(game);
        Main.getInstance().games.remove(game.getId(), game);
        if(result == GameState.RESIGN){
            // You resigned
            System.out.println("You resigned");
        } else if(result == GameState.DRAW){
            //DRAW
            System.out.println("Draw");
        } else  if(result == GameState.WIN){
            // WIN
            System.out.println("You WON");
        } else if(result == GameState.LOSE){
            // LOSE
            System.out.println("You LOST");
        }
    }

}
