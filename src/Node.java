import java.awt.Color;
import java.util.ArrayList;

import edu.macalester.graphics.Fillable;

public class Node {
    private BitBoard position;
    private BitBoard mask;
    private BitBoard playersPosition;
    private ArrayList<Node> children;   //retrieves the children of a specific node, i.e. a game state
    private int turn;
    // private GameManager manager;

    public Node(BitBoard hamiDownPosition, BitBoard hamiDownMask, int turnNumba) {
        children = new ArrayList<Node>();
        turn = turnNumba;
        position = hamiDownPosition;
        mask = hamiDownMask;
        
    }

    public int getTurn() {
        return turn;
    }

    /**
     * Prints out the possible nodes of the current game state. Probably should change the method name.
     */
    public void addChildren() {
        // if(children.isEmpty()){
        //     for (int i = 0; i < board.COLUMNS ; i++) {
        //         if(board.plopPiece(i)){
        //             Node node = new Node(board, turn + 1);
        //             children.add(node);
        //         }
        //     }
        // }
    }

    // private void printGetChildren(){ //for testing purposes. delete when done
    //     for (int i = 0; i < children.length; i++) {
    //         for (int j = 0; j < board[i].length; j++) {
    //             System.out.print(board[i][j]);
    //         }
    //         System.out.println();
    //     }
    // }

    public ArrayList<Node> getOrMakeChildren() {
        if(children.isEmpty()) addChildren();
        return children;
    }

    public boolean hasKids(){
        if(children.isEmpty()){
            return false;
        } else {
            return true;
        }
    }

    public Board getBoard(){
        return getBoard();
    }
}
