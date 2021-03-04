import java.awt.Color;
import edu.macalester.graphics.*;

public class Tile extends GraphicsGroup{
    private final int TYPE;
    private boolean hasDot;
    private Ellipse dotShape;
    private Rectangle wallShape;
    private Point center;

    public Tile(int type, Point point) {
        this.TYPE = type;
        this.center = point;
        switch (type) {
            case 0:
                addDot();
                break;
            case 1:
                makeWall();
                break;
            case 2:
                makeGhostArea();
                break;
        
            default:
                break;
        }
    }

    private void addDot() {
        hasDot = true;
        dotShape = new Ellipse(0, 0, PacMan.DOT_SIZE, PacMan.DOT_SIZE);
        add(dotShape);
    }

    public void removeDot() {
        if (hasDot == true) {
            remove(dotShape);
            hasDot = false;
        }
    }

    private void makeWall() {
        wallShape = new Rectangle(0, 0, PacMan.TILE_SIDE_LENGTH, PacMan.TILE_SIDE_LENGTH);
        wallShape.setFillColor(Color.BLACK);
        add(wallShape);
    }
    
    private void makeGhostArea() {
        wallShape = new Rectangle(0, 0, PacMan.TILE_SIDE_LENGTH, PacMan.TILE_SIDE_LENGTH);
        wallShape.setFillColor(Color.RED);
        add(wallShape);
    }

    public int getType() {
        return TYPE;
    }

    public Boolean getHasDot() {
        return hasDot;
    }

    public Point getTileCenter() {
        return center;
    }
}
