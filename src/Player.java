import edu.macalester.graphics.*;

public class Player extends Sprite {
    private int animationCounter;
    private int locked;
    //The direction and realDirection can't be changed as long as locked is above 0.
    private Image openMouth;
    private Image closedMouth;
    private boolean whichAnimation;

    /**
     * the player that the user controls (pacman themself)
     */
    public Player(Point center, int initialDirection, int speed, Tile[][] board) {
        super(center, initialDirection, speed, board);
        locked = 0;
        animationCounter = 0;
        openMouth = new Image (0, 0, "pacman.gif");
        openMouth.setScale(0.45);
        closedMouth = new Image (0, 0, "pacmanmouthclosed.gif");
        closedMouth.setScale(0.45);
        add(openMouth);
    }

    /**
     * this override implements corner cutting for the player by doing the following:
     * if the player's a bit before (the center of the tile), then 1) lock their direction and realDirection
     * until they hit the midpoint 2) move in both direction and realDirection
     */
    @Override
    public void updatePos(){
        if(!hitsWall(getRealDirection())){
            animationCounter++;
            animate();
        }
        if(super.isInCenter()){
            //If it's in the center of a tile, it's not moving diagonally anyway
            super.updatePos();
        }
        else{
            if(locked > 0){
                if(!hitsWall(getRealDirection())){
                    moveDiagonally();
                    locked--;
                }
            }
            else{
                if(getDirection() != getRealDirection() && !isTurningAround() && isInCenter(4)){
                    moveDiagonally();
                }
                else{
                    super.updatePos();
                }
            }//end of inner else
        }//end of outer else
    }

    /**
     * Switches between PacMan's open mouth picture sprite and his closed mouth
     * sprite every 5 frames.
     */
    private void animate(){
        if(animationCounter%5 == 0){
            whichAnimation = !whichAnimation;
            if(whichAnimation){
                remove(openMouth);
                add(closedMouth);
            }
            else{
                remove(closedMouth);
                add(openMouth);
            }
        }
    }

    /**
     * Moves PacMan diagonally if it's cutting a corner, moving it in both its
     * planned direction for after the turn and the direction that it was moving
     * before the turn.
     */
    private void moveDiagonally(){
        Point newPos = getCenter();
        if(getRealDirection() == 0){
            if(getDirection() == 3){//Up and turning left
                newPos = new Point(getCenter().getX() - getSpeed(), getCenter().getY() - getSpeed());
            }
            else if(getDirection() == 1){//Up and turning right
                newPos = new Point(getCenter().getX() + getSpeed(), getCenter().getY() - getSpeed());
            }
            
        }
        else if(getRealDirection() == 1){
            if(getDirection() == 0){//Right and turning up
                newPos = new Point(getCenter().getX() + getSpeed(), getCenter().getY() - getSpeed());
            }
            else if(getDirection() == 2){//Right and turning down
                newPos = new Point(getCenter().getX() + getSpeed(), getCenter().getY() + getSpeed());
            }
        }
        else if(getRealDirection() == 2){
            if(getDirection() == 3){//Down and turning left
                newPos = new Point(getCenter().getX() - getSpeed(), getCenter().getY() + getSpeed());
            }
            else if(getDirection() == 1){//Down and turning right
                newPos = new Point(getCenter().getX() + getSpeed(), getCenter().getY() + getSpeed());
            }
        }
        else if(getRealDirection() == 3){
            if(getDirection() == 0){//Left and turning up
                newPos = new Point(getCenter().getX() - getSpeed(), getCenter().getY() - getSpeed());
            }
            else if(getDirection() == 2){//Left and turning down
                newPos = new Point(getCenter().getX() - getSpeed(), getCenter().getY() + getSpeed());
            }
        }
        setCenter(newPos);
    }

    /**
     * Returns true if the center of the tile is within the given value in terms
     * of pixels. For example, if howFar is 6 and the center is in 3 pixels, it
     * will return true.
     * @param howFar The number of pixels forward to check.
     */
    private boolean isInCenter(int howFar){
        double tileX = getNearbyTile(0,0).getTileCenter().getX();
        double tileY = getNearbyTile(0,0).getTileCenter().getY();
        Point forwardPosition = getForwardPosition(howFar);
        Point betterTileCenter = new Point(tileX, tileY);
        if(betterTileCenter.equals(forwardPosition)){
            locked = howFar;
            return true;
        }
        if(howFar > 0){
            return isInCenter(howFar - 1);
        }
        return false;
    }

    /**
     * Returns the position that PacMan will be at (assuming there are no obstacles)
     * if it moves howFar pixels in its planned direction.
     * @param howFar The amount of pixels that the player's movement is being simulated.
     */
    private Point getForwardPosition(int howFar){
        double x = getCenter().getX();
        double y = getCenter().getY();
        if(getDirection()==0){
            return new Point(x, y - howFar);
        }
        else if(getDirection()==1){
            return new Point(x + howFar, y);
        }
        else if(getDirection()==2){
            return new Point(x, y + howFar);
        }
        else{//Only possibility here is 3
            return new Point(x - howFar, y);
        }
    }

}
