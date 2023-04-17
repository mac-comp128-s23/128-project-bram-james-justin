import java.lang.reflect.Array;
import java.util.ArrayList;
import java.awt.Color;

import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.Ellipse;
import edu.macalester.graphics.Fillable;
import edu.macalester.graphics.GraphicsObject;
import edu.macalester.graphics.Rectangle;

public class Board {
    public Fillable[][] gameBoard;
    public int xBoxMargin, yBoxMargin;
    public int squareHeightAndWidth;
    public int turnCount;     
    private static final int COLUMNS = 7;
    private static final int ROWS = 6;
    private boolean gameIsOver;
    private int red = 1;    // every red token is a 1. This will be used to convert the gameboard into a matrix
    private int yellow = 2; // every yellow token is a 2. This will be used to convert the gameboard into a matrix
    private int white = 0;  // every white piece is a 0.
    private double outcomeYellow = 0;
    private double outcomeRed = 0;


    public Board(Fillable[][] board) {
        initializeBoard(board);
        xBoxMargin = 100;
        yBoxMargin = 80;
        squareHeightAndWidth = 70;
        gameIsOver = false;
    }

    public void initializeBoard(Fillable[][] board){
        if(board != null){
            gameBoard = board;
        } else {
            gameBoard = new Fillable[7][6];
        }
    }

    public int getNearestColIndex(double mouseX, double mouseY){ //gets the position of the mouse and then assigns it to the nearest column
        int answer = -1;
        if(mouseX > xBoxMargin && mouseX < (squareHeightAndWidth * 7) + xBoxMargin) {
            answer = (int) ((mouseX - xBoxMargin) / squareHeightAndWidth); //If something goes wrong check here          
        }
        return answer;
    }

    public void initializePieces(CanvasWindow canvas){   //1 is red, 2 is yellow
        int colCount = 0;
        int rowCount = 0;
        for (Fillable[] col : gameBoard) {
            rowCount = 0;
            for (Fillable var: col) {
                Fillable square= new Rectangle(xBoxMargin + (70 * (colCount % 7)), yBoxMargin + (70 * (rowCount )), squareHeightAndWidth, squareHeightAndWidth);
                Fillable disc = new Ellipse(105 + (70 * (colCount % 7)), 85 + (70 * (rowCount )), 60, 60);
                ((Ellipse) disc).setFillColor(Color.WHITE); //If we could set this to translucent that would be cool then we could have the sliding effect later.
                ((Rectangle) square).setFillColor(Color.BLUE);
                
                canvas.add((GraphicsObject) square);
                canvas.add((GraphicsObject) disc);
                System.out.println();
                gameBoard[colCount][rowCount] = disc;

                rowCount ++;
            }
            colCount++;
        }
    }

    public Fillable[][] getGameBoard() {
        return gameBoard;
    }

    public void placePiece(double x, double y){
        int index = getNearestColIndex(x, y);
        if (index != -1 && !gameIsOver) {
            Fillable[] col = getGameBoard()[index];
            int count = 0;
            while (count < 6) {       // is less than 6 so that it represents the # of rows
                if (col[count].getFillColor() != Color.WHITE) {
                    break;
                }
                count++;
            }
            if (count != 0) {
                getGameBoard()[index][count - 1].setFillColor(getPlayerColor());
                
                checkWin(getPlayerColor());

                turnCount++;
            }
            if (turnCount == 42 && !gameIsOver) {
                gameOver(false);
            }
        }
    }

    public void checkWin(Color color) {
        // Checks rows for 4 in a row
        for (int column = 0; column < COLUMNS; column++) {
            for (int row = 0; row < ROWS; row++) {
                if (getPieceColor(column, row) == color &&
                    getPieceColor(column + 1, row) == color &&
                    getPieceColor(column + 2, row) == color &&
                    getPieceColor(column + 3, row) == color) {
                    gameOver(true);
                }
                // Checks columns for 4 in a row.
                if (getPieceColor(column, row) == color && getPieceColor(column, row + 1) == color
                    && getPieceColor(column, row + 2) == color && getPieceColor(column, row + 3) == color) {
                    gameOver(true);
                }
                // Checks Rightwards increasing diagonal for 4 in a row.
                if (getPieceColor(column, row) == color &&
                    getPieceColor(column + 1, row - 1) == color &&
                    getPieceColor(column + 2, row - 2) == color &&
                    getPieceColor(column + 3, row - 3) == color) {
                    gameOver(true);
                }
                // Checks leftward increasing diagonal for 4 in a row.
                if (getPieceColor(column, row) == color && getPieceColor(column - 1, row - 1) == color
                    && getPieceColor(column - 2, row - 2) == color && getPieceColor(column - 3, row - 3) == color) {
                    gameOver(true);
                }
            }
        }
    }

      /**
     * Turns Fillable board into a matrix. Can be used for testing the algorithm
     * 
     * @param board
     * @return
     */
    public int[][] makeIntoMatrix(Fillable[][] board) {
        // iterate through the board and the get the player color at each board. if the piece is yellow,
        // return 2
        // if the piece is red, return 1.
        int[][] newBoard = new int[7][6];
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 6; j++) {
                if (getPieceColor(i, j) == Color.WHITE) {
                    newBoard[i][j] = white;
                } else if (getPieceColor(i, j) == Color.RED) {
                    newBoard[i][j] = red;
                } else if (getPieceColor(i, j) == Color.YELLOW) {
                    newBoard[i][j] = yellow;
                }
            }
        }
        return newBoard;
    }


    public Color getPlayerColor() {
        if (turnCount % 2 == 1) {
            return Color.YELLOW;
        } else {
            return Color.RED;
        }
    }

    public Color getPieceColor(int column, int row) {
        if (column < COLUMNS && column > -1 && row < ROWS && row > -1) {
            return (Color) getGameBoard()[column][row].getFillColor();
        }
        return Color.WHITE;
    }

    public void gameOver(boolean playerWon) {
        if (playerWon) {
            if (turnCount % 2 == 0) { // checks if player 1 wins because they are an even move number (first move made
                                      // by red is move 0), not 1
                System.out.println("Red Wins");
                outcomeRed = 1;
                outcomeYellow = 0;
            } else {
                System.out.println("Yellow wins");
                outcomeRed = 0;
                outcomeYellow = 1;
            }
        }
        if (!playerWon) {
            outcomeYellow = 0.5;
            outcomeRed = 0.5;
            System.out.println("Nobody is a winner");
        }
        gameIsOver = true;
    }

    public void resetGameTrackers(){
        gameIsOver = false;
        turnCount = 0;
    }

    public boolean getGameOver() {
        return gameIsOver;
    }

    public double getOutcomeRed() {
        return outcomeRed;
    }


    public double getOutcomeYellow() {
        return outcomeYellow;
    }

    public static void main(String[] args) {
        
    }
}

