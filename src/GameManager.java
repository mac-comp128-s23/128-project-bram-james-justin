import java.awt.Color;
import java.awt.Paint;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.text.AttributeSet.ColorAttribute;

import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.Fillable;
import edu.macalester.graphics.GraphicsGroup;
import edu.macalester.graphics.GraphicsObject;

public class GameManager {
    private Board board;
    private CanvasWindow canvas;
    private final int CANVAS_WIDTH = 700;
    private final int CANVAS_HEIGHT = 600;
    private GraphicsGroup pieces;
    private Node node;

    private int turnCount;
    private boolean gameIsOver;
    private static final int COLUMNS = 7;
    private static final int ROWS = 6;
    private double outcomeYellow = 0;
    private double outcomeRed = 0;

    private int red = 1;    // every red token is a 1. This will be used to convert the gameboard into a matrix
    private int yellow = 2; // every yellow token is a 2. This will be used to convert the gameboard into a matrix
    private int white = 0;  // every white piece is a 0.

    public GameManager() {
        turnCount = 0;
        gameIsOver = false;
        board = new Board();
        canvas = new CanvasWindow("Board", CANVAS_WIDTH, CANVAS_HEIGHT);
        pieces = new GraphicsGroup();
        canvas.add(pieces, 0, 0);
        board.initializePieces(canvas);
        canvas.onClick(event -> {
            if (!gameIsOver) {
                takeATurn(event.getPosition().getX(), event.getPosition().getY());
            } else
                clearGame();
        });
    }

    public void takeATurn(double x, double y) {
        int index = board.getNearestColIndex(x, y);
        if (index != -1) {
            Fillable[] col = board.getGameBoard()[index];
            int count = 0;
            while (count < 6) {       // is less than 6 so that it represents the # of rows
                if (col[count].getFillColor() != Color.WHITE) {
                    break;
                }
                count++;
            }
            if (count != 0) {
                board.getGameBoard()[index][count - 1].setFillColor(getPlayerColor());
                canvas.draw();
                checkWin(getPlayerColor());

                turnCount++;
            }
            if (turnCount == 41 && !gameIsOver) {
                gameOver(false);
            }
        }
        // new Node(makeIntoMatrix(board.getGameBoard()), true);
        Node node = new Node(makeIntoMatrix(board.getGameBoard()), true);
        node.getChildren();
        System.out.println("||||||||||||||||||||||||||||||||||||||");

    }

    /**
     * !!!!DELETE WHEN DONE!!!!: prints out a matrix
     * @param board
     */
    public void printMakeIntoMatrix(int[][] board) {
        System.out.println("------------------------------------------------");
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                System.out.print(board[i][j]);
            }
            System.out.println();
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
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (getPieceColor(i, j) == Color.WHITE) {
                    newBoard[i][j] = 0;
                } else if (getPieceColor(i, j) == Color.RED) {
                    newBoard[i][j] = 1;
                } else if (getPieceColor(i, j) == Color.YELLOW) {
                    newBoard[i][j] = 2;
                }
            }
        }
        return newBoard;
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
        if (turnCount == 41 && !playerWon) {
            outcomeYellow = 0.5;
            outcomeRed = 0.5;
            System.out.println("Nobody is a winner");
        }
        gameIsOver = true;
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

    public void clearGame() {
        canvas.removeAll();
        canvas.add(pieces);
        turnCount = 0;
        board.initializePieces(canvas);
        gameIsOver = false;
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
            return (Color) board.getGameBoard()[column][row].getFillColor();
        }
        return Color.WHITE;
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

    public boolean placePiece() {
        return true;
    }


    public static void main(String[] args) {
        GameManager game = new GameManager();
    }
}
