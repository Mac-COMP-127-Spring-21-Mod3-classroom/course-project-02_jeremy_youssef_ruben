import edu.macalester.graphics.*;

public class Sprite extends GraphicsGroup{
    //direction is the direction that the sprite is planned to move,
    private int direction;
    //while realDirection is the direction the sprite is actually going
    private int realDirection;
    private int speed;
    private Tile[][] board;
    
    public Sprite(Point center, int initialDirection, int speed, Tile[][] board) {
        this.board = board;
        setCenter(center);
        changeDirection(direction);
        this.speed = speed;
    }

    /**
     * Sets the sprite to move one of 4 directions:
     * North (0), east (1), south (2), or west (3).
     * @param board The 2d array that represents the level layout.
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

    public int[] getTileXY(GraphicsObject object) {
        int xPosition = (int) (PacMan.COLS*(object.getCenter().getX() / (PacMan.COLS * PacMan.TILE_SIDE_LENGTH)));
        int yPosition = (int) (PacMan.ROWS*(object.getCenter().getY() / (PacMan.ROWS * PacMan.TILE_SIDE_LENGTH)));
        return new int[] {xPosition,yPosition};
    }

    /**
     * @return what tile position to look at (adjusted so that -1 turns into the max, and anything over the max turns into 0)
     * @param position what position we want to look at
     * @param isX are we looking at the rows? if fasle, then look at columns
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
        // System.out.println("TILE: " + new Point(tileX, tileY));
        // System.out.println("TILE (center): " + getNearbyTile(0,0).getCenter());
        // System.out.println("PLAYER: " + getCenter());
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
     * Determines if there's a wall in front of the sprite.
     * @param direction The direction that the sprite is moving or trying to move.
     * @return False if there's an open tile in front of the sprite, true if there's anything else.
     */
    public boolean hitsWall(int direction){
        // North (0), east (1), south (2), or west (3).
        if (direction == 0) {
            if (getNearbyTile(0, -1).getType() == 1) {
                return true;
            }
        } else if (direction == 1) {
            if (getNearbyTile(1, 0).getType() == 1) {
                return true;
            }
        } else if (direction == 2) {
            if (getNearbyTile(0, 1).getType() == 1) {
                return true;
            }
        } else if (direction == 3) {
            if (getNearbyTile(-1, 0).getType() == 1) {
                return true;
            }
        }
        return false;
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
            if(!hitsWall(direction)){
                realDirection = direction;
                turnSprite();
            }
            if(hitsWall(realDirection)){
                return;
            }
        }
        if(realDirection == 0){
            Point newPos = new Point(getCenter().getX(), getCenter().getY() - speed);
            setCenter(newPos);
        }
        else if(realDirection == 1){
            Point newPos = new Point(getCenter().getX() + speed, getCenter().getY());
            setCenter(newPos);
        }
        else if(realDirection == 2){
            Point newPos = new Point(getCenter().getX(), getCenter().getY() + speed);
            setCenter(newPos);
        }
        else if(realDirection == 3){
            Point newPos = new Point(getCenter().getX() - speed, getCenter().getY());
            setCenter(newPos);
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
     * Returns the direction that the sprite is planning to move when it can move in
     * that direction. This isn't necessarily the direction that the sprite is actually
     * moving.
     */
    public int getDirection(){
        return direction;
    }

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
