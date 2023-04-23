import java.awt.Color;
import java.util.ArrayList;

import edu.macalester.graphics.Fillable;

public class Node {
    private BitBoard position; // has 1s where computer has a disc (yellow)
    private BitBoard mask; // has 1s where there is a disc
    private BitBoard playersPosition;
    private ArrayList<Node> children;   //retrieves the children of a specific node, i.e. a game state
    private int turn;
    private boolean gameIsOverInPosiiton;
    // private GameManager manager;

    public Node(BitBoard hamiDownPosition, BitBoard hamiDownMask, int turnNumba) {
        children = new ArrayList<Node>();
        turn = turnNumba;
        position = hamiDownPosition;
        mask = hamiDownMask;
        decideLeafStatus();
    }

    public void decideLeafStatus(){
        if(turn % 2 == 0) {
            gameIsOverInPosiiton = position.checkWin();
        } else {
            gameIsOverInPosiiton = position.unMask(mask).checkWin();
        }
    }

    public int getTurn() {
        return turn;
    }

    public boolean wasNewBoardCreated(BitBoard newBoard){
        if(newBoard.bit - mask.bit > 0) return true;
        return false;
    }

    /**
     * Adds all the children of a current boardstate. Doesn't make ALL possible children for the entire tree, for purposes of space. 
     * Will be used for analysis of which child is the best.
     */
    public void addChildren() {
        if(children.isEmpty()){
            for (int i = 0; i < Board.COLUMNS ; i++) {
                BitBoard newMask = mask.addBitPiece(i);
                if(wasNewBoardCreated(newMask)){
                    Node node = new Node(position.addBitPiece(i), newMask, turn + 1);
                    children.add(node);
                }
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

    public double evaluateNode(Node current) {

        return 0;
    }


}
