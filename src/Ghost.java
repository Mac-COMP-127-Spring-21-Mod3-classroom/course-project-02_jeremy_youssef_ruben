import edu.macalester.graphics.*;
import java.util.Random;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Handles logic specific to the ghosts and not the player.
 * 
 * @author Jeremy Hubinger, Ruben Escobar, Youssef Aithmad
 */
public class Ghost extends Sprite {
    private Random rand = new Random();
    private Node targetNode=null;
    private Node startingNode=null;
    private Tile[][] board;
    private Node[] allNodes = new Node[PacMan.COLS * PacMan.ROWS];
    private List<Node> pathToTarget;
    private Node actualCurrentNode;
    private double targetPlayerDistance;
    private Node currentPlayerNode;
    private Point previousPosition = null;
    private int count = 0;

    /**
     * A ghost in the game of pacman, which will chase down and try to eliminate the player.
     * @param center the spot to spawn the ghost
     * @param initialDirection 0-3, the direction the ghost should begin by traveling
     * @param board the board that the game is being played on
     */
    public Ghost(Point center, int initialDirection, int speed, Tile[][] board, Image ghost) {
        super(center, initialDirection, speed, board);
        this.board = board;
        generateAllNodesList();
        ghost.setScale(0.025);
        add(ghost);
    }

    /**
     * The ghost's updatePos function is responsible for telling the ghost where to go to follow its path.
     * At the end of this method, it calls its super's updatePos function
     */
    @Override
    public void updatePos() {
        count ++;
        updateActualCurrentNode();

        // if it is still in the ghost starting area
        if (getNearbyTile(0, 0).getType()==1) {
            super.updatePos();
            return;
        }

        // redo a* in case the position changed without following the node path (turning around)
        if (targetNode != null) {
            startingNode = actualCurrentNode;
            doAStar();
        }
        
        // get a new target node if there isnt one (starting), if it reached its previous node, or if enough time has passed
        if (targetNode==null || pathToTarget.size() == 0 || count > 300) {
            // for visualizing a star:
            // for (Node node : allNodes) {
            //     board[node.getyBoardPos()][node.getxBoardPos()].setTileFillColor(Color.BLACK);
            // }
            setNewTargetNode();
            doAStar();
            super.updatePos();
            count = 0;
            return;
        }

        // figure out what the next node in the path to the target is and remove it from the path
        Node nextNode = pathToTarget.get(0);
        if (actualCurrentNode == nextNode) {
            pathToTarget.remove(nextNode);
            if (pathToTarget.size() == 0) return;
            nextNode = pathToTarget.get(0);
        }

        // for visualizing a star:
        // board[nextNode.getyBoardPos()][nextNode.getxBoardPos()].setTileFillColor(Color.BLUE);

        // go in the direction of the next node
        // North (0), east (1), south (2), or west (3).
        if (nextNode.getxBoardPos() - actualCurrentNode.getxBoardPos() < 0) {
            handleMovment(1, 3);
        } else if (nextNode.getxBoardPos() - actualCurrentNode.getxBoardPos() > 0) {
            handleMovment(3, 1);
        } else if (nextNode.getyBoardPos() - actualCurrentNode.getyBoardPos() < 0) {
            handleMovment(2, 0);
        } else if (nextNode.getyBoardPos() - actualCurrentNode.getyBoardPos() > 0) {
            handleMovment(0, 2);
        }

        previousPosition = getCenter();
        super.updatePos();
    }

    /**
     * helper function to handle trying to move in a direction without moving in the given second direction
     * @param n the direction you want to move
     * @param l the direction not to move
     */
    private void handleMovment(int n, int l) {
        if (getRealDirection() != n) {
            changeDirection(l);
        } else if (isStuck()) {
            dontTurnAround(l);
        }
    }

    /**
     * This function is called if the ghost is stuck (if it wants to turn around and is hitting a wall).
     * It turns in some direction that is not the direction it came from (the parameter given)
     * and in a direction that does not hit a wall
     */
    private void dontTurnAround(int directionNotToGo) {
        for (int i = 0; i < 4; i++) {
            if (i != directionNotToGo) {
                if (!hitsWall(i)) {
                    changeDirection(i);
                    return;
                }
            }
        }
    }

    /**
     * @return true if the ghost is stuck, false if not
     */
    private boolean isStuck() {
        if (previousPosition.equals(getCenter())) {
            return true;
        }
        return false;
    }

    /**
     * @param targetPlayerDistance the radius around the player to set the target within
     */
    public void setTargetPlayerDistance(double targetPlayerDistance) {
        this.targetPlayerDistance = targetPlayerDistance;
    }

    /**
     * @param currentPlayerTile to update which tile the player is currently on
     */
    public void setCurrentPlayerNode(Tile currentPlayerTile) {
        currentPlayerNode = allNodes[getTileXY(currentPlayerTile)[1]*(PacMan.COLS) + getTileXY(currentPlayerTile)[0]];
    }

    /**
     * updates what node the ghost is currently using
    */
    private Node updateActualCurrentNode() {
        for (Node node : allNodes) {
            if (board[node.getyBoardPos()][node.getxBoardPos()] == getNearbyTile(0,0)) {
                actualCurrentNode = node;
                return actualCurrentNode;
            }
        }
        return null;
    }

