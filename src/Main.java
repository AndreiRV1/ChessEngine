import Exceptions.EndOfGame;
import Exceptions.InvalidCommandException;
import Exceptions.InvalidLoginCredentials;
import Exceptions.NotFoundException;
import Pieces.Colors;
import org.json.simple.parser.ParseException;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

public class
Main {
    List<User> users;
    Map<Long, Game> games;
    User currentUser;

    public Main() {
        users = new ArrayList<>();
        games = new HashMap<>();
        currentUser = null;
    }

    public void read() throws IOException, ParseException {
        games = JsonReaderUtil.readGamesAsMap(Paths.get("input/games.json"));
        users = JsonReaderUtil.readAccounts(Paths.get("input/accounts.json"), games);
    }

    public void write() throws IOException, ParseException {
        JsonReaderUtil.writeGames(Paths.get("input/games.json"), games);
        JsonReaderUtil.writeAccounts(Paths.get("input/accounts.json"), users);
    }

    public User login(String email, String password) throws InvalidLoginCredentials, NotFoundException {
        for (User user : users) {
            if (user.getEmail().equals(email)) {
                if (user.login(password)) {
                    System.out.println("Login Successful");
                    return user;
                } else {
                    throw new InvalidLoginCredentials("Incorrect Password");
                }
            }
        }
        throw new NotFoundException("User not found");
    }

    public User newAccount(String email, String password) throws InvalidLoginCredentials {
        for (User user : users) {
            if (user.getEmail().equals(email)) {
                throw new InvalidLoginCredentials("Account already exists");
            }
        }
        User user = new User(email, password);
        users.add(user);
        return user;
    }

    private String getEmail(Scanner sc) {
        String email = "";
        while (true) {
            try {
                System.out.println("Please enter your email:");
                email = sc.nextLine();
                if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$"))
                    throw new InvalidCommandException("Invalid Email");
                break;
            } catch (InvalidCommandException e) {
                System.out.println(e.getMessage());
            }
        }
        return email;
    }

    private boolean getLogin(Scanner sc) throws LoginException {
        boolean login = false;
        System.out.println("Welcome to my Chess Game. Are you a returning user?(y/n) If you want to quit press q.");
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

    private void loadGame(Scanner sc) {
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
            throw new InvalidCommandException("Invalid Game");
        }
        if (!gameIds.contains(id))
            throw new InvalidCommandException("Invalid Game");
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
                    try {
                        selectedGame.resume();
                    } catch (EndOfGame e) {
                        System.out.println(e.getMessage());
                        int points = currentUser.getPoints() + selectedGame.players.get(selectedGame.crtPlayer).getPoints();
                        currentUser.setPoints(points);
                        System.out.println("You gained " + points+" points. You have " + currentUser.getPoints() + " points");
                        currentUser.removeGame(selectedGame);
                        games.remove(id, selectedGame);
                    }
                } else if (comm.charAt(0) == 'd') {
                    currentUser.removeGame(selectedGame);
                    games.remove(id, selectedGame);
                }

                break;
            } catch (InvalidCommandException e) {
                System.out.println(e.getMessage());
                System.out.print("Try again. ");
            }
        }

    }

    private void createGame(Scanner sc) {
        boolean done = false;
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
        games.put(newGame.getId(), newGame);
        currentUser.addGame(newGame);
        try {
            newGame.start();
        } catch (EndOfGame e) {
            System.out.println(e.getMessage());
            currentUser.setPoints(currentUser.getPoints() + newGame.players.get(newGame.crtPlayer).getPoints());
            System.out.println("You have " + currentUser.getPoints() + " points");
            currentUser.removeGame(newGame);
            games.remove(newGame.getId(), newGame);
        }
    }

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
                    currentUser = login(email, password);
                } else {
                    currentUser = newAccount(email, password);
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

    public static void main(String[] args) {
        Main app = new Main();
        try {
            app.read();
        } catch (Exception e) {
            System.out.println("Files have been tampered with. No games or user loaded");
            app.games = new HashMap<>();
            app.users = new ArrayList<>();
        }
        try {
            app.run();
        } catch (LoginException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Something went wrong while writing.");
        }finally{
            try {
                app.write();
            }catch (Exception e){
                System.out.println("Something went wrong while saving, no progress saved.");
            }
        }
    }
}