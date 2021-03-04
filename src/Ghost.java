import edu.macalester.graphics.*;
import java.awt.Color;

public class Ghost extends Sprite{
    public Ghost(Point point, int initialDirection, Tile[][] board) {
        super(point, initialDirection, board);
        Rectangle shape = new Rectangle(0, 0, 10, 10);
        shape.setFillColor(Color.RED);
        add(shape);
    }
}
