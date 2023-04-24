import java.awt.Color;
import java.util.ArrayList;

import edu.macalester.graphics.Fillable;

public class Node {
    private BitBoard yPosition; // has 1s where computer has a disc (yellow)
    private BitBoard mask; // has 1s where there is a disc
    private BitBoard playersPosition;
    private ArrayList<Node> children;   //retrieves the children of a specific node, i.e. a game state
    private int turn;
    private boolean gameIsOverInPosiiton;
    // private GameManager manager;

    public Node(BitBoard hamiDownPosition, BitBoard hamiDownMask, int turnNumba) {
        children = new ArrayList<Node>();
        turn = turnNumba;
        yPosition = hamiDownPosition;
        mask = hamiDownMask;
        decideLeafStatus();
    }

    public void decideLeafStatus(){
        if(turn % 2 == 0) {
            gameIsOverInPosiiton = yPosition.checkWin();
        } else {
            gameIsOverInPosiiton = yPosition.unMask(mask).checkWin();
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
     * @throws Exception /////// NOT WORKING YET, IMPLEMENT NEW ADD TO POSITION METHOD
     */
    public void addChildren() throws Exception {
        if(children.isEmpty()){ 
            for (int i = 0; i < Board.COLUMNS ; i++) {
                BitBoard newMask = mask.addBitPieceToMask(i);
                if(wasNewBoardCreated(newMask)){
                    Node node;
                    if(turn % 2 == 0) {
                        BitBoard newPosition = new BitBoard(yPosition.addBitToThisPosition(mask.bit, newMask.bit));
                        node = new Node(newPosition, newMask, turn + 1);
                    } else {
                        node = new Node(yPosition, newMask, turn + 1);
                    }
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

    public ArrayList<Node> getOrMakeChildren() throws Exception {
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


    public int evaluateNode() {
        int score = yPosition.checkTwo() - yPosition.unMask(mask).checkTwo();
        if(gameIsOverInPosiiton){
            if(turn % 2 == 0){
                score -= 100;
            } else {
                score += 100;
            }
        }
        return score;
    }


}
