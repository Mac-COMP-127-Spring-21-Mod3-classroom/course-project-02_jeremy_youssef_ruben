public class Node {
    private boolean isWall;
    private int xBoardPos;
    private int yBoardPos;
    private Node parent;

    private double gCost;
    private double hCost;

    /**
     * this is a node object to be used in the ghost's pathfinding
     */
    public Node(int xBoardPos, int yBoardPos, Tile[][] board, boolean isGhostStart) {
        this.xBoardPos = xBoardPos;
        this.yBoardPos = yBoardPos;
        this.isWall = board[yBoardPos][xBoardPos].getType()==1 ? true : false;
        if (isGhostStart) this.isWall = false;
    }

    public String toString() {
        //  + " It's parent node is: " + parent.toString()
        return "This node is at: " + xBoardPos + " , " + yBoardPos + " this is a wall: " + isWall;
    }

    public void setgCost(double gCost) {
        this.gCost = gCost;
    }

    public void sethCost(double hCost) {
        this.hCost = hCost;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public double getfCost() {
        return gCost + hCost;
    }

    public Node getParent() {
        return parent;
    }

    public int getxBoardPos() {
        return xBoardPos;
    }

    public int getyBoardPos() {
        return yBoardPos;
    }

    public double gethCost() {
        return hCost;
    }

    public double getgCost() {
        return gCost;
    }

    public boolean isWall() {
        return isWall;
    }
}
