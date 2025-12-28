# Chess game
## Radulescu Andrei-Valentin 321CC
A Java implementation of a Chess game application with basic login infrastructure and persistent memory.
I found it to have a medium difficulty level, it took about 30h to make and the biggest difficulty was not letting pieces
move that let a king in check. Other than that is was easy, just long and repetitive to implement. And sometimes the
framework provided felt like the biggest difficulty was implementing the specification, not the game.


## Program flow
Once started you are asked if you already hava an account and redirected to login or register.
Your email and password is asked. If you create an account, the password must be strong enough and
the email shouldn't already exist in the database. Once login/ signup is successful, you can either create
a game, select an existing game or logout. 

You can create a PvP or vs Computer game. You are asked for a nickname and starting color for each player(if applicable)

You can also check an already started game's state, delete it or continue it.

While playing, you can type a position(formatted ***'A1'***) to check the possible moves of the piece there. You can also
move a piece by typing the move(***'A1-A2'***). Basic movement features are implemented without castle and ampasant. If a 
pawn reaches the other side you are asked to change it into another piece. 

You can reach the end of a game either if you resign, you reach a check-mate or a stale-mate(last 3 repeated moves).

## Classes overview

*Base.Main*
- Oversees login system
  - login() and newAccound() create/load _User_ acccounts
- Writes and reads from files
    - read() reads from JSON
    - write() writes to JSON
- Runs the application
  - getEmail(), getLogin(), getCommand() handle user input
- Yields execution to Base.Game
  - loadGame() and createGame() handle transfering control to Base.Game class and user input

*Base.User*
- Data representation of an application user
  - Setters and Getters
  - login() test for password

*Base.Player*
- Data representation for an in-game user
  - makeMove() updates board and internal structures accordingly
  - Setters and Getters
*Base.Game*
- Oversees the execution of the game phase
  - keeps track of current player(setCurrentPlayerColor(), switchPlayer())
  - Keeps move history
  - Checks game logic
    - checkForMate()
    - start() for a new game
    - resume() for playing a game
      - brings game into memory
      - handles turns
      - handles user input and moves
      - checks for end of game conditions
*Pieces*
  - All pieces have a class, derived from the abstract class piece that particularize the movement
  - The piece class has a function for generating moves, with the possibility of checking if it leaves player in check
    (deepCheck)
  - Each piece has a Colors attribute(BLACK/WHITE)

*Move*
  - Keeps track of move characteristics for history
*Position*
  - Validates and stores a board position
*ChessPair*
  - Generic pair of 2 types
*Board*
  - Orchestrates the board logic and mechanics
  - Moves pieces, checks for invalid choices and initializes board according to specifications

## Exceptions
*InvalidCommandException* - Base.User input invalid
*EndOfGame* - terminates game execution
*InvalidLoginCredentials* - Login issues(incorrect credentials)
*InvalidMoveException* - Move violates game rules
*NotFoundException* - Certain asset doesn't exist

## Testing
Use account test@test.com with password Test1!
Base.Game 13 tests changing your pawn, 14 the coputer's pawn, 15 check mate, 16 check, 17 - Take piece + points,
18 - Persistent take piece + points
