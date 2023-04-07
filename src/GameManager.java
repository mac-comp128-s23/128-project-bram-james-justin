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

    public void initializePieces(int[][] gameBoard){   //1 is red, 2 is yellow
        int colCount = 0;
        int rowCount = 0;
        for (int[] col : gameBoard) {
            rowCount = 0;
            for (int var: col) {
                GraphicsObject disc = new Ellipse(30 + (70 * (colCount % 7)), 30 + (70 * (rowCount )), 70, 70);
                if(var == 0) {
                    ((Ellipse) disc).setFillColor(Color.GRAY);
                } else if(var == 1) {
                    ((Ellipse) disc).setFillColor(Color.RED);
                } else {
                    ((Ellipse) disc).setFillColor(Color.YELLOW);
                }
                canvas.add(disc);
                rowCount ++;
            }
            colCount++;
        }
    }


    public static void main(String[] args) {
        GameManager game = new GameManager();
    }
}
