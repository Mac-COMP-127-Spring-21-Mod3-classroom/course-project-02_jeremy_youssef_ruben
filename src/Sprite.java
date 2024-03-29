import edu.macalester.graphics.*;

/**
 * Handles shared logic for ghosts and PacMan.
 * 
 * @author Jeremy Hubinger, Ruben Escobar, Youssef Aithmad
 */
public class Sprite extends GraphicsGroup{
    private int direction;
    //direction is the direction that the sprite is planned to move,
    private int realDirection;
    //while realDirection is the direction the sprite is actually going
    private int speed;
    private Tile[][] board;
    
    /**
     * This is the super class of which ghost and player are both made from
     */
    public Sprite(Point center, int initialDirection, int speed, Tile[][] board) {
        this.board = board;
        setCenter(center);
        changeDirection(direction);
        this.speed = speed;
    }

    /**
     * Sets the sprite to move one of 4 directions:
     * North (0), east (1), south (2), or west (3).
     * @param direction The direction that the sprite will turn at its next opportunity.
     */
    public void changeDirection(int direction) {
        if(direction < 0 || direction > 4){
            return;
        }
        this.direction = direction;
    }

    /**
     * @return the tile xOffset to the left/right of the sprite and
     * yOffset to the top/bottom of the sprite.
     * @param xOffset The number of tiles to the left (negative) or right (positive).
     * @param yOffset The number of tiles above (negative) or below (positive).
     */
    public Tile getNearbyTile(int xOffset, int yOffset){
        return board[wrapTileChecker(yOffset + getTileXY(this)[1], false)][wrapTileChecker(xOffset + getTileXY(this)[0], true)];
    }

    /**
     * Gets the x and y position in the 2d array of tiles that makes up board.
     */
    public int[] getTileXY(GraphicsObject object) {
        int xPosition = (int) (PacMan.COLS*(object.getCenter().getX() / (PacMan.COLS * PacMan.TILE_SIDE_LENGTH)));
        int yPosition = (int) (PacMan.ROWS*(object.getCenter().getY() / (PacMan.ROWS * PacMan.TILE_SIDE_LENGTH)));
        return new int[] {xPosition,yPosition};
    }

    /**
     * Tells whether the player is on the edge of the board and needs to wrap around it.
     * @return what tile position to look at (adjusted so that -1 turns into the max, and anything over the max turns into 0)
     * @param position what position we want to look at
     * @param isX are we looking at the rows? if false, then look at columns
     */
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
     * @return returns true if this sprite is directly in the center of whatever tile it's in, returns false otherwise.
     */
    public boolean isInCenter(){
        double tileX = getNearbyTile(0,0).getTileCenter().getX();
        double tileY = getNearbyTile(0,0).getTileCenter().getY();
        Point betterTileCenter = new Point(tileX, tileY);
        if(betterTileCenter.equals(getCenter())){
            return true;
        }
        return false;
        
    }

    /**
     * @return returns true if the sprite is planning to turn in the opposite direction from what it's facing. Returns false otherwise.
     * (Left to right, right to left, up to down, or down to up.)
     */
    public boolean isTurningAround(){
        int[] directions = {2,3,0,1};
        if (direction == directions[realDirection]) {
            return true;
        }
        return false;
    }

    /**
     * Determines if there's a wall in front of the sprite.
     * @param direction The direction that the sprite is moving or trying to move.
     * @return False if there's an open tile in front of the sprite, true if there's anything else.
     */
    public boolean hitsWall(int direction){
        // North (0), east (1), south (2), or west (3).
        int[][] nearbyByDirections = {
            {0, -1},
            {1, 0},
            {0, 1},
            {-1, 0}
        };
        int[] nearby = nearbyByDirections[direction];
        if (getNearbyTile(nearby[0], nearby[1]).getType() == 1) {
            return true;
        }
        return false;
    }

    /**
     * Changes the direction that the sprite is facing to match the actual direction
     * that it's moving. Note that this is a graphical change only.
     */
    public void turnSprite(){
        int[] rotationsPlayer = {-90, 0, 90, -180};
        if (this instanceof Player){
            setRotation(rotationsPlayer[realDirection]);
        }
        if (this instanceof Ghost){
            if (realDirection == 3){
                this.setScale(-1, 1);
            }
            else {
                this.setScale(1);
            }
        }
    }

    /**
     * Moves the sprite forward by one pixel in whatever direction it's facing, turning
     * the sprite if it's able to and wants to.
     */
    public void updatePos() {
        if(isInCenter() || isTurningAround()){
            if(!hitsWall(direction)){
                realDirection = direction;
                turnSprite();
            }
            if(hitsWall(realDirection)){
                return;
            }
        }
        Point[] points = {
            new Point(getCenter().getX(), getCenter().getY() - speed),
            new Point(getCenter().getX() + speed, getCenter().getY()),
            new Point(getCenter().getX(), getCenter().getY() + speed),
            new Point(getCenter().getX() - speed, getCenter().getY())
        };
        setCenter(points[realDirection]);
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
     * Returns the direction that the sprite is planning to move when it can move in
     * that direction. This isn't necessarily the direction that the sprite is actually
     * moving.
     */
    public int getDirection(){
        return direction;
    }

    /**
     * Returns the speed that the sprite is moving in pixels per second.
     */
    public int getSpeed(){
        return speed;
    }

    /**
     * Returns the actual direction that the sprite is moving, NOT the
     * direction that it's queued to turn.
     */
    public int getRealDirection() {
        return realDirection;
    }
}
