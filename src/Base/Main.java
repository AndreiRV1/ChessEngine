package Base;

import Exceptions.InvalidLoginCredentials;
import Exceptions.NotFoundException;
import org.json.simple.parser.ParseException;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;


public class Main{
    List<User> users;
    Map<Long, Game> games;
    User currentUser;
    public static UIElement ui;
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

    public void saveAccounts(){
        System.out.println("Saving accounts...");
        try {
            JsonReaderUtil.writeAccounts(Paths.get("input/accounts.json"), users);
        }catch (Exception e){
            System.out.println("No accounts saved. Something went wrong.");
        }
    }

    public void saveGames(){
        System.out.println("Saving games...");
        try {
            JsonReaderUtil.writeGames(Paths.get("input/games.json"), games);
        }catch (Exception e){
            System.out.println("No games saved. Something went wrong.");
        }
    }


    public void read() throws IOException, ParseException {
        System.out.println("Reading...");
        games = null;
        users = null;
        games = JsonReaderUtil.readGamesAsMap(Paths.get("input/games.json"));
        users = JsonReaderUtil.readAccounts(Paths.get("input/accounts.json"), games);
    }

    public void write() throws IOException, ParseException {
        System.out.println("Writing...");
        JsonReaderUtil.writeGames(Paths.get("input/games.json"), games);
        JsonReaderUtil.writeAccounts(Paths.get("input/accounts.json"), users);
    }

    public void removeGame(Game game){
        games.remove(game.getId());
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
        saveAccounts();
        return user;
    }

    public void createGame(Game newGame) {
        games.put(newGame.getId(), newGame);
        currentUser.addGame(newGame);
        System.out.println("Creating game");
        newGame.addObserver(ui);
        newGame.start();
        ui.runGame(newGame);
    }

    public void loadGame(Game selectedGame){
            System.out.println("Loading game " + selectedGame.getId());
            selectedGame.addObserver(ui);
            ui.runGame(selectedGame);
    }

    public void discardChanges() {
        String currentEmail = (currentUser != null) ? currentUser.getEmail() : null;
        try {
            read();
            if (currentEmail != null) {
                for (User user : users) {
                    if (user.getEmail().equals(currentEmail)) {
                        currentUser = user;
                        break;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error discarding changes: " + e.getMessage());
        }
    }

    public static void main(String[] args) {

        Main app = getInstance();
        if(args.length == 0){
            System.out.println("Please specify UI mode: CLI/GUI");
            System.exit(0);
        }
        if(args[0].equalsIgnoreCase("CLI")){
            ui = CLIUI.getInstance();
        } else if(args[0].equalsIgnoreCase("GUI")){
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
            System.out.println("Something went wrong.");
        } finally {
            Main.getInstance().saveAccounts();
            Main.getInstance().saveGames();
        }
    }
}