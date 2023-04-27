import java.awt.Color;
import java.util.ArrayList;

import edu.macalester.graphics.Fillable;

public class Node {
    public BitBoard yellowPos; // has 1s where computer has a disc (yellow)
    private BitBoard mask; // has 1s where there is a disc
    private BitBoard playersPosition;
    public ArrayList<Node> children;   //retrieves the children of a specific node, i.e. a game state
    private int turn;
    private boolean gameIsOverInPosiiton;
    private int score;
    // private GameManager manager;

    public Node(BitBoard hamiDownPosition, BitBoard hamiDownMask, int turnNumba) {
        children = new ArrayList<Node>();
        turn = turnNumba;
        yellowPos = hamiDownPosition;
        mask = hamiDownMask;
        decideLeafStatus();
    }

    public void decideLeafStatus(){
        if(turn % 2 == 0) {
            gameIsOverInPosiiton = yellowPos.checkWin();
        } else {
            gameIsOverInPosiiton = yellowPos.unMask(mask).checkWin();
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
    public void addChildren() {
        if(children.isEmpty()){ 
            for (int i = 0; i < Board.COLUMNS ; i++) {
                BitBoard newMask = mask.addBitPieceToMask(i);
                if(wasNewBoardCreated(newMask)){
                    Node node;
                    if(turn % 2 == 0) { // if this node is a red move/turn
                        BitBoard newPosition = new BitBoard(yellowPos.addBitToThisPosition(mask.bit, newMask.bit));
                        node = new Node(newPosition, newMask, turn + 1);
                    } else {
                        node = new Node(yellowPos, newMask, turn + 1);
                    }
                    children.add(node);
                } else {
                    System.out.println("addChildren error! (in Node)");
                    children.add(null);
                }
            }
        }
    }

    public ArrayList<Node> getChildren(){
        return children;
    }

    public int getScore(){
        return score;
    }

    public ArrayList<Node> getOrMakeChildren(){
        if(children.isEmpty()) {
            addChildren();
        }
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
        score = yellowPos.checkTwo() - yellowPos.unMask(mask).checkTwo();
        if(gameIsOverInPosiiton){
            System.out.println("Game's over ran in evaluate node");
            if(turn % 2 == 0){
                score -= 100;
            } else {
                score += 100;
            }
        }
        System.out.println("Node score= " + score);
        return score;
    }

    public boolean getGameIsOverInPosition() {
        return gameIsOverInPosiiton;
    }
}