    /**
     * updates the local variable allNodes to be an accurate list of nodes representing the board given in the constructor
     */
    private void generateAllNodesList() {
        for (int i = 0; i < PacMan.ROWS; i++) {
            for (int j = 0; j < PacMan.COLS; j++) {
                if (board[i][j] == getNearbyTile(0,0)) {
                    allNodes[i*(PacMan.COLS) + j] = new Node(j, i, board, true);
                    startingNode = allNodes[i*(PacMan.COLS) + j];
                } else {
                    allNodes[i*(PacMan.COLS) + j] = new Node(j, i, board, false);
                }
            }
        }
    }

    /**
     * sets a new target node
     */
    private void setNewTargetNode() {
        do {
            targetNode = allNodes[rand.nextInt(PacMan.COLS * PacMan.ROWS)];
            startingNode = actualCurrentNode;
        } while (board[targetNode.getyBoardPos()][targetNode.getxBoardPos()].getType() == 1 || targetNode == updateActualCurrentNode() || board[targetNode.getyBoardPos()][targetNode.getxBoardPos()].getType() == 4 || getDistance(targetNode, currentPlayerNode) > targetPlayerDistance);
        // for visualizing a star:
        // board[targetNode.getyBoardPos()][targetNode.getxBoardPos()].setTileFillColor(Color.RED);
    }

    /**
     * gets the Neighbouring nodes of the given node
     */
    private List<Node> getNeighbours(Node currentNode) {
        List<Node> neighbours = new ArrayList<>();
        for (Node node : allNodes) {
            if (node.getxBoardPos() == currentNode.getxBoardPos() 
            || node.getyBoardPos() == currentNode.getyBoardPos()) {
                if (getDifference(node.getxBoardPos(), currentNode.getxBoardPos(), true) == 1 || getDifference(node.getyBoardPos(), currentNode.getyBoardPos(), false) == 1) {
                    neighbours.add(node);
                }
            }
        }
        return neighbours;
    }

    /**
     * Gets the absolute difference between the two numbers given.
     * The given numbers should be positions on the board.
     * This function accounts for wrapping around the board.
     */
    private double getDifference(double n, double l, boolean isX) {
        int toCheck = isX ? PacMan.COLS : PacMan.ROWS;
        if ((n==0 || l==0) && (n==toCheck-1 || l==toCheck-1)) return 1;
        return Math.abs(n-l);
    }

    /**
     * This is the hueristic function that a* uses to determine what path to take.
     * It takes two nodes and returns the absolute distance between them.
     */
    private double getDistance(Node currentNode, Node neighbour) {
        return ((getDifference(neighbour.getxBoardPos(), currentNode.getxBoardPos(), true)) + (getDifference(neighbour.getyBoardPos(), currentNode.getyBoardPos(), false)));
    }

    /**
     * this function is called once a* has found a path.
     * this traces the path back up through the parent nodes of each node.
     * since a* updates the parent node of the end node to be the one on the path, and that node's parent
     * to be the next one on the path, this functino retraces that and puts the nodes (in order)
     * into the instance variable pathToTarget
     */
    private void retracePath(Node startNode, Node endNode) {
        pathToTarget = new ArrayList<>();
        Node currentNode = endNode;
        while (currentNode != startNode) {
            pathToTarget.add(currentNode);
            currentNode = currentNode.getParent();
        }
        Collections.reverse(pathToTarget);
    }

    /**
     * credit to this tutorial for some of the code (adapted for java and pacman by us): https://www.youtube.com/watch?v=mZfyt03LDH4
     * implementation of the a* pathfinding algorithm.
     * this function's purpose is to update the instance variable pathToTargetNode
     */
    private void doAStar() {
        // list of nodes to check
        List<Node> openList = new ArrayList<>();
        openList.add(startingNode);
        // list of nodes already checked
        List<Node> closedList = new ArrayList<>();

        // check the nodes in the open list (this while loop adds to the open list, so when it is done, the path should have been found)
        while (openList.size() > 0) {
            Node currentNode = openList.get(0);
            // check nodes in open list, if we have found a better than where we currently are, set that to the current node
            for (int i = 0; i < openList.size(); i++) {
                if (openList.get(i).getfCost() < currentNode.getfCost() ||
                    (openList.get(i).getfCost() == currentNode.getfCost() && openList.get(i).gethCost() < currentNode.gethCost())) {
                        currentNode = openList.get(i);
                }
            }

            openList.remove(currentNode);

            closedList.add(currentNode);

            // if we have found a path to the target
            if (currentNode == targetNode) {
                retracePath(startingNode, targetNode);
                return;
            }

            // update the cost and parent of each neighbour node and add them to the open list
            for (Node neighbour : getNeighbours(currentNode)) {
                if (neighbour.isWall() || closedList.contains(neighbour)) {
                    continue;
                }
                double newMovementCostToNeighbour = currentNode.getgCost() + getDistance(currentNode, neighbour);
                // if the new cost to reach this node is less than the old cost, update its parent and cost
                if (newMovementCostToNeighbour < neighbour.getgCost() || !openList.contains(neighbour)) {
                    neighbour.setgCost(newMovementCostToNeighbour);
                    neighbour.sethCost(getDistance(neighbour, targetNode));
                    neighbour.setParent(currentNode);

                    // add this node to the open list
                    if (!openList.contains(neighbour)) {
                        openList.add(neighbour);
                    }
                }
            }
        }
    }
}
