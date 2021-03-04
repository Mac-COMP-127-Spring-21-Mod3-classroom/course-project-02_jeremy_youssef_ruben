import edu.macalester.graphics.*;
import java.awt.Color;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;

public class Ghost extends Sprite {
    private int count = 0;
    private Random rand = new Random();
    private List<Image> ghostPNG = new ArrayList<>();
    private final Image redGhost = new Image("red_ghost.png");
    private final Image yellowGhost = new Image("yellow_ghost.png");
    private final Image purpleGhost = new Image("purple_ghost.png");
    private final Image greyGhost = new Image("grey_ghost.png");

    public Ghost(Point point, int initialDirection, Tile[][] board) {
        super(point, initialDirection, board);
        ghostPNG.add(redGhost);
        ghostPNG.add(yellowGhost);
        ghostPNG.add(purpleGhost);
        ghostPNG.add(greyGhost);
        Image ghost = ghostPNG.get(rand.nextInt(ghostPNG.size()));
        ghost.setScale(0.05);
        add(ghost);
        // Rectangle shape = new Rectangle(0, 0, 10, 10);
        // shape.setFillColor(Color.RED);
        // add(shape);
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
            // if (random == 3) super.setRotation(); need to figure out a way to flip images along an axis
            // if (random == 1) super.setRotation();
            count = 0;
        }
        super.updatePos();
    }
}
