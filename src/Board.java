
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
    public static final int COLUMNS = 7;
    private static final int ROWS = 6;
    private boolean gameIsOverInPosition;
    private BitBoard mask;
    private BitBoard yellow;
    public PositionEvaluator positionEvaluator;

    public Board(Fillable[][] board) {
        gameBoard = new Fillable[7][6];
        xBoxMargin = 100;
        yBoxMargin = 80;
        squareHeightAndWidth = 70;
        gameIsOverInPosition = false;
        mask = new BitBoard(0b0000000000000000000000000000000000000000000000000); //length should be 49 (7*7)
        yellow = new BitBoard(0b0000000000000000000000000000000000000000000000000);
        positionEvaluator = new PositionEvaluator(new Node(yellow, mask, 0));
    }

    public int getNearestColIndex(double mouseX, double mouseY){ //gets the position of the mouse and then assigns it to the nearest column
        int answer = -1;
        if(mouseX > xBoxMargin && mouseX < (squareHeightAndWidth * 7) + xBoxMargin) {
            answer = (int) ((mouseX - xBoxMargin) / squareHeightAndWidth); //If something goes wrong check here          
        }
        System.out.println("check for -1 " + answer);
        return answer;
    }

    public int getColToPlayIn(double x, double y){
        int column;
        if (turnCount % 2 == 0) {
            column = getNearestColIndex(x, y);
            positionEvaluator.updateTree(column);
        } else {
            Node nextMove = positionEvaluator.getNextAIMove();
            column = getColumn(nextMove.yellowPos.bit ^ yellow.bit);
        }
        return column;
    }

    public void playerPlacePiece(double x, double y) {
        int index =  getColToPlayIn(x, y);
        if (index != -1 && !gameIsOverInPosition) {
            Fillable[] col = gameBoard[index];
            int count = 0;
            while (count < 6) {      // is less than 6 so that it represents the # of rows
                // System.out.println("last move went in column: " + index);
                if (col[count].getFillColor() != Color.WHITE) {
                    break;
                }
                count++;
            }
            System.out.println("count " + count);
            if (count != 0) {
                gameBoard[index][count - 1].setFillColor(getPlayerColor());
                // System.out.println("last move went in column: " + index);
                BitBoard updatedMask = mask.addBitPieceToMask(index);
                if(turnCount % 2 == 1){
                    yellow.bit = yellow.addBitToThisPosition(mask.bit, updatedMask.bit);
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
            if (turnCount == 42 && !gameIsOverInPosition) {
                gameOver(false);
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
            for (Fillable var: col) {
                Fillable square= new Rectangle(xBoxMargin + (70 * (colCount % 7)), yBoxMargin + (70 * (rowCount )), squareHeightAndWidth, squareHeightAndWidth);
                Fillable disc = new Ellipse(105 + (70 * (colCount % 7)), 85 + (70 * (rowCount )), 60, 60);
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
        positionEvaluator = new PositionEvaluator(new Node(yellow, mask, 0));
    }   

    public boolean getGameIsOverInPosition() {
        return gameIsOverInPosition;
    }

    public void setGameIsOverInPosition(boolean gameIsOver){
        gameIsOverInPosition = gameIsOver;
    }

    /**
     * Gets the column based on the location of the first one in the bitstring.
     * @param newestBitPiece
     * @return
     */
    public int getColumn(Long newestBitPiece){
        int trailingZeroes = Long.numberOfTrailingZeros(newestBitPiece);
        return trailingZeroes/7; 
    }

    public static void main(String[] args) {
        GameManager game = new GameManager();
      
        //
    }
}

