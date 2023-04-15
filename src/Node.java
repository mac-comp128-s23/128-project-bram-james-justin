import java.util.ArrayList;

import edu.macalester.graphics.Fillable;

public class Node {
    private Board board;
    private GameManager manager;
    private ArrayList<Node> children;   //retrieves the children of a specific node, i.e. a game state
    private Fillable [][] boardState;
    public Node(Fillable [][] game) {
        boardState = game;
        children = new ArrayList<>();
    }



    public void addNodeChild(Node child) {
        children.add(child);
    }

    public ArrayList<Node> getChildren() {
        return children;
    }


}
