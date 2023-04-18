import java.lang.reflect.Array;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.List;

import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.Ellipse;
import edu.macalester.graphics.Fillable;
import edu.macalester.graphics.GraphicsObject;
import edu.macalester.graphics.Point;
import edu.macalester.graphics.Rectangle;

public class Board {
    public Fillable[][] gameBoard;
    public int xBoxMargin, yBoxMargin;
    public int squareHeightAndWidth;
    public int turnCount;     
    public static final int COLUMNS = 7;
    private static final int ROWS = 6;
    private boolean gameIsOverInPosition;
    private int red = 1;    // every red token is a 1. This will be used to convert the gameboard into a matrix
    private int yellow = 2; // every yellow token is a 2. This will be used to convert the gameboard into a matrix
    private int white = 0;  // every white piece is a 0.
    private double outcomeRed = 0;
    private ArrayList<ArrayList<Point>> redList, yellowList; 
    private ArrayList<Point> redWins, yellowWins;


    public Board(Fillable[][] board, ArrayList<ArrayList<Point>> redOne, ArrayList<ArrayList<Point>> yellowOne, ArrayList<Point> redThree, ArrayList<Point> yellowThree) {
        initializeBoard(board);
        xBoxMargin = 100;
        yBoxMargin = 80;
        squareHeightAndWidth = 70;
        gameIsOverInPosition = false;
        outcomeRed = 5000;
        redList = redOne;
        yellowList = yellowOne;
        redWins = redThree;
        yellowWins = yellowThree;
    }
   
    public void initializeArrays(){
        redList = new ArrayList<ArrayList<Point>>();
        yellowList = new ArrayList<ArrayList<Point>>();
        redWins = new ArrayList<>();
        yellowWins = new ArrayList<>();
    }

    public void initializeBoard(Fillable[][] board){
        if(board != null){
            gameBoard = board;
        } else {
            gameBoard = new Fillable[7][6];
        }
    }

    public int getNearestColIndex(double mouseX, double mouseY){ //gets the position of the mouse and then assigns it to the nearest column
        int answer = -1;
        if(mouseX > xBoxMargin && mouseX < (squareHeightAndWidth * 7) + xBoxMargin) {
            answer = (int) ((mouseX - xBoxMargin) / squareHeightAndWidth); //If something goes wrong check here          
        }
        return answer;
    }

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
                System.out.println();
                gameBoard[colCount][rowCount] = disc;

