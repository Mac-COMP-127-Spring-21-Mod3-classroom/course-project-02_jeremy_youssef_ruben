import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.awt.Color;

import edu.macalester.graphics.*;

//Credit for how Pacman works: https://www.gamasutra.com/view/feature/3938/the_pacman_dossier.php?print=1

public class PacMan {
    private final Tile[][] board = new Tile[ROWS][COLS];
    // public static final int ROWS = 10;
    // public static final int COLS = 11;
    public static final int ROWS = 31;
    public static final int COLS = 36;
    // public static final int TILE_SIDE_LENGTH = 40;
    public static final int TILE_SIDE_LENGTH = 20;
    // public static final int CANVAS_WIDTH = 440;
    // public static final int CANVAS_HEIGHT = 400;
    public static final int CANVAS_WIDTH = 720;
    public static final int CANVAS_HEIGHT = 620;
    public static final int CANVAS_Y_OFFSET = 50;
    public static final int DOT_SIZE = 3;
    private Point playerStartingPoint;
    private ArrayList<Point> ghostStartingPoints;
    private ArrayList<Ghost> ghosts;
    private List<Image> ghostPNG;
    private final Image redGhost = new Image("red_ghost.png");
    private final Image yellowGhost = new Image("yellow_ghost.png");
    private final Image purpleGhost = new Image("purple_ghost.png");
    private final Image greyGhost = new Image("grey_ghost.png");
    private Random rand = new Random();
    private Player player;
    private CanvasWindow canvas;
    private int numPoints = 0;
    private int totalDots = 0;
    private int lives;
    private boolean ateDot;
    private GraphicsText livesText;
    private GraphicsText pointsText;

    /**
     * the main pacman game class
     */
    public PacMan() {
        canvas = new CanvasWindow("PacMan!", CANVAS_WIDTH, CANVAS_HEIGHT + CANVAS_Y_OFFSET);
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

    /**
     * the main animation loop for the game
     */
    private void animate() {
        if(ateDot){
            ateDot = false;
        }
        else{
            player.updatePos();
            checkEatenDot();
        }
        for (Ghost ghost : ghosts) {
            ghost.setCurrentPlayerNode(player.getNearbyTile(0, 0));
            ghost.setTargetPlayerDistance(COLS-((((totalDots*COLS)/totalDots)*numPoints + 1)/totalDots));
            ghost.updatePos();
        }
        checkLost();
        checkWon();
        updateText();
    }

    private void updateText() {
        pointsText.setText("Points: " + numPoints*100);
        livesText.setText("Lives: " + lives);
        pointsText.setCenter(pointsText.getWidth(), CANVAS_HEIGHT + CANVAS_Y_OFFSET/2);
        livesText.setCenter(CANVAS_WIDTH - livesText.getWidth(), CANVAS_HEIGHT + CANVAS_Y_OFFSET/2);
    }

    private void resetTextDisplays() {
        pointsText = new GraphicsText();
        livesText = new GraphicsText();
        pointsText.setFontSize(30);
        livesText.setFontSize(30);
        pointsText.setFillColor(Color.BLUE);
        livesText.setFillColor(Color.BLUE);
        Rectangle filler = new Rectangle(0,CANVAS_HEIGHT,CANVAS_WIDTH,CANVAS_Y_OFFSET);
        filler.setFillColor(Color.BLACK);
        canvas.add(filler);
        canvas.add(livesText);
        canvas.add(pointsText);
    }

    private void checkLost() {
        for (Ghost ghost : ghosts) {
            if (Math.abs(player.getCenter().getX() - ghost.getCenter().getX()) < TILE_SIDE_LENGTH/2) {
                if (Math.abs(player.getCenter().getY() - ghost.getCenter().getY()) < TILE_SIDE_LENGTH/2) {
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
        ateDot = false;
        makeBoard();
        ghosts = new ArrayList<>();
        player = null;
        resetGhosts();
        resetPlayer();
        resetTextDisplays();
    }

    private void resetPlayer() {
        if (player != null) {
            canvas.remove(player);
        }
        player = new Player(playerStartingPoint, 0, 2, board);
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

    /**
     * creates the board of tiles given by the boardDesign variable in this function
     */
    private void makeBoard() {
        ghostStartingPoints = new ArrayList<>();
        // 0 = dot, 1 = wall, 2 = ghost start, 3 = player start (turns into 0), 4 = void
        int[][] boardDesign = {
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1},
            {1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1},
            {1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1},
            {1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1},
            {4, 4, 4, 4, 4, 4, 1, 1, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 1, 4, 4, 4, 4, 4, 4, 4},
            {1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 4, 4, 4, 4, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 4, 4, 4, 4, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 2, 2, 2, 2, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1},
            {4, 4, 4, 4, 4, 4, 1, 1, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 1, 4, 4, 4, 4, 4, 4, 4},
            {1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1},
            {1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1},
            {1, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 1},
            {1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1},
            {1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1},
            {1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}

            // {0,0,0,0,0,0,0,1,1,0,0},
            // {0,1,1,1,0,1,0,0,1,0,0},
            // {0,1,0,0,0,1,1,0,1,0,0},
            // {0,1,0,1,0,0,1,0,0,0,0},
            // {0,1,0,1,2,0,1,0,1,1,0},
            // {0,1,0,1,1,1,1,0,0,0,0},
            // {0,1,0,0,3,0,0,0,1,0,0},
            // {0,1,1,1,0,1,0,1,1,1,0},
            // {0,1,0,0,0,1,0,1,0,1,0},
            // {0,0,0,1,0,0,0,0,0,0,0}
        };
        for (int i = 0; i < COLS; i++) {
            for (int j = 0; j < ROWS; j++) {
                Point point = new Point(i*TILE_SIDE_LENGTH + TILE_SIDE_LENGTH/2.0, j*TILE_SIDE_LENGTH + TILE_SIDE_LENGTH/2.0);
                int type = boardDesign[j][i];
                if (type == 3) {
                    playerStartingPoint = point;
                    type = 0;
                } else if (type == 2) {
                    ghostStartingPoints.add(point);
                }
                Tile tile = new Tile(type, point);
                board[j][i] = tile;
                canvas.add(tile);
                tile.setCenter(point);
                if (tile.getType() == 0) {
                    totalDots++;
                }
            }
        }
    }

    private void resetGhosts() {
        for (Ghost ghost : ghosts) {
            canvas.remove(ghost);
        }
        ghosts = new ArrayList<>();
        ghostPNG = new ArrayList<>();
        ghostPNG.add(redGhost);
        ghostPNG.add(yellowGhost);
        ghostPNG.add(purpleGhost);
        ghostPNG.add(greyGhost);
        for (Point point : ghostStartingPoints) {
            Integer index = rand.nextInt(ghostPNG.size());
            Ghost ghost = new Ghost(point, 0, 2, board, ghostPNG.get(index));
            ghosts.add(ghost);
            canvas.add(ghost);
            ghost.setCenter(point);
            ghostPNG.remove(ghostPNG.get(index));
        }
    }

    /**
     * checks if the player has a dot to eat in the tile it is in, removes the dot and adds a point if it does
     */
    private void checkEatenDot() {
        Tile tile = player.getNearbyTile(0,0);
        if (tile.getHasDot()) {
            tile.removeDot();
            ateDot = true;
            numPoints++;
        }
    }

    public static void main(String[] args) {
        new PacMan();
    }
}
