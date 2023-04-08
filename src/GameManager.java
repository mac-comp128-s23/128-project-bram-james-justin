import java.awt.Color;
import java.awt.Paint;
import java.lang.reflect.Array;
import java.util.HashMap;

import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.Ellipse;
import edu.macalester.graphics.Fillable;
import edu.macalester.graphics.GraphicsGroup;
import edu.macalester.graphics.GraphicsObject;
import edu.macalester.graphics.Rectangle;

public class GameManager {
    private Board board;
    private CanvasWindow canvas;
    private final int CANVAS_WIDTH = 700;
    private final int CANVAS_HEIGHT = 600; 
    private GraphicsGroup pieces;
    private boolean player1Turn;  //Red if 1, Yellow if 2
    private GraphicsObject [][] ellipseGameBoard;
    private HashMap<Integer[][], GraphicsObject> tokenMap;
    private int turnCount;
    
    public GameManager() {
        turnCount = 0;
        board = new Board();
        canvas = new CanvasWindow("Board", CANVAS_WIDTH, CANVAS_HEIGHT);
        pieces = new GraphicsGroup();
        canvas.add(pieces, 0, 0);
        board.initializePieces(canvas);
    }

    public void displayBoard(){

    }

    // public void initializePieces(int[][] gameBoard){   //1 is red, 2 is yellow
    //     int colCount = 0;
    //     int rowCount = 0;
    //     for (int[] col : gameBoard) {
    //         rowCount = 0;
    //         for (int var: col) {
    //             GraphicsObject square= new Rectangle(100 + (70 * (colCount % 7)), 80 + (70 * (rowCount )), 70, 70);
    //             GraphicsObject disc = new Ellipse(105 + (70 * (colCount % 7)), 85 + (70 * (rowCount )), 60, 60);
    //             if(var == 0) {
    //                 ((Ellipse) disc).setFillColor(Color.WHITE); //If we could set this to translucent that would be cool then we could have the sliding effect later.
    //             }
    //             ((Rectangle) square).setFillColor(Color.BLUE);
    //             canvas.add(square);
    //             canvas.add(disc);
    //             // ellipseGameBoard[colCount][rowCount] = disc;

    //             rowCount ++;
    //         }
    //         colCount++;
    //     }
    // }


    // public Ellipse getToken() {
    //     return pieces;
    // }   

    public void placePiece(){
        canvas.onClick(event -> {
            int index = board.getNearestColIndex(event.getPosition().getX(), event.getPosition().getY());
            Fillable[] col = board.getGameBoard()[index];
            Boolean finished = false;
            int count = 0;
            if(turnCount < 43){
                while(count < ){
                    if(col[count].getFillColor() != Color.WHITE){
                        board.getGameBoard()[index][count - 1].setFillColor(getPlayerColor());
                        finished = true;
                    }
                    System.out.println("YYYAt");
                    count ++;
                }
            }
            canvas.draw();
            player1Turn = !player1Turn;
        });
    }
    // public void placeYellow(){

    // }
// } else if(var == 1) {
//     ((Ellipse) disc).setFillColor(Color.RED);
// } else {
//     ((Ellipse) disc).setFillColor(Color.YELLOW);
// }

    private Paint getPlayerColor() {
        Paint color;
        if(player1Turn) {
            color = Color.RED;
        } else {
            color = Color.YELLOW;
        }
        return color;
    }

    public static void main(String[] args) {
        GameManager game = new GameManager();
    }
}
