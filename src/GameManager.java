import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.GraphicsGroup;

public class GameManager {
    private static Board board;
    private CanvasWindow canvas;
    private final int CANVAS_WIDTH = 700;
    private final int CANVAS_HEIGHT = 600;
    private GraphicsGroup pieces;
    private boolean programsRunning;

    public GameManager() {
        canvas = new CanvasWindow("Board", CANVAS_WIDTH, CANVAS_HEIGHT);
        pieces = new GraphicsGroup();
        board = new Board(null);
        canvas.add(pieces, 0, 0);
        board.initializePieces(canvas);
        programsRunning = false;

        canvas.onClick(event -> {
            programsRunning = true;
            if (!board.getGameIsOverInPosition()) {
                takeATurn(event.getPosition().getX(), event.getPosition().getY());
            } 
            else {
                clearBoard();
            }
            programsRunning = false;
        });

        /// FOR TESTING!!!
        // canvas.onKeyDown(event -> {
        //     if(event.getKey() == Key.RETURN_OR_ENTER)
        //     board.TESTTHENODES();
        // });
    }

    /**
     * Takes a turn. Passes in mouses X and Y coordinates for when it is the players turn.
     * @param mouseX
     * @param mouseY
     */
    public void takeATurn(double mouseX, double mouseY) {
        board.playerPlacePiece(mouseX, mouseY); 
        canvas.draw();
    }

    /**
     * Resets the game.
     */
    public void clearBoard() {
        canvas.removeAll();
        canvas.add(pieces);
        board.initializePieces(canvas);
        board.resetGameTrackers();
    }

    public static void main(String[] args) {
        // GameManager game = new GameManager();
        BitBoard bitBoard = new BitBoard(0b0000000000000000000000000000000000000000000000000);
        new Node(board, new BitBoard(0b0000000000000000000000000000000000000000000000000), bitBoard, 1).evaluateNode();
    }
}
