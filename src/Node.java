import java.awt.Color;
import java.util.ArrayList;

import edu.macalester.graphics.Fillable;

public class Node {
    private Board board;
    private ArrayList<Node> children;   //retrieves the children of a specific node, i.e. a game state
    private int turn;
    // private GameManager manager;

    public Node(Fillable[][] fillables) {
        board = new Board(fillables);
        children = new ArrayList<Node>();

    }

    /**
     * Prints out the possible nodes of the current game state. Probably should change the method name.
     */
    public void addChildNode() {
        for (int i = 0; i < board.COLUMNS ; i++) {
            if(board.plopPiece(i)){
                Node node = new Node(board.getGameBoard());
                children.add(node);
            }
        }
    }

    // private void printGetChildren(){ //for testing purposes. delete when done
    //     for (int i = 0; i < children.length; i++) {
    //         for (int j = 0; j < board[i].length; j++) {
    //             System.out.print(board[i][j]);
    //         }
    //         System.out.println();
    //     }
    // }

    public void getChildren() {
        for (Node child : children) {
            // printGetChildren(child);
            System.out.println("+++++++++++++++++++++++++++++++++++");
        }
    }

    


}
