import Board.Board;
import Pieces.*;
import Board.Position;
import Board.ChessPair;
import Board.Move;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.List;


/**
 * Utility class for reading JSON documents using JSON.simple ("simple-json").
 *
 * ###### IMPORTANT: This is just an example of how to read JSON documents using the library.
 * Your classes might differ slightly, so don’t hesitate to update this class as needed.
 *
 * Expected structures:
 * - accounts.json: an array of objects with fields: email (String), password (String), points (Number), games (array of numbers)
 * - games.json: an array of objects with fields matching the JSON provided:
 *   id (Number), players (array of {email, color}), currentPlayerColor (String),
 *   board (array of {type, color, position}), moves (array of {playerColor, from, to})
 */
public final class JsonReaderUtil {

    private JsonReaderUtil() {
    }

    /**
     * Reads the accounts from the given JSON file path.
     *
     * @param path path to accounts.json
     * @return list of Account objects (empty list if file not found or array empty)
     * @throws IOException    if I/O fails
     * @throws ParseException if JSON is invalid
     */
    public static List<User> readAccounts(Path path, Map<Long, Game> gameMap) throws IOException, ParseException {
        if (path == null || !Files.exists(path)) {
            return new ArrayList<>();
        }
        try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            JSONParser parser = new JSONParser();
            Object root = parser.parse(reader);
            JSONArray arr = asArray(root);
            List<User> result = new ArrayList<>();

            if (arr == null) {
                return result;
            }

            for (Object item : arr) {
                JSONObject obj = asObject(item);
                if (obj == null) {
                    continue;
                }

                User acc = new User(asString(obj.get("email")), asString(obj.get("password")));
                acc.setPoints(asInt(obj.get("points"), 0));
                List<Long> gameIds = new ArrayList<>();
                JSONArray games = asArray(obj.get("games"));
                if (games != null) {
                    for (Object gid : games) {
                        gameIds.add(asLong(gid, 0));
                    }
                }
                acc.setGames(gameIds, gameMap);
                result.add(acc);
            }
            return result;
        }
    }

    public static void writeAccounts(Path path, List<User> users) throws IOException, ParseException {
        if (path == null || !Files.exists(path)) {
            return;
        }
        try (FileWriter writer = new FileWriter(path.toString(), StandardCharsets.UTF_8)) {
            JSONArray arr = new JSONArray();
            JSONObject obj;

            for (User user : users) {
                obj = new JSONObject();
                obj.put("email", user.getEmail());
                obj.put("password", user.getPassword());
                obj.put("points", user.getPoints());
                List<Long> gameIds = new ArrayList<>();
                for (Game game : user.getActiveGames()) {
                    gameIds.add(game.getId());
                }
                obj.put("games", gameIds);
                arr.add(obj);
            }
            writer.write(arr.toJSONString());
            writer.flush();
        }
    }

    /**
     * Reads the games from the given JSON file path and returns them as a map by id.
     * The structure strictly follows games.json as provided (no title/genre).
     *
     * @param path path to games.json
     * @return map id -> Game (empty if file missing or array empty)
     * @throws IOException    if I/O fails
     * @throws ParseException if JSON is invalid
     */
    public static Map<Long, Game> readGamesAsMap(Path path) throws IOException, ParseException {
        Map<Long, Game> map = new HashMap<>();
        if (path == null || !Files.exists(path)) {
            return map;
        }
        try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            JSONParser parser = new JSONParser();
            Object root = parser.parse(reader);
            JSONArray arr = asArray(root);
            if (arr == null) return map;
            for (Object item : arr) {
                JSONObject obj = asObject(item);
                if (obj == null) {
                    continue;
                }
                long id = asLong(obj.get("id"), -1);
                if (id < 0) {
                    continue;
                }// skip invalid
                Game g = null;
                // players array
                JSONArray playersArr = asArray(obj.get("players"));
                if (playersArr != null) {
                    List<Player> players = new ArrayList<>();
                    for (Object pItem : playersArr) {
                        JSONObject pObj = asObject(pItem);
                        if (pObj == null) {
                            continue;
                        }
                        String email = asString(pObj.get("email"));
                        String color = asString(pObj.get("color"));
                        players.add(new Player(email, color));
                    }
                    g = new Game(id, players);
                }
                if (g != null)
                    // currentPlayerColor
                    g.setCurrentPlayerColor(asString(obj.get("currentPlayerColor")));

                // board array
                JSONArray boardArr = asArray(obj.get("board"));
                if (boardArr != null) {
                    Board board = new Board();
                    for (Object bItem : boardArr) {
                        JSONObject bObj = asObject(bItem);
                        if (bObj == null) {
                            continue;
                        }

                        String type = asString(bObj.get("type"));
                        String color = asString(bObj.get("color"));
                        String position = asString(bObj.get("position"));
                        Piece piece = PieceFactory.factory(type, color, position);

                        board.addPiece(piece);
                    }
                    g.setBoard(board);
                }

                // Parse optional moves array
                JSONArray movesArr = asArray(obj.get("moves"));
                if (movesArr != null) {
                    List<Move> moves = new ArrayList<>();
                    for (Object mItem : movesArr) {
                        JSONObject mObj = asObject(mItem);
                        if (mObj == null) {
                            continue;
                        }

                        String playerColor = asString(mObj.get("playerColor"));
                        String from = asString(mObj.get("from"));
                        String to = asString(mObj.get("to"));
                        String piece = null;
                        String color = null;

                        try{
                            JSONObject captured = asObject(mObj.get("captured"));
                            piece = asString(captured.get("type"));
                            color = asString(captured.get("color"));
                        }catch(Exception ignored){}
                        Piece capturedPiece = null;
                        if(piece != null) {
                            capturedPiece = switch (piece) {
                                case "N" -> new Knight(Colors.valueOf(color), new Position(to));
                                case "K" -> new King(Colors.valueOf(color), new Position(to));
                                case "Q" -> new Queen(Colors.valueOf(color), new Position(to));
                                case "R" -> new Rook(Colors.valueOf(color), new Position(to));
                                case "P" -> new Pawn(Colors.valueOf(color), new Position(to));
                                case "B" -> new Bishop(Colors.valueOf(color), new Position(to));
                                default -> null;
                            };
                        }

                        String specialMove = asString(mObj.get("specialMove"));

                        moves.add(new Move(Colors.valueOf(playerColor), new Position(from), new Position(to), capturedPiece,specialMove));
                    }
                    g.setMoves(moves);
                }
                map.put(id, g);
            }
        }
        return map;
    }

    public static void writeGames(Path path, Map<Long, Game> games) throws IOException, ParseException {
        Map<Long, Game> map = new HashMap<>();
        if (path == null || !Files.exists(path)) {
            return;
        }
        try (FileWriter writer = new FileWriter(path.toString(), StandardCharsets.UTF_8)) {
            JSONArray players, board, arr = new JSONArray();
            JSONObject obj, player, piece;
            Iterator<Map.Entry<Long, Game>> it = games.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<Long, Game> entry = it.next();
                obj = new JSONObject();
                obj.put("id", entry.getKey());
                obj.put("currentPlayerColor", entry.getValue().players.get(entry.getValue().crtPlayer).color.toString());
                players = new JSONArray();
                player = new JSONObject();
                player.put("color", entry.getValue().players.getFirst().color.toString());
                player.put("email", entry.getValue().players.getFirst().name);
                players.add(player);
                player = new JSONObject();
                player.put("color", entry.getValue().players.getLast().color.toString());
                player.put("email", entry.getValue().players.getLast().name);
                players.add(player);
                obj.put("players", players);
                board = new JSONArray();
                for (ChessPair<Position, Piece> p : entry.getValue().board.pieces) {
                    piece = new JSONObject();
                    piece.put("type", String.valueOf(p.getValue().type()));
                    piece.put("color", p.getValue().getColor().toString());
                    piece.put("position", p.getValue().getPosition().toString());
                    board.add(piece);
                }
                //todo captured piece
                JSONArray moves = new JSONArray();
                for (Map.Entry<Move, Player> move : entry.getValue().moveHistory.entrySet()) {
                    JSONObject moveObj = new JSONObject();
                    moveObj.put("playerColor",move.getValue().color.toString());
                    moveObj.put("from",move.getKey().getLastPosition().toString());
                    moveObj.put("to",move.getKey().getNextPosition().toString());
                    if(move.getKey().getCapturedPiece() != null) {
                        JSONObject cap = new JSONObject();
                        cap.put("type", String.valueOf(move.getKey().getCapturedPiece().type()));
                        cap.put("color", move.getKey().getCapturedPiece().getColor().toString());
                        moveObj.put("captured",cap);
                    }
                    if(move.getKey().getSpecialMove() != null && !move.getKey().getSpecialMove().isEmpty()){
                        moveObj.put("specialMove",move.getKey().getSpecialMove());
                    }
                    moves.add(moveObj);
                }

                obj.put("board", board);
                obj.put("moves", moves);
                arr.add(obj);
            }
            writer.write(arr.toJSONString());
            writer.flush();
        }
    }
    // -------- helper converters --------

    private static JSONArray asArray(Object o) {
        return (o instanceof JSONArray) ? (JSONArray) o : null;
    }

    private static JSONObject asObject(Object o) {
        return (o instanceof JSONObject) ? (JSONObject) o : null;
    }

    private static String asString(Object o) {
        return o == null ? null : String.valueOf(o);
    }

    private static int asInt(Object o, int def) {
        if (o instanceof Number) return ((Number) o).intValue();
        try {
            return o != null ? Integer.parseInt(String.valueOf(o)) : def;
        } catch (NumberFormatException e) {
            return def;
        }
    }

    private static long asLong(Object o, long def) {
        if (o instanceof Number) return ((Number) o).longValue();
        try {
            return o != null ? Long.parseLong(String.valueOf(o)) : def;
        } catch (NumberFormatException e) {
            return def;
        }
    }
}
