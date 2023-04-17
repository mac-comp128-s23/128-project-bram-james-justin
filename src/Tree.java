public class Tree {
    private Node root;
    private int currentTurn;

    public Tree(Node n){
        root = n;
        currentTurn = 0;


    }

    public void factor(){
        
    }

    public Node getMove(){
        return root;
    }
    
    // public double minimax(Node node, Integer depth, Integer alpha, Integer beta, GameManager manager){
    //     if(manager.getGameOver()==true){
    //         break; 
    //     }
    // }
}
