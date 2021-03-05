// import edu.macalester.graphics.Rectangle;

import edu.macalester.graphics.*;

public class Player extends Sprite {
    /**
     * the player that the user controls (pacman themself)
     */
    public Player(Point center, int initialDirection, Tile[][] board) {
        super(center, initialDirection, board);
        Image player = new Image (0, 0, "pacman.gif");
        player.setScale(0.45);
        add(player);
        // add(new Rectangle(0, 0, 10, 10));
    }
}
