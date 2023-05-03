
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.Ellipse;
import edu.macalester.graphics.Fillable;
import edu.macalester.graphics.GraphicsObject;
import edu.macalester.graphics.Rectangle;

public class Board {
    private Fillable[][] gameBoard;
    private int xBoxMargin, yBoxMargin;
    private int squareHeightAndWidth;
    private int turnCount;     
    private static final int COLUMNS = 7;
    private static final int ROWS = 6;
    private boolean isGameOver;
    private BitBoard mask;
    private BitBoard yellow;
    private PositionEvaluator positionEvaluator;
    private Map<Integer, Integer> columnMap;

    /**
     * Creates a Fillable board of size 7x6, 42 bits with underlying bitboards of size 7x7, 49-bits for AI move determination.
     * @param Fillable board
     */
    public Board(Fillable[][] board) {
        gameBoard = new Fillable[COLUMNS][ROWS];
        xBoxMargin = 100;
        yBoxMargin = 80;
        squareHeightAndWidth = 70;
        isGameOver = false;
        mask = new BitBoard(0b0000000000000000000000000000000000000000000000000);
        yellow = new BitBoard(0b0000000000000000000000000000000000000000000000000);
        positionEvaluator = new PositionEvaluator(new Node(this, yellow, mask, 0));
        initializeColumnMap();
    }

    /**
     * Initializes map that stores each column as a key and the amount of empty spots (including sentinel node row)
     * per column (7 slots per column).
     */
    public void initializeColumnMap(){
        columnMap = new HashMap<>();
        columnMap.put(0,7);
        columnMap.put(1,7);
        columnMap.put(2,7);
        columnMap.put(3,7);
        columnMap.put(4,7);
        columnMap.put(5,7);
        columnMap.put(6,7);
    }

    /**
     * Takes in the mouse's X and Y and assigns it to the nearest column based on the board's margins.
     * @param mouseX
     * @param mouseY
     * @return A specified column
     */
    public int getNearestColIndex(double mouseX, double mouseY){ 
        int column = -1;
        if(mouseX > xBoxMargin && mouseX < (squareHeightAndWidth * COLUMNS) + xBoxMargin) {
            column = (int) ((mouseX - xBoxMargin) / squareHeightAndWidth);          
        }
        return column;
    }

    /**
     * Calls getNearestColIndex when the player's turn and places the piece in that column. The tree is then 
     * updated to reflect the current board state. When it is the bot's turn it gets the next move and the next move's column.
     * @param x
     * @param y
     * @return
     */
    public int getSpecificCol(double x, double y){
        int column;
        if (turnCount % 2 == 0) {
            column = getNearestColIndex(x, y);
        } else {
            Node nextMove = positionEvaluator.getNextAIMove();
            column = nextMove.getMask().getColumnUsingNewMask(mask.getBit()); 
        }

        return column;
    }

    /**
     * Places the player's piece if the game is not finished and is within bounds and puts it into the columnMap.
     * @param x
     * @param y
     */
    public void placePiece(double x, double y) {
        int column =  getSpecificCol(x, y);
        if (column != -1 && !isGameOver ) {
            Fillable[] fillableColumn = gameBoard[column];
            int row = 0;
            while (row < ROWS) {
                if (fillableColumn[row].getFillColor() != Color.WHITE) {
                    break;
                }
                row++;
            }   

            columnMap.put(column, row);
            updateGameState(column, row);
        }
    }

    /**
     * Updates the game tree based on whether a column is not full, and adds yellow's bit to a bitboard to determine
     * whether a future game state wins the game. Determines whether the game is finished or not.
     * @param column
     * @param row
     */
    public void updateGameState (int column, int row){
        if (!isColumnFull(column)) { 
            positionEvaluator.updateTree(column);
            gameBoard[column][row - 1].setFillColor(getPlayerColor());
            BitBoard updatedMask = mask.addBitPieceToMask(column);
            if(turnCount % 2 == 1){
                yellow.setBit(yellow.addBitToThisPosition(mask.getBit(), updatedMask.getBit()));
                if(yellow.checkWin()){
                    gameOver(true);
                }
            } else {
                if(yellow.unMask(updatedMask).checkWin()){
                    gameOver(true);
                }
            }
            mask = updatedMask;
            turnCount++;
        }

        if (turnCount == 42 && !isGameOver) {
            gameOver(false);
        }
    }


    /**
     * Initializes the game pieces and board on canvas.
     * @param canvas
     */
    public void initializePieces(CanvasWindow canvas){
        int colCount = 0;
        int rowCount = 0;
        for (Fillable[] col : gameBoard) {
            rowCount = 0;
            for (Fillable var: col) {
                Fillable square= new Rectangle(xBoxMargin + (70 * (colCount % COLUMNS)), yBoxMargin + (70 * (rowCount )), squareHeightAndWidth, squareHeightAndWidth);
                Fillable disc = new Ellipse(105 + (70 * (colCount % 7)), 85 + (70 * (rowCount )), 60, 60);
                ((Ellipse) disc).setFillColor(Color.WHITE);
                ((Rectangle) square).setFillColor(Color.BLUE);
                
                canvas.add((GraphicsObject) square);
                canvas.add((GraphicsObject) disc);

                gameBoard[colCount][rowCount] = disc;

                rowCount ++;
            }
            colCount++;
        }
    }

    /**
     * Determines which player has what token. For the purposes of this project, the human player is red and the
     * bot is yellow.
     * @return
     */
    public Color getPlayerColor() {
        if (turnCount % 2 == 1) {
            return Color.YELLOW;
        } else {
            return Color.RED;
        }
    }

    /**
     * Prints out in the terminal who has won the game.
     * @param playerWon
     */
    public void gameOver(boolean playerWon) {
        if (playerWon) {
            if (turnCount % 2 == 0) { 
                System.out.println("Red Wins");
            } else {
                System.out.println("Yellow wins");
            }
        }
        if (!playerWon) {
            System.out.println("Nobody is a winner");
        }
        isGameOver = true;
    }

    /**
     * Resets the game variables needed to play a new game.
     */
    public void resetGameTrackers(){
        isGameOver = false;
        turnCount = 0;
        mask = new BitBoard(0b0000000000000000000000000000000000000000000000000L);
        yellow = new BitBoard(0b0000000000000000000000000000000000000000000000000L);
        positionEvaluator = new PositionEvaluator(new Node(this, yellow, mask, 0));
        initializeColumnMap();
    }   

    /**
     * Checks if the column is full for a specific column
     * @param column
     * @return
     */
    public boolean isColumnFull(int column){
        if(columnMap.get(column) == 0){
            return true;
        }
        return false;
    }

    /**
     * @return The current value of gameIsOverInPostion
     */
    public boolean getIsGameOver() {
        return isGameOver;
    }

}

