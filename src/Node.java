import java.awt.Color;
import java.util.ArrayList;

import edu.macalester.graphics.Fillable;

public class Node {
    public BitBoard yellowPos; // has 1s where computer has a disc (yellow)
    public BitBoard mask; // has 1s where there is a disc
    private BitBoard playersPosition;
    public ArrayList<Node> children;   //retrieves the children of a specific node, i.e. a game state
    public int turn;
    public boolean gameIsOverInPosiiton;
    public double score;
    public int positionEvaluationScore;
    // private GameManager manager;

    public Node(BitBoard hamiDownPosition, BitBoard hamiDownMask, int turnNumba) {
        children = new ArrayList<Node>();
        turn = turnNumba;
        yellowPos = hamiDownPosition;
        mask = hamiDownMask;
        decideLeafStatus();
        positionEvaluationScore = Integer.MIN_VALUE;
    }

    public void decideLeafStatus(){
        if(turn % 2 == 1) {
            gameIsOverInPosiiton = yellowPos.checkWin();
            System.out.println("yellow position.checkwin" + yellowPos.checkWin());
        } else {
            gameIsOverInPosiiton = yellowPos.unMask(mask).checkWin();
            System.out.println("red position check win " +yellowPos.unMask(mask).checkWin());
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
        System.out.println("gameover:" + gameIsOverInPosiiton);
        // if(!gameIsOverInPosiiton){ 
            for (int i = 0; i < Board.COLUMNS ; i++) {
                BitBoard newMask = mask.addBitPieceToMask(i);
                if(wasNewBoardCreated(newMask)){
                    Node node;
                    if(turn % 2 == 1) { // if this node is a red move/turn
                        BitBoard newPosition = new BitBoard(yellowPos.addBitToThisPosition(mask.bit, newMask.bit));
                        node = new Node(newPosition, newMask, turn + 1);
                    } else {
                        node = new Node(yellowPos, newMask, turn + 1);
                    }
                    children.add(node);
                } 
            }
        }
    // }

    public ArrayList<Node> getChildren(){
        if (children.isEmpty()) {
            addChildren();
        }
        return children;
    }

    public double getScore(){
        return score;
    }

    public boolean hasKids(){
        if(children.isEmpty()){
            return false;
        } else {
            return true;
        }
    }


    public int evaluateNode() {
        positionEvaluationScore = yellowPos.checkTwo() - yellowPos.unMask(mask).checkTwo();
        if(gameIsOverInPosiiton){
            if(turn % 2 == 0){
                positionEvaluationScore -= 100;
            } else {
                positionEvaluationScore += 100;
            }
        }
        return positionEvaluationScore;
    }

    public boolean getGameIsOverInPosition() {
        return gameIsOverInPosiiton;
    }
}
