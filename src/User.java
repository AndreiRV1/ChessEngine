import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class User {
    private final String email;
    private final String password;
    private final List<Game> games;
    private int points;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
        games = new ArrayList<>();
    }

    public void addGame(Game game) {
        games.add(game);
    }

    public void removeGame(Game game) {
        games.remove(game);
    }

    public List<Game> getActiveGames() {
        return games;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getEmail() {
        return email;
    }

    public boolean login(String password) {
        return password.equals(this.password);
    }

    public void setGames(List<Long> gameIds, Map<Long, Game> gameMap) {
        for (long id : gameIds) {
            Game game = gameMap.get(id);
            games.add(game);
        }
    }

    public String getPassword() {
        return password;
    }
}
