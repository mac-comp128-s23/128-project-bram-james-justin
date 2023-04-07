import java.lang.reflect.Array;
import java.util.ArrayList;

import edu.macalester.graphics.GraphicsObject;

public class Board {
    public int[][] gameBoard;

    public Board() {
        gameBoard = new int[7][6];
        initializeBoard();
    }

    public void initializeBoard(){
        for (int[] col : gameBoard) {
            for (int i : col) {
                col[i] = 0;
            }
        }
    }

    public int[][] getGameBoard() {
        return gameBoard;
    }

    public static void main(String[] args) {
        
    }
}

