import java.util.Collections;

public class PositionEvaluator {
    public Node root;
    private int currentTurn; // maximizing player should be even turn count
    private int searchDepth;

    public PositionEvaluator(Node n){
        root = n;
        currentTurn = 0;
        searchDepth = 4;
    }

    /*
     * creates the tree, using alpha-beta pruning and minimax on the imaginary full game tree,
     * using a max depth of searchDepth 2** and alpha beta scores as doubles so we can use infinities
     */
    public double evaluatePosition(Node start, double alpha, double beta){
        Node current = start;
        double eval = -1.0;
        if( current.getTurn() - currentTurn >= searchDepth || current.getGameIsOverInPosition()){
            return current.evaluateNode();
        }
        if(current.getTurn() % 2 == 1) {
            double maxEval = Double.NEGATIVE_INFINITY; 
            for(Node child: current.getOrMakeChildren()){
                eval = evaluatePosition(child, alpha, beta);
                maxEval = Math.max(maxEval, eval);
                alpha = Math.max(alpha, eval);
                if(beta <= alpha)break;
            }
            return maxEval;
        } else {
            double minEval = Double.POSITIVE_INFINITY;
            for(Node child: current.getOrMakeChildren()){
                eval = evaluatePosition(child, alpha, beta);
                minEval = Math.min(minEval, eval);
                beta = Math.min(beta, eval);
                if(beta <= alpha) break;
            }
            return minEval; 
        }
    }
 
    /*
     * returs the highest score child node of the root, than updates the root to reflect the turn progression
     */
    public Node getNextAIMove(){
        int maxScore = Integer.MIN_VALUE;
        Node returnNode = null;
        evaluatePosition(root, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        for (Node child: root.getChildren()) {
            if(child != null){
                if(child.getScore() > maxScore) maxScore = child.getScore();
                returnNode = child;
            }
        }
        root = returnNode;
        return returnNode;
    }

    /*
     * updates the tree to reflect the players last move
     */
    public void updateTree(int index) {
        root = root.getChildren().get(index);
    }
}
