import edu.macalester.graphics.*;

public class Sprite extends GraphicsGroup{
    private int direction;
    private int realDirection;
    //direction is the direction that the sprite is planned to move,
    //while realDirection is the direction the sprite is actually going
    private GraphicsObject sprite;
    
    public Sprite(double initialX, double initialY, int initialDirection) {
        setCenter(new Point(initialX, initialY));
        direction = initialDirection;
    }

    /**
     * Sets the sprite to move one of 4 directions:
     * North (0), east (1), south (2), or west (3).
     * If there's an open space in that direction, the sprite will turn and return true.
     * If not, it won't turn and will return false.
     * @param board The 2d array that represents the level layout.
     * @return A boolean representing whether or not the sprite can turn that direction.
     */
    public boolean changeDirection(int direction, Tile[][] board) {
        if(direction < 0 || direction > 4){
            return false;
        }
        
        //Types: 0 you can move through, 1 you can't, 2 only ghosts can
        //TODO: Delete this comment when it's no longer needed
        this.direction = direction;
        // if(direction == 0){
        //     if(getNearbyTile(board, 0, -1).getType() == 0){
        //         this.direction = 0;
        //         return true;        
        //     }
        // }
        // else if(direction == 1){
        //     if(getNearbyTile(board, 1, 0).getType() == 0){
        //         this.direction = 1;
        //         return true;
        //     }
        // }
        // else if(direction == 2){
        //     if(getNearbyTile(board, 0, 1).getType() == 0){
        //         this.direction = 2;
        //         return true;
        //     }
        // }
        // else if(direction == 3){
        //     if(getNearbyTile(board, -1, 0).getType() == 0){
        //         this.direction = 3;
        //         return true;
        //     }
        // }

        return false;
    }

    /**
     * Returns the tile that the sprite's center is within.
     * @param board The 2d array that represents the level layout.
     */
    public Tile getCurrentTile(Tile[][] board){
        int xPosition = (int) (10*(getCenter().getX() / (PacMan.ROWS * PacMan.TILE_SIDE_LENGTH)));
        int yPosition = (int) (10*(getCenter().getY() / (PacMan.COLS * PacMan.TILE_SIDE_LENGTH)));
        return board[xPosition][yPosition];
    }

    /**
     * Returns the tile xOffset to the left/right of the sprite and
     * yOffset to the top/bottom of the sprite.
     * @param board The 2d array that represents the level layout.
     * @param xOffset The number of tiles to the left (negative) or right (positive).
     * @param yOffset The number of tiles above (negative) or below (positive).
     */
    public Tile getNearbyTile(Tile[][] board, int xOffset, int yOffset){
        int xPosition = (int) (10*(getCenter().getX() / (PacMan.ROWS * PacMan.TILE_SIDE_LENGTH)));
        int yPosition = (int) (10*(getCenter().getY() / (PacMan.COLS * PacMan.TILE_SIDE_LENGTH)));
        return board[wrapTileChecker(xOffset + xPosition, true)][wrapTileChecker(yOffset + yPosition, false)];
    }

    private int wrapTileChecker(int position, boolean isX) {
        int toCheck = isX ? PacMan.COLS : PacMan.ROWS;
        if (position < 0) {
            return toCheck-1;
        } else if (position > toCheck) {
            return 0;
        } else {
            return position;
        }
    }

    public boolean isInCenter(Tile[][] board){
        double tileX = getCurrentTile(board).getCenter().getX() + 2.5;
        double tileY = getCurrentTile(board).getCenter().getY() - 0.5;
        Point betterTileCenter = new Point(tileX, tileY);
        if(betterTileCenter.equals(getCenter())){

            return true;
        }
        System.out.println("Tile: " + betterTileCenter);
        System.out.println("Sprite: " + getCenter());
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
            if (getNearbyTile(board, 0, -1).getType() == 0) {
                return false;
            }
        } else if (direction == 1) {
            if (getNearbyTile(board, 1, 0).getType() == 0) {
                return false;
            }
        } else if (direction == 2) {
            if (getNearbyTile(board, 0, 1).getType() == 0) {
                return false;
            }
        } else if (direction == 3) {
            if (getNearbyTile(board, -1, 0).getType() == 0) {
                return false;
            }
        }
        return true;
    }

    public void updatePos(Tile[][] board) {
        //System.out.println("Sprite center: " + getCenter());
        //System.out.println("Tile center: " + getCurrentTile(board).getCenter());
        //if the dude is just turning around, that can happen immediately
        if(isInCenter(board) || isTurningAround()){
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
    }
}
