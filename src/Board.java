
import java.awt.Color;
import java.util.BitSet;

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
    public static final int COLUMNS = 7;
    private static final int ROWS = 6;
    private boolean gameIsOverInPosition;
    private BitBoard mask;
    private BitBoard yellow;
    private PositionEvaluator PE;

    public Board() {
        gameBoard = new Fillable[7][6];
        xBoxMargin = 500;
        yBoxMargin = 480;
        squareHeightAndWidth = 70;
        gameIsOverInPosition = false;
        mask = new BitBoard(0b0000000000000000000000000000000000000000000000000); //length should be 49 (7*7)
        yellow = new BitBoard(0b0000000000000000000000000000000000000000000000000);
        PE = new PositionEvaluator(new Node(yellow, mask, 0));
    }

    public void turn(int x, int y) throws Exception{
        if(turnCount % 2 == 0){
            playerPlacePiece(x, y);
        } else{
            AIMove();
        }
    }

    public int getNearestColIndex(double mouseX, double mouseY){ //gets the position of the mouse and then assigns it to the nearest column
        int answer = -1;
        if(mouseX > xBoxMargin && mouseX < (squareHeightAndWidth * 7) + xBoxMargin) {
            answer = (int) ((mouseX - xBoxMargin) / squareHeightAndWidth); //If something goes wrong check here          
        }
        return answer;
    }

    public void playerPlacePiece(double x, double y) throws Exception{
        int index = getNearestColIndex(x, y);
        if (index != -1) {
            Node board = PE.makePlayerMove(index);
            if(board.getGameIsOverInPosition()) gameOver(true);
            updateBoard(board.getYellowString(), board.getRedString());
                        /// add check for tie if we ever let AI go first!! 
        }
    } 

    public void AIMove() throws Exception{
        Node board = PE.makeAIMove();
        if(board.getGameIsOverInPosition()){
            gameOver(true);
        }
        updateBoard(board.getYellowString(), board.getRedString());
    }

    public void updateBoard(long yellow, long red){
        BitSet y = BitSet.valueOf(new long[] {yellow});
        BitSet r = BitSet.valueOf(new long[] {red});
        int count = 0;
        for (Fillable[] fillables : gameBoard) {
            for(Fillable square: fillables){
                if(y.get(count)){
                    square.setFillColor(Color.YELLOW);
                } else if (r.get(count)){
                    square.setFillColor(Color.RED);
                }
                if(count % 7 == 0) {
                    count += 2;
                } else {
                    count++;
                }
            }
        }
    }





    // public void TESTTHENODES(){
    //     Node n = new Node(yellow, mask, turnCount);
    //     System.out.println(n.evaluateNode());
    // }

    public void initializePieces(CanvasWindow canvas){   //1 is red, 2 is yellow
        int colCount = 0;
        int rowCount = 0;
        for (Fillable[] col : gameBoard) {
            rowCount = 0;
            for (Fillable v: col) {
                Fillable square= new Rectangle(xBoxMargin - (70 * (colCount % 7)), yBoxMargin - (70 * (rowCount )), squareHeightAndWidth, squareHeightAndWidth);
                Fillable disc = new Ellipse(xBoxMargin +5 - (70 * (colCount % 7)), yBoxMargin + 5 - (70 * (rowCount )), 60, 60);
                ((Ellipse) disc).setFillColor(Color.WHITE); //If we could set this to translucent that would be cool then we could have the sliding effect later.
                ((Rectangle) square).setFillColor(Color.BLUE);
                
                canvas.add((GraphicsObject) square);
                canvas.add((GraphicsObject) disc);

                gameBoard[colCount][rowCount] = disc;

                rowCount ++;
            }
            colCount++;
        }
    }

    public Color getPlayerColor() {
        if (turnCount % 2 == 1) {
            return Color.YELLOW;
        } else {
            return Color.RED;
        }
    }

    public Color getPieceColor(double d, double e) {
        if (d < COLUMNS && d > -1 && e < ROWS && e > -1) {
            return (Color) gameBoard[(int) d][(int) e].getFillColor();
        }
        return Color.WHITE;
    }

    public void gameOver(boolean playerWon) {
        if (playerWon) {
            if (turnCount % 2 == 0) { // checks if player 1 wins because they are an even move number (first move made
                                      // by red is move 0), not 1
                System.out.println("Red Wins");
            } else {
                System.out.println("Yellow wins");
            }
        }
        if (!playerWon) {
            System.out.println("Nobody is a winner");
        }
        gameIsOverInPosition = true;
    }

    public void resetGameTrackers(){
        gameIsOverInPosition = false;
        turnCount = 0;
        mask = new BitBoard(0b0000000000000000000000000000000000000000000000000);
        yellow = new BitBoard(0b0000000000000000000000000000000000000000000000000);
    }

    public boolean getGameIsOverInPosition() {
        return gameIsOverInPosition;
    }

    public void setGameIsOverInPosition(boolean gameIsOver){
        gameIsOverInPosition = gameIsOver;
    }

    public static void main(String[] args) {
            GameManager game = new GameManager();
            BitBoard board = new BitBoard(0b0000000000000000000000000000000000000000000000000);
    
        
    }
}

