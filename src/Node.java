import java.util.ArrayList;

public class Node {
    private Board nodeGameboard;
    private BitBoard yellowPositions; // has 1s where computer has a disc (yellow)
    private BitBoard mask; // has 1s where there is a disc
    private ArrayList<Node> children;
    private int turn;
    private boolean gameIsOverInPosition;
    private double score;
    private int positionEvaluationScore;
    private static final int COLUMNS = 7;

    /**
     * Represents a gamestate of connect 4. Stores the game as two BitBoards as 
     * @param gameboard 
     * @param inputPosition
     * @param inputMask
     * @param turnNumber
     */
    public Node(Board gameboard, BitBoard inputPosition, BitBoard inputMask, int turnNumber) {
        nodeGameboard = gameboard;
        children = new ArrayList<Node>();
        turn = turnNumber;
        yellowPositions = inputPosition;
        mask = inputMask;
        decideLeafStatus();
        positionEvaluationScore = Integer.MIN_VALUE;
    }

    /**
     * Checks if at the node either the player or computer has won. 
     * If they have sets gameIsOverInPosition to True.
     */
    private void decideLeafStatus(){
        if(turn % 2 == 1) {
            gameIsOverInPosition = yellowPositions.checkWin();
        } else {
            gameIsOverInPosition = yellowPositions.unMask(mask).checkWin();
        }
    }

    /**
     * Checks if a new BitBoard was created.
     * @param newBoard
     * @return
     */
    private boolean wasNewBoardCreated(BitBoard newBoard){
        long updatedBoardBit = newBoard.getBit() & 0b0111111011111101111110111111011111101111110111111L;
        if(mask.getBit() - updatedBoardBit == 0) return false;
        return true;
    }

    /**
     * Adds all the children of a current boardstate. Doesn't make ALL possible children for the entire tree, for purposes of space. 
     * Once a column is full no longer creates a child in that column.
     * Will be used for analysis of which child is the best.
     */
    private void addChildren() {
        for (int i = 0; i < COLUMNS ; i++) {
            if(!nodeGameboard.isColumnFull(i)){
                BitBoard newMask = mask.addBitToMask(i);
                if(wasNewBoardCreated(newMask)){
                    Node node;
                    if(turn % 2 == 1) {
                        BitBoard newPosition = new BitBoard(yellowPositions.addBitToPos(mask.getBit(), newMask.getBit()));
                        node = new Node(nodeGameboard, newPosition, newMask, turn + 1);
                    } else {
                        node = new Node(nodeGameboard, yellowPositions, newMask, turn + 1);
                    }
                    children.add(node);
                }  
            }
        }
    }

    /**
     * Gets the current list of children. If there are no children to be gotten, 
     * it makes the children for the Node.
     * @return children
     */
    public ArrayList<Node> getOrMakeChildren(){
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
        this.score = scoreSetter;
    }

    /**
     * Evaluates the score of the node by comparing the # of check twos in the node and if there is a 
     * win for either player.
     * @return
     */
    public int evaluateNode() {
        positionEvaluationScore = yellowPositions.checkTwos() - yellowPositions.unMask(mask).checkTwos();
        if(gameIsOverInPosition){
            if(turn % 2 == 0){
                positionEvaluationScore -= 100; //red wins
            } else {
                positionEvaluationScore += 100 * turn; //yellow wins
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
     * Gets the BitBoard that represents yellows pieces.
     * @return
     */
    public BitBoard getYellowPosition(){
        return yellowPositions;
    }

    /**
     * Gets the BitBoard that represents the entire board state which is the mask.
     * @return
     */
    public BitBoard getMask(){
        return mask;
    }
    
}
