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
    private boolean gameOver;
    private static final int COLUMNS=7;
    private static final int ROWS=6;
    
    public GameManager() {
        turnCount = 0;
        gameOver = false;
        board = new Board();
        canvas = new CanvasWindow("Board", CANVAS_WIDTH, CANVAS_HEIGHT);
        pieces = new GraphicsGroup();
        canvas.add(pieces, 0, 0);
        board.initializePieces(canvas);
        canvas.onClick(event -> {
            int index = board.getNearestColIndex(event.getPosition().getX(), event.getPosition().getY());
            System.out.println(index);
            Fillable[] col = board.getGameBoard()[index];
            if(turnCount < 43){
                int count = 0;
                while(count < 6){
                    if(col[count].getFillColor() != Color.WHITE){
                        break;
                    }
                    count ++;
                }
                board.getGameBoard()[index][count - 1].setFillColor(getPlayerColor());
            }
            canvas.draw();
            player1Turn = !player1Turn;
            System.out.println(player1Turn);
            checkWin(getPlayerColor(player1Turn));
            System.out.println(getPlayerColor(player1Turn));
            
        });

    }

    public Color getPlayerColor(boolean turn){
        if(turn==true){
            return Color.YELLOW;
        }
        else if(turn==false){
            return Color.RED;
        }
        return Color.BLACK;
    }
    public Color getPieceColor(int column, int row){
        if(column<COLUMNS &&row<ROWS){
        return (Color) board.getGameBoard()[column][row].getFillColor();
        }
        return Color.WHITE;
    }
    public boolean checkWin(Color color){
        //Checks rows for Connect4
        for (int i=0; i<COLUMNS;i++){
            for(int j=0;j<ROWS;j++){
                if(getPieceColor(i, j)==color && getPieceColor(i+1, j)==color&& getPieceColor(i+2, j)==color&& getPieceColor(i+3, j)==color){
                    System.out.println("Won");
                    return true;
                }
            }  
        }
        return false;
    }

    public void displayBoard(){

    }

    public boolean placePiece(){
        return true;
    }

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
