// import edu.macalester.graphics.Rectangle;

import edu.macalester.graphics.*;

public class Player extends Sprite {
    private int locked;
    //The direction and realDirection can't be changed as long as locked is above 0.

    /**
     * the player that the user controls (pacman themself)
     */
    public Player(Point center, int initialDirection, int speed, Tile[][] board) {
        super(center, initialDirection, speed, board);
        locked = 0;
        Image player = new Image (0, 0, "pacman.gif");
        player.setScale(0.45);
        add(player);
        // add(new Rectangle(0, 0, 10, 10));
    }

    //if the player's a bit before, then 1) lock their direction and realDirection
    //until they hit the midpoint 2) move in both direction and realDirection

    // @Override
    // public void updatePos(){
    //     double x = getCenter().getX();
    //     double y = getCenter().getY();
    //     if(locked > 0){
    //         if(!hitsWall(getRealDirection())){
    //             moveDiagonally();
    //             locked--;
    //         }
    //     }
    //     else{
    //         if(getDirection() != getRealDirection() && !isTurningAround()){

    //         }
    //     }//end of else
    // }

    public void moveDiagonally(){
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

    public boolean isInCenter(Point forwardPosition){
        double tileX = getNearbyTile(0,0).getTileCenter().getX();
        double tileY = getNearbyTile(0,0).getTileCenter().getY();
        Point betterTileCenter = new Point(tileX, tileY);
        if(betterTileCenter.equals(forwardPosition)){
            return true;
        }
        return false;
    }

    public Point getForwardPosition(int howFar){
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
