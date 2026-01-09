# Chess game
## Radulescu Andrei-Valentin 321CC

A comprehensive Java implementation of a Chess game, featuring a graphical user interface (Swing), persistent storage, 
a basic AI opponent, and a robust login system.

---

I found it to have a medium difficulty level, it took about 30h to make the engine and 30h for the GUI. 
The biggest difficulty was not letting pieces move that let a king in check(deep checking as i called it) and 
working with Swing. Other than that is was easy, just long and repetitive to implement. And sometimes the
framework provided felt like the biggest difficulty was implementing the specification, not the game. 
Also, the refactoring was tedious, it would have been better to know beforehand the architecture that we were supposed 
to do.

---

## Running the app

The application supports two interaction modes: **GUI** (Graphical User Interface) and **CLI** (Command Line Interface).
You should specify in the cli arguments what mode you want to use.

---

## Design Patterns

The project follows several clean-coding principles and design patterns:

- **Observer Pattern**: Used to decouple the game logic (`Game`) from the UI (`GUIUI`, `CLIUI`). 
The UI listens for events like `MOVE_MADE`, `PIECE_CAPTURED`, and `GAME_END`. Also used to help manage screen transitions
in the `Window` class.
- **Strategy Pattern**: Implemented for the scoring system (`ScoreStrategy`), 
allowing for different point-calculation methods and for the movement of the pieces(`MoveStrategy`).
- **Factory Pattern**: Used in `PieceFactory` to instantiate different types of chess pieces, images for pieces or 
unicode characters based on input strings.
- **Singleton Pattern**: Used for the `Main` class and the `UIElements`

---

## Project Structure

### Core Components
- **Base.Main**: Entry point. Manages user sessions, persistence, and UI switching.
- **Base.Game**: The heart of the engine. Tracks players, move history, and evaluates game states.
- **Board.Board**: Handles the physical representation of the chess board and piece placement.
- **Board.Move**: Encapsulates a single move, including capture data and special moves.
- **Base.CLIU/Base.GUIUI**: Orchestrates the user interface in both modes

### Pieces & Movement
- **Pieces.Piece**: Abstract base class for all chess pieces.
- **Pieces.Moves.MoveStrategy**: Interface for different movement algorithms (e.g., `PawnMoveStrategy`, `KingMoveStrategy`).
- **Pieces.PieceFactory**: Simplifies piece creation.

### User Interface (GUI)
- **GUI.Window**: Main JFrame container using `CardLayout` to switch between Login, Menu, Game and End panels.
- **GUI.GUIBoard**: An interactive grid-based board representation.
- **GUI.GamePannel**: Displays the board alongside move history and captured pieces.
- **Custom Elements**: Custom styled elements for buttons, Combo boxes, Panels, Text and password fields and Radio buttons
- **Custom Pannels** for each window contained in the `Window` class


---

## Custom Exceptions
- **InvalidCommandException**: Thrown when user input is malformed or invalid.
- **EndOfGame**: Custom signal to terminate game execution.
- **InvalidLoginCredentials**: Handles incorrect password or existing email during signup.
- **InvalidMoveException**: Thrown when a move violates chess rules.
- **NotFoundException**: Thrown when a requested asset or user is not found.

---

## Testing
To test the system, you can use the following credentials:
- **Email**: `test@test.com`
- **Password**: `Test1!`

The project includes test cases for:
- Pawn promotion (Human and Computer).
- Checkmate and Stalemate scenarios.
- Persistence of points and captured pieces.
