import edu.macalester.graphics.Rectangle;

public class Player extends Sprite {

    public Player(double initialX, double initialY, int initialDirection) {
        super(initialX, initialY, initialDirection);
        add(new Rectangle(0, 0, 10, 10));
    }
    
}
