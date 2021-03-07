public class Node {
    private boolean isWall;
    private int xBoardPos;
    private int yBoardPos;
    private Node parent;

    private int gCost;
    private int hCost;

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

    public void setgCost(int gCost) {
        this.gCost = gCost;
    }

    public void sethCost(int hCost) {
        this.hCost = hCost;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public int getfCost() {
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

    public int gethCost() {
        return hCost;
    }

    public int getgCost() {
        return gCost;
    }

    public boolean isWall() {
        return isWall;
    }
}