                rowCount ++;
            }
            colCount++;
        }
    }

    public Fillable[][] getGameBoard() {
        return gameBoard;
    }

    public void placePiece(double x, double y){
        int index = getNearestColIndex(x, y);
        if (index != -1 && !gameIsOverInPosition) {
            Fillable[] col = getGameBoard()[index];
            int count = 0;
            while (count < 6) {       // is less than 6 so that it represents the # of rows
                if (col[count].getFillColor() != Color.WHITE) {
                    break;
                }
                count++;
            }
            if (count != 0) {
                getGameBoard()[index][count - 1].setFillColor(getPlayerColor());
                
                checkPiece(index, count-1, getPlayerColor());

                turnCount++;
            }
            if (turnCount == 42 && !gameIsOverInPosition) {
                gameOver(false);
            }
        }
    }

    /*
     * for the tree to create nodes with unique boards
     */
    public boolean plopPiece(int x){
        Fillable[] col = getGameBoard()[x];
        int count = 0;
        while (count < 6) {       // is less than 6 so that it represents the # of rows
            if (col[count].getFillColor() != Color.WHITE) {
                break;
            }
            count++;
        }
        if (count != 0) {
            getGameBoard()[x][count - 1].setFillColor(getPlayerColor());
            
            checkPiece(x, count-1, getPlayerColor());

            turnCount++;
        } else {
            return false;
        }
        if (turnCount == 42) {
            gameOver(false);
        }
        return true;
    }

    public boolean checkPiece(int x, int y, Color color) {
        // Checks rows for 4 in a row
        // if (getPieceColor(x, y) == color &&
        //     getPieceColor(x + 1, y) == color &&
        //     getPieceColor(x + 2, y) == color &&
        //     getPieceColor(x + 3, y) == color ||
        //     getPieceColor(x, y) == color &&
        //     getPieceColor(x - 1, y) == color &&
        //     getPieceColor(x - 2, y) == color &&
        //     getPieceColor(x - 3, y) == color) {
        //     gameOver(true);
        //     return true;
        // }
        // // Checks columns for 4 in a row.
        // if (getPieceColor(x, y) == color &&
        //     getPieceColor(x, y + 1) == color && 
        //     getPieceColor(x, y + 2) == color && 
        //     getPieceColor(x, y + 3) == color ||
        //     getPieceColor(x, y) == color &&
        //     getPieceColor(x, y - 1) == color && 
        //     getPieceColor(x, y - 2) == color && 
        //     getPieceColor(x, y - 3) == color){
        //     gameOver(true);
        //     return true;
        // }
        // // Checks Rightwards  diagonal for 4 in a row.
        // if (getPieceColor(x, y) == color &&
        //     getPieceColor(x + 1, y - 1) == color &&
        //     getPieceColor(x + 2, y - 2) == color &&
        //     getPieceColor(x + 3, y - 3) == color ||
        //     getPieceColor(x, y) == color &&
        //     getPieceColor(x - 1, y - 1) == color &&
        //     getPieceColor(x - 2, y - 2) == color &&
        //     getPieceColor(x - 3, y - 3) == color) {
        //     gameOver(true);
        //     return true;
        // }
        // // Checks leftward diagonal for 4 in a row.
        // if (getPieceColor(x, y) == color &&
        //     getPieceColor(x - 1, y - 1) == color &&
        //     getPieceColor(x - 2, y - 2) == color &&
        //     getPieceColor(x - 3, y - 3) == color ||
        //     getPieceColor(x, y) == color &&
        //     getPieceColor(x + 1, y + 1) == color &&
        //     getPieceColor(x + 2, y + 2) == color &&
        //     getPieceColor(x + 3, y + 3) == color) {
        //     gameOver(true);
        //     return true;
        // }
        // return false;
        Point addedDiscCoordinates = new Point(x, y);
        
        if(!checkWin(addedDiscCoordinates)){ // check to see if we met our win con
            ArrayList<ArrayList<Point>> mightyList;
            ArrayList<Point> mightyWinList;
            ArrayList<ArrayList<Point>> opponentList;
            ArrayList<Point> opponentWinList;
            if(getPlayerColor() == Color.RED){
                mightyList = redList;
                mightyWinList = redWins;
                opponentList = yellowList;
                opponentWinList = yellowWins;
            } else {
                mightyList = yellowList;
                mightyWinList = yellowWins;
                opponentList = redList;
                opponentWinList = redWins;
            }

            for(ArrayList<Point> list: mightyList){ //move players two-move-wins to one-move-wins list
                for(Point p: list){
                    if(addedDiscCoordinates == p) {
                        mightyList.remove(list);
                        list.remove(addedDiscCoordinates);
                        mightyWinList.add(list.get(0));
                    }
                }
            } 
            
            for(ArrayList<Point> list: opponentList){ // remove opponents blocked two-move wins
                for(Point p: list){
                    if(addedDiscCoordinates == p) {
                        opponentList.remove(list);
                    }
                }
            } 
            if(opponentWinList.contains(addedDiscCoordinates)) opponentWinList.remove(addedDiscCoordinates); //remove opponents blocked one move wins
            
            addWinConditions(addedDiscCoordinates, mightyList);
        }   
        // if(getPieceColor(x + 1, y))
        System.out.println(redList.toString());
        return true;
    }  
    
    public void addWinConditions(Point p, ArrayList<ArrayList<Point>> winList){
        ArrayList<Point> list = adjacencyList(p);
        Color checkColor = getPlayerColor();
        for (Point point : list) {
            if(checkColor == getPieceColor( point.getX(),  point.getY())){
                int vectorX =  (int)(p.getX() - point.getX());
                int vectorY =  (int)(p.getY() - point.getY());

                if(getPieceColor( p.getX() + (2 * vectorX),  p.getY() + (2 * vectorY)) == checkColor &&
                getPieceColor( p.getX() -  vectorX,  p.getY() - vectorY) == checkColor){
                    ArrayList<Point> pointPair = new ArrayList<Point>();
                    pointPair.add(new Point((p.getX() + (2 * vectorX)), p.getY() + (2*vectorY)));
                    pointPair.add(new Point( p.getX() -  vectorX,  p.getY() - vectorY));
                    winList.add(pointPair);
                }

                if(getPieceColor( p.getX() + 3*(vectorX),  p.getY() + 3*(vectorY) ) == checkColor &&
                getPieceColor( p.getX() + 2*(vectorX),  p.getY() + 2*(vectorY)) == checkColor){
                    ArrayList<Point> pointPair = new ArrayList<Point>();
                    pointPair.add(new Point((p.getX() + 3*(vectorX)), p.getY() + 3*(vectorY)));
                    pointPair.add(new Point( p.getX() +  2*(vectorX),  p.getY() + 2*(vectorY)));
                    winList.add(pointPair);
                }

                if(getPieceColor(p.getX() - vectorX, p.getY() - vectorY) == checkColor &&
                getPieceColor(p.getX() -  2*(vectorX), p.getY() -  2*(vectorY)) == checkColor){
                    ArrayList<Point> pointPair = new ArrayList<Point>();
                    pointPair.add(new Point((p.getX() - vectorX), p.getY() - vectorY));
                    pointPair.add(new Point( p.getX() -  2*(vectorX),  p.getY() -  2*(vectorY)));
                    winList.add(pointPair);
                }

            }
        }
    }

    public ArrayList<Point> adjacencyList(Point p){
        ArrayList<Point> list = new ArrayList<Point>();
        list.add(new Point(p.getX()+1, p.getY()+1));
        list.add(new Point(p.getX()-1, p.getY()+1));
        list.add(new Point(p.getX(), p.getY()+1));
        list.add(new Point(p.getX()+1, p.getY()));
        list.add(new Point(p.getX()+1, p.getY()-1));
        list.add(new Point(p.getX(), p.getY()-1));
        list.add(new Point(p.getX()-1, p.getY()-1));
        list.add(new Point(p.getX()-1, p.getY()));
        return list;
    }

    public boolean checkWin(Point last){
        if(Color.RED == getPlayerColor()){
            if(redWins.contains(last)) {
                gameOver(true);
                return true;
            } 
        }else {
            if(yellowWins.contains(last)) {
                gameOver(true);
                return true;
            }    
        }
        return false;
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
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 6; j++) {
                if (getPieceColor(i, j) == Color.WHITE) {
                    newBoard[i][j] = white;
                } else if (getPieceColor(i, j) == Color.RED) {
                    newBoard[i][j] = red;
                } else if (getPieceColor(i, j) == Color.YELLOW) {
                    newBoard[i][j] = yellow;
                }
            }
        }
        return newBoard;
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
            return (Color) getGameBoard()[(int) d][(int) e].getFillColor();
        }
        return Color.WHITE;
    }

    public void gameOver(boolean playerWon) {
        if (playerWon) {
            if (turnCount % 2 == 0) { // checks if player 1 wins because they are an even move number (first move made
                                      // by red is move 0), not 1
                System.out.println("Red Wins");
                outcomeRed = 10000;
            } else {
                System.out.println("Yellow wins");
                outcomeRed = 0;
            }
        }
        if (!playerWon) {
            outcomeRed = 50;
            System.out.println("Nobody is a winner");
        }
        gameIsOverInPosition = true;
    }

    public void resetGameTrackers(){
        gameIsOverInPosition = false;
        turnCount = 0;
    }

    public boolean getGameIsOverInPosition() {
        return gameIsOverInPosition;
    }

    public static void main(String[] args) {
        
    }
}

