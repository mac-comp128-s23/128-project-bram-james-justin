import java.awt.Color;
import java.lang.reflect.Array;

import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.Ellipse;
import edu.macalester.graphics.GraphicsGroup;
import edu.macalester.graphics.GraphicsObject;

public class GameManager {
    private Board board;
    private CanvasWindow canvas;
    private final int CANVAS_WIDTH = 700;
    private final int CANVAS_HEIGHT = 600; 
    private GraphicsGroup pieces;
    
    public GameManager() {
        board = new Board();
        canvas = new CanvasWindow("Board", CANVAS_WIDTH, CANVAS_HEIGHT);
        pieces = new GraphicsGroup();
        initializePieces(board.getGameBoard());
        canvas.add(pieces, 0, 0);
    }

    public void displayBoard(){

    }

    public void initializePieces(int[][] gameBoard){
        int colCount = 0;
        for (int[] col : gameBoard) {
            for (int rowCount: col) {
                GraphicsObject disc = new Ellipse(30 + (70 * (colCount % 7)), 30 + (70 * (rowCount )), 70, 70);
                if(gameBoard[colCount][rowCount] == 0) {
                    ((Ellipse) disc).setFillColor(Color.GRAY);
                } else if(gameBoard[colCount][rowCount] == 1) {
                    ((Ellipse) disc).setFillColor(Color.RED);
                } else {
                    ((Ellipse) disc).setFillColor(Color.YELLOW);
                }
                canvas.add(disc);
            }
            colCount++;
        }
    }

    public static void main(String[] args) {
        GameManager game = new GameManager();
    }
}
