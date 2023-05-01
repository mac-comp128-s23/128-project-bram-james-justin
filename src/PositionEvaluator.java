import java.util.Collections;

public class PositionEvaluator {
    public Node root;
   // maximizing player should be even turn count
    private int searchDepth;
    private boolean playerTurn;

    public PositionEvaluator(Node n){
        root = n;
        searchDepth = 8;
        playerTurn=true;
    }

    /*
     * creates the tree, using alpha-beta pruning and minimax on the imaginary full game tree,
     * using a max depth of searchDepth 2** and alpha beta scores as doubles so we can use infinities
     */
    public double evaluatePosition(Node start, int depth, double alpha, double beta, boolean playerTurn){
        System.out.println("-----------------------------------------------------------");
        Node current = start;
        if(depth ==0 || current.getGameIsOverInPosition()){
            return current.evaluateNode();
        }
        if(playerTurn) {
            double maxEval = Double.NEGATIVE_INFINITY; 
            for(Node child: current.getChildren()){
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
            for(Node child: current.getChildren()){
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
 
    /*
     * returs the highest score child node of the root, than updates the root to reflect the turn progression
     */
    public Node getNextAIMove(){
        Node returnNode = null;
        double eval=  evaluatePosition(root, searchDepth, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, playerTurn);
        // Collections.sort(root.getChildren(), new ChildSorter());
        // Node returnNode=root.getChildren().get(0);
        System.out.println("get children " + root.getChildren());

        for (Node child: root.getChildren()) {
            System.out.println("Child score:" + child.getScore());
            System.out.println("eval "+ eval);
            System.out.println(Long.toBinaryString(child.mask.bit));
        
                if(child.getScore() <= eval) {
                    returnNode = child;
                    break;
                }
        }  
        root = returnNode;
        return returnNode;
    }

    /*
     * updates the tree to reflect the players last move
     */
    public void updateTree(int index) {
        if(index !=-1){
            System.out.println("index: " + index);
            root = root.getChildren().get(index);
            // System.out.println("Child size: " + root.getChildren().size());
            // currentTurn++;
        }
        
    }

    // public boolean getPlayer(){
    //     boolean yellow=current.getTurn() % 2 == 1;
    // }

    public static void main(String[] args) {
        GameManager game = new GameManager();

    }
}
