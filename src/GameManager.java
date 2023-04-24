import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.GraphicsGroup;

public class GameManager {
    private Board board;
    private BitBoard bitboard;
    private Node gameState;
    private CanvasWindow canvas;
    private final int CANVAS_WIDTH = 700;
    private final int CANVAS_HEIGHT = 600;
    private GraphicsGroup pieces;

    public GameManager() {
        canvas = new CanvasWindow("Board", CANVAS_WIDTH, CANVAS_HEIGHT);
        pieces = new GraphicsGroup();
        board = new Board(null);
        canvas.add(pieces, 0, 0);
        board.initializePieces(canvas);

        canvas.onClick(event -> {
            if (!board.getGameIsOverInPosition()) {
                try {
                    takeATurn(event.getPosition().getX(), event.getPosition().getY());
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {
                clearBoard();
            }
        });

        /// FOR TESTING!!!
        canvas.onKeyDown(event -> {
            
        });
    }

    public void takeATurn(double x, double y) throws Exception {
        board.playerPlacePiece(x, y); 
        canvas.draw();
    }

    public void clearBoard() {
        canvas.removeAll();
        canvas.add(pieces);
        board.initializePieces(canvas);
        board.resetGameTrackers();
    }

    public static void main(String[] args) {
        GameManager game = new GameManager();
        BitBoard board = new BitBoard(0b0000000000000000000000000000000000000000000000000);

    }
}
