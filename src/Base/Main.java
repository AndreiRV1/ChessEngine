package Base;

import Exceptions.EndOfGame;
import Exceptions.InvalidLoginCredentials;
import Exceptions.NotFoundException;
import org.json.simple.parser.ParseException;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;


public class Main {
    List<User> users;
    Map<Long, Game> games;
    User currentUser;
    static UIElement ui;
    private static Main instance = null;

    private Main() {
        users = new ArrayList<>();
        games = new HashMap<>();
        currentUser = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public static Main getInstance(){
        if (instance == null){
            instance = new Main();
        }
        return instance;
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
                    currentUser = user;
                    return user;
                } else {
                    throw new InvalidLoginCredentials("Incorrect Password");
                }
            }
        }
        throw new NotFoundException("Base.User not found");
    }

    public User newAccount(String email, String password) throws InvalidLoginCredentials {
        for (User user : users) {
            if (user.getEmail().equals(email)) {
                throw new InvalidLoginCredentials("Account already exists");
            }
        }
        User user = new User(email, password);
        users.add(user);
        currentUser = user;
        return user;
    }

    public void createGame(Game newGame) {
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

    public void loadGame(Game selectedGame){
        try {
            selectedGame.resume();
        } catch (EndOfGame e) {
            System.out.println(e.getMessage());
            int points = currentUser.getPoints() + selectedGame.players.get(selectedGame.crtPlayer).getPoints();
            currentUser.setPoints(points);
            System.out.println("You gained " + points+" points. You have " + currentUser.getPoints() + " points");
            currentUser.removeGame(selectedGame);
            Main.getInstance().games.remove(selectedGame.getId(), selectedGame);
        }
    }

    public static void main(String[] args) {

        Main app = getInstance();
        if(args.length == 0){
            System.out.println("Please specify UI mode: CLI/GUI");
        }
        if(args[0].equals("CLI")){
            ui = CLIUI.getInstance();
        } else if(args[0].equals("GUI")){
            ui = GUIUI.getInstance();
        }

        try {
            app.read();
        } catch (Exception e) {
            System.out.println("Files have been tampered with. No games or user loaded");
            app.games = new HashMap<>();
            app.users = new ArrayList<>();
        }
        try {
            ui.run();
        } catch (LoginException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
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