import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.GraphicsGroup;

public class GameManager {
    private static Board board;
    private CanvasWindow canvas;
    private final int CANVAS_WIDTH = 700;
    private final int CANVAS_HEIGHT = 600;
    private GraphicsGroup pieces;

    /**
     * Initializes the game and canvas. Each valid click determines whose turn it is. The player is even, and the
     * bot is odd.
     */
    public GameManager() {
        canvas = new CanvasWindow("Board", CANVAS_WIDTH, CANVAS_HEIGHT);
        pieces = new GraphicsGroup();
        board = new Board(null);
        canvas.add(pieces, 0, 0);
        board.initializePieces(canvas);

        canvas.onClick(event -> {
            if (!board.getIsGameOver()) {
                takeATurn(event.getPosition().getX(), event.getPosition().getY());
            } 
            else {
                clearBoard();
            }
        });
    }

    /**
     * Takes a turn for the human player. Passes in mouses X and Y coordinates for when it is the players turn.
     * @param mouseX
     * @param mouseY
     */
    private void takeATurn(double mouseX, double mouseY) {
        board.placePiece(mouseX, mouseY); 
        canvas.draw();
    }

    /**
     * Resets the game by clearing the canvas and reinitializing all the pieces.
     */
    private void clearBoard() {
        canvas.removeAll();
        canvas.add(pieces);
        board.initializePieces(canvas);
        board.resetGameTrackers();
    }

    public static void main(String[] args) {
        GameManager game = new GameManager();
    }
}
