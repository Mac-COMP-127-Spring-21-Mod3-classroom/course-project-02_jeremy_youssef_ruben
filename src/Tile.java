import java.awt.Color;
import edu.macalester.graphics.*;

public class Tile extends GraphicsGroup{
    private int type;
    private boolean hasDot;
    private Ellipse dotShape;
    private Point center;
    private Rectangle tileShape;

    public Tile(int type, Point point) {
        this.type = type;
        this.center = point;
        switch (type) {
            case 0:
                makeEmptyTile();
                addDot();
                break;
            case 1:
                makeWall();
                break;
            case 2:
                makeGhostArea();
                break;
            case 4:
                makeEmptyTile();
                this.type = 0;
                break;
        
            default:
                break;
        }
    }

    private void addDot() {
        hasDot = true;
        dotShape = new Ellipse(0, 0, PacMan.DOT_SIZE, PacMan.DOT_SIZE);
        dotShape.setFillColor(Color.WHITE);
        add(dotShape);
        dotShape.setCenter(PacMan.TILE_SIDE_LENGTH/2, PacMan.TILE_SIDE_LENGTH/2);
    }

    public void setTileFillColor(Color color) {
        this.tileShape.setFillColor(color);
    }

    private void makeEmptyTile() {
        tileShape = new Rectangle(0, 0, PacMan.TILE_SIDE_LENGTH, PacMan.TILE_SIDE_LENGTH);
        tileShape.setFillColor(Color.BLACK);
        add(tileShape);
    }

    public void removeDot() {
        if (hasDot == true) {
            remove(dotShape);
            hasDot = false;
        }
    }

    private void makeWall() {
        tileShape = new Rectangle(0, 0, PacMan.TILE_SIDE_LENGTH-1, PacMan.TILE_SIDE_LENGTH-1);
        tileShape.setFillColor(Color.BLACK);
        tileShape.setStrokeColor(Color.BLUE);
        add(tileShape);
    }
    
    private void makeGhostArea() {
        tileShape = new Rectangle(0, 0, PacMan.TILE_SIDE_LENGTH, PacMan.TILE_SIDE_LENGTH);
        tileShape.setFillColor(Color.BLACK);
        add(tileShape);
        Rectangle topLine = new Rectangle(0, 0, PacMan.TILE_SIDE_LENGTH, 1);
        topLine.setStrokeColor(Color.WHITE);
        add(topLine);
    }

    public int getType() {
        return type;
    }

    public Boolean getHasDot() {
        return hasDot;
    }

    public Point getTileCenter() {
        return center;
    }
}
