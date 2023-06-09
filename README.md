# Connect Four Bot

Project by: Bram Nutt, James McCarthy, and Justin Chalichemala

Utilizes **kilt-graphics API** from **edu.macalester.graphics** for **Java**.

This project creates a Connect Four interactable GUI using the **kilt-graphics API** to allow the player to play against an artificial intelligence. The Connect Four board is represented using *Fillable 2D arrays*, but also a *bitstring* representation to allow for the bot to determine which moves are best. The player uses the red tokens while the bot uses yellow.

## How to Play
1. Run GameManager, then UI will appear. 

2. The player clicks on the unfilled column they want to play in to place token. 
3. Click again to have the computer make a move. 
4. The terminal prints out who won.
5. Click one more time to reset the game and play again. 

## Understanding Bitstrings for the Game
The entire board is represented as **42-bits** as it is a **7x6** game. Each space in the board is represented as a 0 or 1 on the bitboard, with *1s* representing where the bot's current and future positions will be. **_HOWEVER_**, for the purposes of shifting the board vertically and horizontally, the bitboard representation of the game is **49-bits** which is a **7x7** game. The top row of the bitboard contains a sentinel row to ensure shifting bits to check diagonals doesn't result in a piece appearing at an incorrect location.

**Shifting** a bit indicates that the current, most recent bit for the bot "shifts" right or left depending on what direction we want to check.

The bitboard is **masked** and **unmasked** to determine what possible locations of a valid move is for the bot. Unmasking gives the position of where the player's red tokens are, and masking shows where any piece is on the board. This allows the bot to determine what are the valid moves.

## Decision making using a Game Tree and Minimax Algorithm
The bot utilizes a game tree that contains the *current* board state as the root, and *all* possible valid moves the bot can take. Each child of the current board state contains a **score** which is calculated according to the number of two-in-a-rows it leads to, or if it's a winning state. To reduce computation time and to help understand what move to make, the **minimax algorithm** is utilized by examining the possible board states up to a specific depth in the tree. The algorithm prunes unnecessary branches to reduce computation time as well.

## Classes
**GameManager**: GameManager creates the graphic objects and handles the canvas click calls, as well as initializes the main instance of the Board(). **GameManager has the main method to run the program.**

**Board**: Board manages the fillable visual representation of the live game which calls position evaluator to choose moves for the ai to make. 

**PositionEvaluator**: Creates a game tree using the Node class, and uses a minimax algorithm and alpha-beta pruning to evaluate the nodes, allowing it to return moves for the AI and stay updated with player moves. 

**Node**: Each node is a game state, which holds the boards information as a pair of BitBoards, as well as other useful information about the position, and has some helper methods for modifying and duplicating bitboards.

**BitBoard**: A bitstring representation of the connect four board, including a sentinal row to allow for clean adjacency testing. It also has helper methods for testing adjacency and creating modified BitBoards. 
