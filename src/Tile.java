import java.awt.Color;
import edu.macalester.graphics.*;

public class Tile extends GraphicsGroup{
    private final int TYPE;
    private boolean hasDot;
    private Ellipse dotShape;
    private Point center;

    public Tile(int type, Point point) {
        this.TYPE = type;
        this.center = point;
        switch (type) {
            case 0:
                addDotTile();
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

    private void addDotTile() {
        hasDot = true;
        Rectangle tileShape = new Rectangle(0, 0, PacMan.TILE_SIDE_LENGTH, PacMan.TILE_SIDE_LENGTH);
        tileShape.setFillColor(Color.BLACK);
        add(tileShape);
        dotShape = new Ellipse(0, 0, PacMan.DOT_SIZE, PacMan.DOT_SIZE);
        dotShape.setFillColor(Color.WHITE);
        add(dotShape);
        dotShape.setCenter(PacMan.TILE_SIDE_LENGTH/2, PacMan.TILE_SIDE_LENGTH/2);
    }

    public void removeDot() {
        if (hasDot == true) {
            remove(dotShape);
            hasDot = false;
        }
    }

    private void makeWall() {
        Rectangle wallShape = new Rectangle(0, 0, PacMan.TILE_SIDE_LENGTH-1, PacMan.TILE_SIDE_LENGTH-1);
        wallShape.setFillColor(Color.BLACK);
        wallShape.setStrokeColor(Color.BLUE);
        add(wallShape);
    }
    
    private void makeGhostArea() {
        Rectangle wallShape = new Rectangle(0, 0, PacMan.TILE_SIDE_LENGTH, PacMan.TILE_SIDE_LENGTH);
        wallShape.setFillColor(Color.BLACK);
        add(wallShape);
        Rectangle topLine = new Rectangle(0, 0, PacMan.TILE_SIDE_LENGTH, 1);
        topLine.setStrokeColor(Color.WHITE);
        add(topLine);
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
