
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.Ellipse;
import edu.macalester.graphics.Fillable;
import edu.macalester.graphics.GraphicsObject;
import edu.macalester.graphics.Rectangle;

public class Board {
    private Fillable[][] gameBoard;
    private int xBoxMargin, yBoxMargin;
    private int squareHeightAndWidth;
    private int turnCount;     
    // private static final int COLUMNS = 7;
    // private static final int ROWS = 6;
    private boolean gameIsOverInPosition;
    private BitBoard mask;
    private BitBoard yellow;
    private PositionEvaluator positionEvaluator;
    private Map<Integer, Integer> columnMap;

    public Board(Fillable[][] board) {
        gameBoard = new Fillable[7][6];
        xBoxMargin = 100;
        yBoxMargin = 80;
        squareHeightAndWidth = 70;
        gameIsOverInPosition = false;
        mask = new BitBoard(0b0000000000000000000000000000000000000000000000000); //length should be 49 (7*7)
        yellow = new BitBoard(0b0000000000000000000000000000000000000000000000000);
        positionEvaluator = new PositionEvaluator(new Node(this, yellow, mask, 0));
        initializeColumnMap();
    }

    public void initializeColumnMap(){
        columnMap = new HashMap<>();
        columnMap.put(0,7);
        columnMap.put(1,7);
        columnMap.put(2,7);
        columnMap.put(3,7);
        columnMap.put(4,7);
        columnMap.put(5,7);
        columnMap.put(6,7);
    }

    /**
     * Takes in the mouses X and Y and assigns it to the nearest column.
     * @param mouseX
     * @param mouseY
     * @return
     */
    public int getNearestColIndex(double mouseX, double mouseY){ 
        int column = -1;
        if(mouseX > xBoxMargin && mouseX < (squareHeightAndWidth * 7) + xBoxMargin) {
            column = (int) ((mouseX - xBoxMargin) / squareHeightAndWidth);          
        }
        return column;
    }

    /**
     *  If it is the users turn it gets the nearest column to the mouses x and y position 
     *  and places the piece in that column. The tree is then updated to refelct the current board state.
     *  When it is the bots turn it gets the next move and then gets that moves next column.
     * @param x
     * @param y
     * @return
     */
    public int getColToPlayIn(double x, double y){
        int column;
        if (turnCount % 2 == 0) {
            column = getNearestColIndex(x, y);
        } else {
            Node nextMove = positionEvaluator.getNextAIMove();
            column = nextMove.getMask().getColumnUsingNewMask(mask.getBit()); 
        }

        return column;
    }

    public void playerPlacePiece(double x, double y) {
        int column =  getColToPlayIn(x, y);
        if (column != -1 && !gameIsOverInPosition ) {
            Fillable[] fillableColumn = gameBoard[column];
            int row = 0;
            while (row < 6) {      // is less than 6 so that it represents the # of rows
                if (fillableColumn[row].getFillColor() != Color.WHITE) {
                    break;
                }
                row++;
            }   

            columnMap.put(column, row);
            updateGameState(column, row);
        }
    }

    public void updateGameState (int column, int row){
        if (!isColumnFull(column)) { 
            positionEvaluator.updateTree(column);
            gameBoard[column][row - 1].setFillColor(getPlayerColor());
            BitBoard updatedMask = mask.addBitPieceToMask(column);
            if(turnCount % 2 == 1){
                yellow.setBit(yellow.addBitToThisPosition(mask.getBit(), updatedMask.getBit()));
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


    public void initializePieces(CanvasWindow canvas){
        int colCount = 0;
        int rowCount = 0;
        for (Fillable[] col : gameBoard) {
            rowCount = 0;
            for (Fillable var: col) {
                Fillable square= new Rectangle(xBoxMargin + (70 * (colCount % 7)), yBoxMargin + (70 * (rowCount )), squareHeightAndWidth, squareHeightAndWidth);
                Fillable disc = new Ellipse(105 + (70 * (colCount % 7)), 85 + (70 * (rowCount )), 60, 60);
                ((Ellipse) disc).setFillColor(Color.WHITE);
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

    public void gameOver(boolean playerWon) {
        if (playerWon) {
            if (turnCount % 2 == 0) { 
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
        mask = new BitBoard(0b0000000000000000000000000000000000000000000000000L);
        yellow = new BitBoard(0b0000000000000000000000000000000000000000000000000L);
        positionEvaluator = new PositionEvaluator(new Node(this, yellow, mask, 0));
        initializeColumnMap();
    }   

    public boolean isColumnFull(int column){
        if(columnMap.get(column) == 0){
            return true;
        }
        return false;
    }

    public boolean getGameIsOverInPosition() {
        return gameIsOverInPosition;
    }

}

