public class PositionEvaluator {
    private Node root;
    private int searchDepth;
    private boolean playerTurn;

    public PositionEvaluator(Node n){
        root = n;
        searchDepth = 8;
        playerTurn=true;
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
        if(depth ==0 || current.getGameIsOverInPosition()){
            return current.evaluateNode();
        }
        if(playerTurn) {
            double maxEval = Double.NEGATIVE_INFINITY; 
            for(Node child: current.getOrMakeChildren()){
                // if(child != null){
                    current.score = evaluatePosition(child, depth-1, alpha, beta, false);
                    maxEval = Math.max(maxEval, current.score);
                    alpha = Math.max(alpha, current.score);
                    if(beta <= alpha)break;
                // }
            }
            return maxEval;
        } else {
            double minEval = Double.POSITIVE_INFINITY;
            for(Node child: current.getOrMakeChildren()){
                // if(child != null){
                    current.score = evaluatePosition(child, depth-1, alpha, beta, true);
                    minEval = Math.min(minEval, current.score);
                    beta = Math.min(beta, current.score);
                    if(beta <= alpha) break;
                // }
            }
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
        // Collections.sort(root.getChildren(), new ChildSorter());
        // Node returnNode=root.getChildren().get(0);
        // System.out.println("get children " + root.getChildren());

        for (Node child: root.getOrMakeChildren()) {
                if(child.getScore() <= eval) {
                    returnNode = child;
                    break;
                }
        }  
        // System.out.println(Long.toBinaryString(root.mask.bit));
        root = returnNode;
        return returnNode;
    }

    /**
     * Updates the tree so the root reflects the most recent move and boardstate.
     * @param index
     */
    public void updateTree(int index) {
        if(index !=-1){
            System.out.println("index: " + index);
            root = root.getOrMakeChildren().get(index);
            // System.out.println("Child size: " + root.getChildren().size());
            // currentTurn++;
        }
        
    }

    public static void main(String[] args) {
        GameManager game = new GameManager();
    }
}
