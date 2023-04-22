import java.awt.Color;
import java.awt.Paint;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;

import javax.swing.text.AttributeSet.ColorAttribute;

import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.Fillable;
import edu.macalester.graphics.GraphicsGroup;
import edu.macalester.graphics.GraphicsObject;

public class GameManager {
    private Board board;
    private Node gameState;
    private CanvasWindow canvas;
    private final int CANVAS_WIDTH = 700;
    private final int CANVAS_HEIGHT = 600;
    private GraphicsGroup pieces;

    public GameManager() {
        canvas = new CanvasWindow("Board", CANVAS_WIDTH, CANVAS_HEIGHT);
        pieces = new GraphicsGroup();
        board = new Board(null, null, null, null, null);
        board.initializeArrays();
        canvas.add(pieces, 0, 0);
        board.initializePieces(canvas);

        canvas.onClick(event -> {
            if (!board.getGameIsOverInPosition()) {
                takeATurn(event.getPosition().getX(), event.getPosition().getY());
            } else {
                clearBoard();
            }
        });
    }

    public void takeATurn(double x, double y) {
        board.placePiece(x, y); 
        canvas.draw();

        // new Node(makeIntoMatrix(board.getGameBoard()), true);
        printMakeIntoMatrix(board.makeIntoMatrix(board.getGameBoard()));

        // Node node = new Node(makeIntoMatrix(board.getGameBoard()), true);
        // node.getChildren();
        System.out.println("||||||||||||||||||||||||||||||||||||||");

    }

    /**
     * !!!!DELETE WHEN DONE!!!!: prints out a matrix
     * @param board
     */
    public void printMakeIntoMatrix(int[][] board) {
        System.out.println("--------------------------");
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                System.out.print(board[j][i] + " ");
            }
            System.out.println();
        }
    }

    public void clearBoard() {
        canvas.removeAll();
        canvas.add(pieces);
        board.initializePieces(canvas);
        board.resetGameTrackers();
    }

    public static void main(String[] args) {
        // GameManager game = new GameManager();
        BitBoard board = new BitBoard(0b000000000000000000000000000000000000000000);

    }
}
