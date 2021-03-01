import java.util.ArrayList;

import edu.macalester.graphics.*;

public class PacMan {
    private final Tile[][] BOARD;
    private static final int MAX_DOTS;
    private static final int CANVAS_WIDTH;
    private static final int CANVAS_HEIGHT;
    private ArrayList<Ghost> ghosts;
    private Player player;
    private CanvasWindow canvas;
    private int points;

    public PacMan() {
        canvas = new CanvasWindow("PacMan!", CANVAS_WIDTH, CANVAS_HEIGHT);
        canvas.onKeyDown(event -> {
            switch (event.getKey()) {
                case value:
                    
                    break;
            
                default:
                    break;
            }
        });
        startGame();
        canvas.animate(this::animate);
    }

    private void animate() {}
    private void startGame() {}
}
