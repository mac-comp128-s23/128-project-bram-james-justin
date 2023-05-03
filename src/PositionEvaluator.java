public class PositionEvaluator {
    private Node root;
    private int searchDepth;
    private boolean playerTurn;

    public PositionEvaluator(Node n){
        root = n;
        searchDepth = 5;
        playerTurn = true;
    }

    /**
     * Evaluates the position using the minimax algorithm on a game tree of specified depth. 
     * Uses Alpha Beta pruning to reduce time spent traversing tree.
     * 
     * @param start
     * @param depth
     * @param alpha
     * @param beta
     * @param playerTurn
     * @return
     */
    public double evaluatePosition(Node start, int depth, double alpha, double beta, boolean playerTurn){
        Node current = start;
        if(depth == 0 || current.getGameIsOverInPosition()){
            int score = current.evaluateNode();
            current.setScore(score);
            return score;
        }
        
        if(playerTurn) {
            double maxEval = Double.NEGATIVE_INFINITY; 
            for(Node child : current.getOrMakeChildren()){
                double score = evaluatePosition(child, depth-1, alpha, beta, false);
                
                maxEval = Math.max(maxEval, score);
                alpha = Math.max(alpha, maxEval);
                if (beta <= alpha) break;
            }
            current.setScore(maxEval);
            return maxEval;

        } else {
            double minEval = Double.POSITIVE_INFINITY;
            for(Node child : current.getOrMakeChildren()){
                double score = evaluatePosition(child, depth-1, alpha, beta, true);
                minEval = Math.min(minEval, score);
                beta = Math.min(beta, minEval);
                if (beta <= alpha) break;
            }
            current.setScore(minEval);
            return minEval; 
        }
    }
 

    /**
     * Returns the highest score child node of the root, than updates the root to reflect the turn progression.
     * @return
     */
    public Node getNextAIMove(){
        Node returnNode = null;
        double eval=  evaluatePosition(root, searchDepth, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, playerTurn);
        for (Node child: root.getOrMakeChildren()) {
            if(child.getScore() == eval) {
                returnNode = child;
                break;
            }
        }  
        return returnNode;
    }

    /**
     * Updates the tree so the root reflects the most recent move and boardstate.
     * @param index
     */
    public void updateTree(int index) {
        if(index !=-1){
            for (Node child : root.getOrMakeChildren()) {
                if(index == child.getMask().getColumnUsingNewMask(root.getMask().getBit())){
                    root = child;
                    break;
                }
            }            
        }
    }
}
