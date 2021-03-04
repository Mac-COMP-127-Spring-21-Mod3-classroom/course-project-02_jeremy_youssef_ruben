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

    //TODO: Check to make sure this does what I think it does (checks what tile is on the other
    //side) before writing incorrect Javadoc
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

    /**
     * Returns true if this sprite is directly in the center of whatever tile it's in,
     * returns false otherwise.
     */
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

    /**
     * Returns true if the sprite is planning to turn in the opposite direction
     * from what it's facing. (Left to right, right to left, up to down, or down to up.)
     * Returns false otherwise.
     */
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

    /**
     * Moves the sprite's center to the given location.
     */
    public void moveTo(Point toMove) {
        setCenter(toMove);
    }

    /**
     * Determines if there's a wall in front of the sprite.
     * @param board The array that contains the level layout.
     * @param direction The direction that the sprite is moving.
     * @return False if there's an open tile in front of the sprite, true if there's
     * anything else.
     */
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

    /**
     * Changes the direction that the sprite is facing to match the actual direction
     * that it's moving. Note that this is a graphical change only.
     */
    public void turnSprite(){
        if(realDirection == 0){
            setRotation(-90);
        }
        else if(realDirection == 1){
            setRotation(0);
        }
        else if(realDirection == 2){
            setRotation(90);
        }
        else if(realDirection == 3){
            setRotation(-180);
        }
    }

    /**
     * Moves the sprite forward by one pixel in whatever direction it's facing, turning
     * the sprite if it's able to and wants to.
     */
    public void updatePos() {
        if(isInCenter() || isTurningAround()){
            if(!hitsWall(board, direction)){
                realDirection = direction;
                turnSprite();
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

    /**
     * Checks whether the sprite is out of bounds, and moves it to the other side of
     * the canvas if it is.
     */
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

    /**
     * Returns the actual direction that the sprite is moving, NOT the
     * direction that it's queued to turn.
     */
    public int getRealDirection() {
        return realDirection;
    }
}
