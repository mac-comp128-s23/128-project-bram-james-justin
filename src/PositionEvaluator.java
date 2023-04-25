import java.util.Collections;

public class PositionEvaluator {
    private Node root;
    private int currentTurn; // maximizing player should be even turn count
    private int searchDepth;

    public PositionEvaluator(Node n){
        root = n;
        currentTurn = 0;
        searchDepth = 2;  
    }

    public Node makePlayerMove(int col) throws Exception{
        root = root.getOrMakeChildren().get(col);
        return root;
    }

    public Node makeAIMove() throws Exception{
        Node bestNode = null;
        evaluatePosition(root, 0, 0);
        // Collections.sort(root.getChildren(), null);
        int max = Integer.MIN_VALUE;
        for (Node child : root.getChildren()) {
            if(child.getScore() > max) {      //// if returns null check here
                max = child.getScore();
                bestNode = child;
            }
        }
        root = bestNode;
        return bestNode;
    }

    /*
     * creates the tree, using alpha-beta pruning and minimax on the imaginary full game tree,
     * using a max depth of searchDepth 2** and alpha beta scores as doubles so we can use infinities
     */
    public double evaluatePosition(Node start, double alpha, double beta) throws Exception{
        Node current = start;
        double eval = -1.0;
        if(currentTurn - current.getTurn() <= searchDepth || current.getGameIsOverInPosition()){
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
 
    public Node getNextMove(){
        return root;
    }

}
