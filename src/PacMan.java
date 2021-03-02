import java.util.ArrayList;

import edu.macalester.graphics.*;

public class PacMan {
    private final Tile[][] board = new Tile[ROWS][COLS];
    public static final int ROWS = 10;
    public static final int COLS = 10;
    public static final int TILE_SIDE_LENGTH = 40;
    public static final int CANVAS_WIDTH = 400;
    public static final int CANVAS_HEIGHT = 400;
    public static final int DOT_SIZE = 5;
    private Point playerStartingPoint;
    private ArrayList<Point> ghostStartingPoints = new ArrayList<>();
    private ArrayList<Ghost> ghosts = new ArrayList<>();
    private Player player;
    private CanvasWindow canvas;
    private int numPoints = 0;
    private int totalDots = 0;
    private int lives = 0;

    public PacMan() {
        canvas = new CanvasWindow("PacMan!", CANVAS_WIDTH, CANVAS_HEIGHT);
        resetGame();
        canvas.onKeyDown(event -> {
            switch (event.getKey().toString()) {
                case "LEFT_ARROW":
                    player.changeDirection(3, board);
                    break;
                case "RIGHT_ARROW":
                    player.changeDirection(1, board);
                    break;
                case "DOWN_ARROW":
                    player.changeDirection(2, board);
                    break;
                case "UP_ARROW":
                    player.changeDirection(0, board);
                    break;
            
                default:
                    break;
            }
        });
        startGame();
        canvas.animate(this::animate);
    }

    private void animate() {
        player.updatePos(board);
        checkEatenDot();
        for (Ghost ghost : ghosts) {
            ghost.updatePos(board);
        }
        checkLost();
        checkWon();
    }

    private void checkLost() {
        for (Ghost ghost : ghosts) {
            if (player.getCurrentTile(board) == ghost.getCurrentTile(board)) {
                loseLife();
            }
        }
    }

    private void checkWon() {
        if (numPoints == totalDots) {
            winGame();
        }
    }

    private void startGame() {

    }

    private void resetGame() {
        canvas.removeAll();
        makeBoard();
        makeGhosts();
        resetPlayer();
    }

    private void resetPlayer() {
        if (player != null) {
            canvas.remove(player);
        }
        player = new Player(playerStartingPoint.getX(), playerStartingPoint.getY(), 0);
        canvas.add(player);
        player.setCenter(playerStartingPoint);
    }

    private void winGame() {
        resetGame();
    }

    private void loseGame() {
        resetGame();
    }

    private void loseLife() {
        lives--;
        if (lives == 0) {
            loseGame();
        }
        resetPlayer();
    }

    private void makeBoard() {
        // 0 = dot, 1 = wall, 2 = ghost start, 3 = player start (turns into 0)
        int[][] boardDesign = {
            {0,0,0,0,0,0,0,1,1,0},
            {0,1,1,1,0,1,0,0,1,0},
            {0,1,0,0,0,1,1,0,1,0},
            {0,1,0,1,0,0,1,0,0,0},
            {0,1,0,1,2,2,1,0,1,1},
            {0,1,0,1,1,1,1,0,0,0},
            {0,1,0,0,0,1,0,0,1,0},
            {0,1,1,1,0,1,0,1,1,1},
            {0,1,0,0,0,1,0,1,0,1},
            {3,0,0,1,0,0,0,0,0,0}
        };
        int currentRow = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                Point point = new Point(i*TILE_SIDE_LENGTH + TILE_SIDE_LENGTH/2, j*TILE_SIDE_LENGTH + TILE_SIDE_LENGTH/2);
                int type = boardDesign[j][i];
                if (type == 3) {
                    playerStartingPoint = point;
                    type = 0;
                } else if (type == 2) {
                    ghostStartingPoints.add(point);
                }
                Tile tile = new Tile(type);
                board[currentRow][j] = tile;
                canvas.add(tile);
                tile.setCenter(point);
                if (tile.getType() == 0) {
                    totalDots++;
                }
            }
            currentRow++;
        }
    }

    private void makeGhosts() {
        // ghosts.add(new Ghost());
    }

    private void checkEatenDot() {
        Tile tile = player.getCurrentTile(board);
        if (tile.getHasDot()) {
            tile.removeDot();
        }
    }

    public static void main(String[] args) {
        PacMan game = new PacMan();
        game.startGame();
    }
}
