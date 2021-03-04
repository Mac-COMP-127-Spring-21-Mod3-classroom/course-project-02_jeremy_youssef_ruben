// import edu.macalester.graphics.Rectangle;

import edu.macalester.graphics.*;

public class Player extends Sprite {
    public Player(Point point, int initialDirection, Tile[][] board) {
        super(point, initialDirection, board);
        Image player = new Image (0, 0, "pacman.gif");
        player.setScale(0.5);
        add(player);
        // add(new Rectangle(0, 0, 10, 10));
    }
}
