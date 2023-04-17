import java.awt.Color;
import java.util.ArrayList;

import edu.macalester.graphics.Fillable;

public class Node {
    private Board board;
    private ArrayList<int[][]> children;   //retrieves the children of a specific node, i.e. a game state
    private int[][] boardState;
    private int turn;
    // private GameManager manager;

    public Node(int[][] game, boolean b) {
        boardState = game;
        children = new ArrayList<>();
        addChildNode();
    }

    /**
     * Prints out the possible nodes of the current game state. Probably should change the method name.
     */
    public void addChildNode() {
        for (int i = 0; i < boardState.length; i++) { //column
            int[][] newBoard = boardState;
            for (int j = 0; j < newBoard[i].length; j++) { //row
                    if ((j < boardState.length-1) &&
                        newBoard[i+1][j] == 0) {
                        newBoard[i+1][j] = turn;
                        break;

                    } else if ((j < boardState.length-1) && newBoard[i][j+1] == 0) {
                        newBoard[i][j+1] = turn;
                        break;

                    } else if ((j < boardState.length-1) && newBoard[i-1][j] == 0) {
                        newBoard[i-1][j] = turn;
                        break;

                    } else if ((j < boardState.length-1) && newBoard[i][j-1] == 0) {
                        newBoard[i][j-1] = turn;
                        break;
                    }
            }
            children.add(newBoard);
        }
    }

    private void printGetChildren(int[][] board){ //for testing purposes. delete when done
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                System.out.print(board[i][j]);
            }
            System.out.println();
        }
    }

    public void getChildren() {
        for (int[][] child : children) {
            printGetChildren(child);
            System.out.println("+++++++++++++++++++++++++++++++++++");
        }
    }

    


}
