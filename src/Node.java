import java.util.ArrayList;

public class Node {
    private Board nodeGameboard;
    private BitBoard yellowPos; // has 1s where computer has a disc (yellow)
    private BitBoard mask; // has 1s where there is a disc
    private ArrayList<Node> children;   //retrieves the children of a specific node, i.e. a game state
    private int turn;
    private boolean gameIsOverInPosition;
    private double score;
    private int positionEvaluationScore;
    private static final int COLUMNS = 7;
    private ArrayList<Integer> gameHistory;
    // private GameManager manager;

    public Node(Board gameboard, BitBoard hamiDownPosition, BitBoard hamiDownMask, int turnNumber) {
        nodeGameboard = gameboard;
        children = new ArrayList<Node>();
        turn = turnNumber;
        yellowPos = hamiDownPosition;
        mask = hamiDownMask;
        decideLeafStatus();
        positionEvaluationScore = Integer.MIN_VALUE;
        gameHistory = new ArrayList<>();
    }

public ArrayList<Integer> getHistory(){
    return gameHistory;
}

public void setHistory(ArrayList<Integer> parentHistory){
    gameHistory = parentHistory;
}

    /**
     * Checks if at the node either the player or computer has won. 
     * If they have sets gameIsOverInPosition to True.
     */
    public void decideLeafStatus(){ //Could we merge this with getGameIsOverInPosition?
        if(turn % 2 == 1) {
            gameIsOverInPosition = yellowPos.checkWin();
            // System.out.println("yellow position.checkwin" + yellowPos.checkWin());
        } else {
            gameIsOverInPosition = yellowPos.unMask(mask).checkWin();
            // System.out.println("red position check win " +yellowPos.unMask(mask).checkWin());
        }
    }

    /**
     * Checks if a new bitboard was created.
     * @param newBoard
     * @return
     */
    public boolean wasNewBoardCreated(BitBoard newBoard){
        
        if(newBoard.getBit() - mask.getBit() > 0) return true;
        return false;
    }

    /**
     * Adds all the children of a current boardstate. Doesn't make ALL possible children for the entire tree, for purposes of space. 
     * Once a column is full no longer creates a child in that column.
     * Will be used for analysis of which child is the best.
     */
    public void addChildren() {
        // System.out.println("gameover:" + gameIsOverInPosiiton);
        // if(!gameIsOverInPosiiton){ 
            for (int i = 0; i < COLUMNS ; i++) {
                // System.out.println("column "+ i);
                // System.out.println("column full check" +nodeGameboard.isColumnFull(i));
                if(!nodeGameboard.isColumnFull(i)){ // this doens't do a lot because nodes are created before column is full
                    BitBoard newMask = mask.addBitPieceToMask(i);
                    // System.out.println("newMask" + Long.toBinaryString(newMask.getBit()));
                    if(wasNewBoardCreated(newMask)){
                        Node node;
                        if(turn % 2 == 1) { // if this node is a red move/turn
                            BitBoard newPosition = new BitBoard(yellowPos.addBitToThisPosition(mask.getBit(), newMask.getBit()));
                            node = new Node(nodeGameboard, newPosition, newMask, turn + 1);
                        } else {
                            node = new Node(nodeGameboard, yellowPos, newMask, turn + 1);
                        }
                        children.add(node);
                        // System.out.println("added child" + Long.toBinaryString(children.get(i).yellowPos.getBit()));
                    }  
                }
            }
        }
    // }
    /**
     * Gets the current list of children. If there are no children to be gotten, 
     * it makes the children for the Node.
     * @return
     */
    public ArrayList<Node> getOrMakeChildren(){
        // System.out.println("children" + children.size());
        if (children.isEmpty()) {
            addChildren();
        }
        return children;
    }
    
    /**
     * Gets the score of a Node
     * @return
     */
    public double getScore(){
        return score;
    }

    public void setScore(double scoreSetter){
        score=scoreSetter;
    }


    /**
     * Evaluates the score of the node by comparing the # of check twos in the node and if there is a 
     * win for either player.
     * @return
     */
    public int evaluateNode() {
        positionEvaluationScore = yellowPos.checkNumberOfTwos() - yellowPos.unMask(mask).checkNumberOfTwos();
        if(gameIsOverInPosition){
            if(turn % 2 == 0){
                positionEvaluationScore -= 100; //red wins
            } else {
                positionEvaluationScore += 100; //yellow wins
            }
        }
        return positionEvaluationScore;
    }

    /**
     * Gets if the game is over in the Node. In other words checks if the node is a leaf.
     * @return
     */
    public boolean getGameIsOverInPosition() {
        return gameIsOverInPosition;
    }
    /**
     * Gets the bitboard that represents yellows pieces.
     * @return
     */
    public BitBoard getYellowPosition(){
        return yellowPos;
    }
    /**
     * Gets the bitboard that represents the entire board state which is the mask.
     * @return
     */
    public BitBoard getMask(){
        return mask;
    }
    
}
