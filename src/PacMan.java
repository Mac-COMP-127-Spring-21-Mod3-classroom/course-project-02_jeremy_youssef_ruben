import java.util.ArrayList;

import edu.macalester.graphics.*;

public class PacMan {
    private final Tile[][] board = new Tile[ROWS][COLS];
    public static final int ROWS = 10;
    public static final int COLS = 10;
    public static final int TILE_SIDE_LENGTH = 40;
    public static final int CANVAS_WIDTH = 400;
    public static final int CANVAS_HEIGHT = 400;
    public static final int DOT_SIZE = 6;
    private Point playerStartingPoint;
    private ArrayList<Point> ghostStartingPoints;
    private ArrayList<Ghost> ghosts;
    private Player player;
    private CanvasWindow canvas;
    private int numPoints = 0;
    private int totalDots = 0;
    private int lives;

    public PacMan() {
        canvas = new CanvasWindow("PacMan!", CANVAS_WIDTH, CANVAS_HEIGHT);
        resetGame();
        canvas.onKeyDown(event -> {
            switch (event.getKey().toString()) {
                case "LEFT_ARROW":
                    player.changeDirection(3);
                    break;
                case "RIGHT_ARROW":
                    player.changeDirection(1);
                    break;
                case "DOWN_ARROW":
                    player.changeDirection(2);
                    break;
                case "UP_ARROW":
                    player.changeDirection(0);
                    break;
            
                default:
                    break;
            }
        });
        canvas.animate(this::animate);
    }

    private void animate() {
        player.updatePos();
        checkEatenDot();
        for (Ghost ghost : ghosts) {
            ghost.updatePos();
        }
        checkLost();
        checkWon();
    }

    private void checkLost() {
        // TODO: this crashes the game for some reason when the interaction happens
        for (Ghost ghost : ghosts) {
            if (Math.abs(player.getCenter().getX() - ghost.getCenter().getX()) < TILE_SIDE_LENGTH/2) {
                if (Math.abs(player.getCenter().getY() - ghost.getCenter().getY()) < TILE_SIDE_LENGTH/2) {
                    System.out.println("lost life");
                    System.out.println("player " + player.getCenter());
                    System.out.println("ghost " + ghost.getCenter());
                    loseLife();
                    break;
                }
            }
        }
    }

    private void checkWon() {
        if (numPoints == totalDots) {
            winGame();
        }
    }

    private void resetGame() {
        canvas.removeAll();
        numPoints = 0;
        lives = 3;
        makeBoard();
        ghosts = new ArrayList<>();
        player = null;
        resetGhosts();
        resetPlayer();
    }

    private void resetPlayer() {
        if (player != null) {
            canvas.remove(player);
        }
        player = new Player(playerStartingPoint, 0, board);
        canvas.add(player);
        double x = playerStartingPoint.getX();
        double y = playerStartingPoint.getY();
        player.setCenter(new Point(x,y));
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
        resetGhosts();
        resetPlayer();
    }

    private void makeBoard() {
        ghostStartingPoints = new ArrayList<>();
        // 0 = dot, 1 = wall, 2 = ghost start, 3 = player start (turns into 0)
        int[][] boardDesign = {
            {0,0,0,0,0,0,0,1,1,0},
            {0,1,1,1,0,1,0,0,1,0},
            {0,1,0,0,0,1,1,0,1,0},
            {0,1,0,1,0,0,1,0,0,0},
            {0,1,0,1,2,2,1,0,1,1},
            {0,1,0,1,1,1,1,0,0,0},
            {0,1,0,0,3,0,0,0,1,0},
            {0,1,1,1,0,1,0,1,1,1},
            {0,1,0,0,0,1,0,1,0,1},
            {0,0,0,1,0,0,0,0,0,0}
        };
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                Point point = new Point(i*TILE_SIDE_LENGTH + TILE_SIDE_LENGTH/2.0, j*TILE_SIDE_LENGTH + TILE_SIDE_LENGTH/2.0);
                int type = boardDesign[j][i];
                if (type == 3) {
                    playerStartingPoint = point;
                    type = 0;
                } else if (type == 2) {
                    ghostStartingPoints.add(point);
                }
                Tile tile = new Tile(type, point);
                board[i][j] = tile;
                canvas.add(tile);
                tile.setCenter(point);
                if (tile.getType() == 0) {
                    totalDots++;
                }
            }
        }
    }

    private void resetGhosts() {
        System.out.println("happned");
        for (Ghost ghost : ghosts) {
            canvas.remove(ghost);
        }
        ghosts = new ArrayList<>();
        for (Point point : ghostStartingPoints) {
            Ghost ghost = new Ghost(point, 0, board);
            ghosts.add(ghost);
            canvas.add(ghost);
            ghost.setCenter(point);
        }
        System.out.println("finished");
    }

    private void checkEatenDot() {
        Tile tile = player.getCurrentTile();
        if (tile.getHasDot()) {
            tile.removeDot();
            numPoints++;
        }
    }

    public static void main(String[] args) {
        new PacMan();
    }
}
