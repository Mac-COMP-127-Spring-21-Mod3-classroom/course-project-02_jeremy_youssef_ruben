import edu.macalester.graphics.*;
import java.awt.Color;
import java.util.Random;

public class Ghost extends Sprite {
    private int count = 0;
    private Random rand = new Random();

    public Ghost(Point point, int initialDirection, Tile[][] board) {
        super(point, initialDirection, board);
        Rectangle shape = new Rectangle(0, 0, 10, 10);
        shape.setFillColor(Color.RED);
        add(shape);
    }

    public void updatePos() {
        count++;
        if (count > 100) {
            int random;
            while (true) {
                random = rand.nextInt(4);
                if (random == super.getRealDirection()) continue;
                else if (random == 0 && super.getRealDirection() == 2) continue;
                else if (random == 2 && super.getRealDirection() == 0) continue;
                else if (random == 1 && super.getRealDirection() == 3) continue;
                else if (random == 1 && super.getRealDirection() == 3) continue;
                break;
            }
            super.changeDirection(random);
            count = 0;
        }
        super.updatePos();
    }
}
