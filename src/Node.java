import java.util.ArrayList;

public class Node {
    private Board board;
    private GameManager manager;
    private Integer avgRedOtucome;
    private ArrayList<Node> children;   //retrieves the children of a specific node, i.e. a game state
    
    public Node(Board gameBoard) {
        board = gameBoard;
        children = new ArrayList<>();
    }

    public Board currentBoardState() {
        return board;
    }

    public void addNodeChild(Node child) {
        children.add(child);
    }
}
