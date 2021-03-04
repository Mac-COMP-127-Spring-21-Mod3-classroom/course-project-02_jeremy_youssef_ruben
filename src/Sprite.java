import edu.macalester.graphics.*;

public class Sprite extends GraphicsGroup{
    //direction is the direction that the sprite is planned to move,
    private int direction;
    //while realDirection is the direction the sprite is actually going
    private int realDirection;
    private Tile[][] board;
    
    public Sprite(Point point, int initialDirection, Tile[][] board) {
        this.board = board;
        setCenter(point);
        changeDirection(direction);
    }

    /**
     * Sets the sprite to move one of 4 directions:
     * North (0), east (1), south (2), or west (3).
     * If there's an open space in that direction, the sprite will turn and return true.
     * If not, it won't turn.
     * @param board The 2d array that represents the level layout.
     */
    public void changeDirection(int direction) {
        if(direction < 0 || direction > 4){
            return;
        }
        this.direction = direction;
    }

    /**
     * Returns the tile that the sprite's center is within.
     * @param board The 2d array that represents the level layout.
     */
    public Tile getCurrentTile(){
        return board[wrapTileChecker(getTileXY()[0], true)][wrapTileChecker(getTileXY()[1], false)];
    }

    /**
     * Returns the tile xOffset to the left/right of the sprite and
     * yOffset to the top/bottom of the sprite.
     * @param board The 2d array that represents the level layout.
     * @param xOffset The number of tiles to the left (negative) or right (positive).
     * @param yOffset The number of tiles above (negative) or below (positive).
     */
    public Tile getNearbyTile(int xOffset, int yOffset){
        return board[wrapTileChecker(xOffset + getTileXY()[0], true)][wrapTileChecker(yOffset + getTileXY()[1], false)];
    }

    private int[] getTileXY() {
        int xPosition = (int) (10*(getCenter().getX() / (PacMan.ROWS * PacMan.TILE_SIDE_LENGTH)));
        int yPosition = (int) (10*(getCenter().getY() / (PacMan.COLS * PacMan.TILE_SIDE_LENGTH)));
        return new int[] {xPosition,yPosition};
    }

    private int wrapTileChecker(int position, boolean isX) {
        int toCheck = isX ? PacMan.COLS : PacMan.ROWS;
        if (position < 0) {
            return toCheck-1;
        } else if (position >= toCheck) {
            return 0;
        } else {
            return position;
        }
    }

    public boolean isInCenter(){
        double tileX = getCurrentTile().getTileCenter().getX();
        double tileY = getCurrentTile().getTileCenter().getY();
        // System.out.println("TILE: " + new Point(tileX, tileY));
        // System.out.println("TILE (center): " + getCurrentTile().getCenter());
        // System.out.println("PLAYER: " + getCenter());
        Point betterTileCenter = new Point(tileX, tileY);
        if(betterTileCenter.equals(getCenter())){
            return true;
        }
        return false;
        
    }

    private boolean isTurningAround(){
        if(realDirection == 0){
            if(direction == 2){
                return true;
            }
        }
        else if(realDirection == 1){
            if(direction == 3){
                return true;
            }
        }
        else if(realDirection == 2){
            if(direction == 0){
                return true;
            }
        }
        else if(realDirection == 3){
            if(direction == 1){
                return true;
            }
        }
        return false;
    }

    public void moveTo(Point toMove) {
        setCenter(toMove);
    }

    public boolean hitsWall(Tile[][]board, int direction){
        if (direction == 0) {
            if (getNearbyTile(0, -1).getType() == 0) {
                return false;
            }
        } else if (direction == 1) {
            if (getNearbyTile(1, 0).getType() == 0) {
                return false;
            }
        } else if (direction == 2) {
            if (getNearbyTile(0, 1).getType() == 0) {
                return false;
            }
        } else if (direction == 3) {
            if (getNearbyTile(-1, 0).getType() == 0) {
                return false;
            }
        }
        return true;
    }

    public void updatePos() {
        if(isInCenter() || isTurningAround()){
            if(!hitsWall(board, direction)){
                realDirection = direction;
            }
            if(hitsWall(board, realDirection)){
                return;
            }
        }
        if(realDirection == 0){
            Point newPos = new Point(getCenter().getX(), getCenter().getY() - 1);
            moveTo(newPos);
        }
        else if(realDirection == 1){
            Point newPos = new Point(getCenter().getX() + 1, getCenter().getY());
            moveTo(newPos);
        }
        else if(realDirection == 2){
            Point newPos = new Point(getCenter().getX(), getCenter().getY() + 1);
            moveTo(newPos);
        }
        else if(realDirection == 3){
            Point newPos = new Point(getCenter().getX() - 1, getCenter().getY());
            moveTo(newPos);
        }
        checkWrapAround();
    }

    private void checkWrapAround() {
        double x = getCenter().getX();
        double y = getCenter().getY();
        if (x < 0) {
            setCenter(PacMan.CANVAS_WIDTH, y);
        }
        if (y < 0) {
            setCenter(x, PacMan.CANVAS_HEIGHT);
        }
        if (x > PacMan.CANVAS_WIDTH) {
            setCenter(0, y);
        }
        if (y > PacMan.CANVAS_HEIGHT) {
            setCenter(x, 0);
        }
    }
}
