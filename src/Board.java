import java.util.ArrayList;
import java.awt.Color;
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
    private int outcomeRed = 0;
    private BitBoard fullboard;
    private BitBoard unmask;
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

    public void placePiece(double x, double y){
        int index = getNearestColIndex(x, y);
        if (index != -1 && !gameIsOverInPosition) {
            Fillable[] col = gameBoard[index];
            int count = 0;
            while (count < 6) {      // is less than 6 so that it represents the # of rows
                if (col[count].getFillColor() != Color.WHITE) {
                    break;
                }
                count++;
            }
            if (count != 0) {
                gameBoard[index][count - 1].setFillColor(getPlayerColor());
                
                checkPiece(index, count-1, getPlayerColor());

                turnCount++;
            }
            if (turnCount == 42 && !gameIsOverInPosition) {
                gameOver(false);
            }
        }
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

                gameBoard[colCount][rowCount] = disc;

                rowCount ++;
            }
            colCount++;
        }
    }

    /*
     * for the tree to create nodes with unique boards
     */
    public boolean plopPiece(int x){
        Fillable[] col = gameBoard[x];
        int count = 0;
        while (count < 6) {       // is less than 6 so that it represents the # of rows
            if (col[count].getFillColor() != Color.WHITE) {
                break;
            }
            count++;
        }
        if (count != 0) {
            gameBoard[x][count - 1].setFillColor(getPlayerColor());
            
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

