import java.awt.Color;
import java.lang.reflect.Array;

import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.Ellipse;
import edu.macalester.graphics.GraphicsGroup;
import edu.macalester.graphics.GraphicsObject;
import edu.macalester.graphics.Rectangle;

public class GameManager {
    private Board board;
    private CanvasWindow canvas;
    private final int CANVAS_WIDTH = 700;
    private final int CANVAS_HEIGHT = 600; 
    private GraphicsGroup pieces;
    private Integer playerTurn;  //Red if 1, Yellow if 2
    private GraphicsObject [][] ellipseGameBoard;
    
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
                GraphicsObject square= new Rectangle(100 + (70 * (colCount % 7)), 80 + (70 * (rowCount )), 70, 70);
                GraphicsObject disc = new Ellipse(105 + (70 * (colCount % 7)), 85 + (70 * (rowCount )), 60, 60);
                ellipseGameBoard[colCount][rowCount]=disc;
                if(var == 0) {
                    ((Ellipse) disc).setFillColor(Color.WHITE); //If we could set this to translucent that would be cool then we could have the sliding effect later.
                }
                ((Rectangle) square).setFillColor(Color.BLUE);
                canvas.add(square);
                canvas.add(disc);
                rowCount ++;
            }
            colCount++;
        }
    }




    public void placeRed(){
        canvas.onMouseDown(event -> {
            ellipseGameBoard[]
        });
    }
    // public void placeYellow(){

    // }
// } else if(var == 1) {
//     ((Ellipse) disc).setFillColor(Color.RED);
// } else {
//     ((Ellipse) disc).setFillColor(Color.YELLOW);
// }

    public static void main(String[] args) {
        GameManager game = new GameManager();
    }
}
