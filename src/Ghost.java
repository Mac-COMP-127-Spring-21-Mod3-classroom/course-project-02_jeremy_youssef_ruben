import edu.macalester.graphics.*;
import java.util.Random;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Ghost extends Sprite {
    private int count = 0;
    private Random rand = new Random();
    private List<Image> ghostPNG = new ArrayList<>();
    private final Image redGhost = new Image("red_ghost.png");
    private final Image yellowGhost = new Image("yellow_ghost.png");
    private final Image purpleGhost = new Image("purple_ghost.png");
    private final Image greyGhost = new Image("grey_ghost.png");
    private Node targetNode;
    private Node startingNode;
    private Tile[][] board;
    private Node[] allNodes = new Node[PacMan.COLS * PacMan.ROWS];
    private List<Node> pathToTarget;
    private Node actualCurrentNode;

    public Ghost(Point point, int initialDirection, Tile[][] board) {
        super(point, initialDirection, board);
        this.board = board;
        generateAllNodesList();
        actualCurrentNode = startingNode;
        setNewTargetNode();
        doAStar();
        ghostPNG.add(redGhost);
        ghostPNG.add(yellowGhost);
        ghostPNG.add(purpleGhost);
        ghostPNG.add(greyGhost);
        Image ghost = ghostPNG.get(rand.nextInt(ghostPNG.size()));
        ghost.setScale(0.025);
        add(ghost);
    }

    public void updatePos() {
        // if (count > 100) {
        //     int random;
        //     while (true) {
        //         random = rand.nextInt(4);
        //         if (random == super.getRealDirection()) continue;
        //         else if (random == 0 && super.getRealDirection() == 2) continue;
        //         else if (random == 2 && super.getRealDirection() == 0) continue;
        //         else if (random == 1 && super.getRealDirection() == 3) continue;
        //         else if (random == 3 && super.getRealDirection() == 1) continue;
        //         break;
        //     }
        //     super.changeDirection(random);
        //     // if (random == 3) super.setRotation(); need to figure out a way to flip images along an axis
        //     // if (random == 1) super.setRotation();
        //     count = 0;
        // }

        // if (count > 1000) {
        //     setNewTargetNode();
        //     doAStar();
        //     count = 0;
        // }

        updateActualCurrentNode();
        if (pathToTarget.size() == 0) {
            // for visualizing a star
            for (Node node : allNodes) {
                board[node.getxBoardPos()][node.getyBoardPos()].setTileFillColor(Color.BLACK);
            }
            setNewTargetNode();
            doAStar();
        }
        Node nextNode = pathToTarget.get(0);
        if (actualCurrentNode == nextNode) {
            pathToTarget.remove(nextNode);
            if (pathToTarget.size() == 0) return;
            nextNode = pathToTarget.get(0);
            // for visualizing a star
            board[nextNode.getxBoardPos()][nextNode.getyBoardPos()].setTileFillColor(Color.BLUE);
        }
        if (nextNode.getxBoardPos() - actualCurrentNode.getxBoardPos() < 0) {
            changeDirection(3);
        } else if (nextNode.getxBoardPos() - actualCurrentNode.getxBoardPos() > 0) {
            changeDirection(1);
        } else if (nextNode.getyBoardPos() - actualCurrentNode.getyBoardPos() < 0) {
            changeDirection(0);
        } else if (nextNode.getyBoardPos() - actualCurrentNode.getyBoardPos() > 0) {
            changeDirection(2);
        }

        super.updatePos();
        count++;
    }

    private void updateActualCurrentNode() {
        for (Node node : allNodes) {
            if (board[node.getxBoardPos()][node.getyBoardPos()] == getCurrentTile()) {
                actualCurrentNode = node;
                return;
            }
        }
    }

    private void generateAllNodesList() {
        for (int i = 0; i < PacMan.ROWS; i++) {
            for (int j = 0; j < PacMan.COLS; j++) {
                if (board[i][j] == getCurrentTile()) {
                    allNodes[i*PacMan.ROWS + j] = new Node(i, j, board, true);
                    startingNode = allNodes[i*PacMan.ROWS + j];
                } else {
                    allNodes[i*PacMan.ROWS + j] = new Node(i, j, board, false);
                }
            }
        }
    }

    private void setNewTargetNode() {
        do {
            targetNode = allNodes[rand.nextInt(PacMan.COLS * PacMan.ROWS)];
            startingNode = actualCurrentNode;
        } while (targetNode.isWall());
        // for visualizing a star
        board[targetNode.getxBoardPos()][targetNode.getyBoardPos()].setTileFillColor(Color.RED);
    }

    private List<Node> getNeighbours(Node currentNode) {
        // todo: right now it doesnt know about wrapping
        List<Node> neighbours = new ArrayList<>();
        for (Node node : allNodes) {
            if (node.getxBoardPos() == currentNode.getxBoardPos() 
            || node.getyBoardPos() == currentNode.getyBoardPos()) {
                if (Math.abs(node.getxBoardPos() - currentNode.getxBoardPos()) == 1 || Math.abs(node.getyBoardPos() - currentNode.getyBoardPos()) == 1) {
                    neighbours.add(node);
                }
            }
        }
        return neighbours;
    }

    private int getDistance(Node currentNode, Node neighbour) {
        return ((Math.abs(neighbour.getxBoardPos() - currentNode.getxBoardPos())) + (Math.abs(neighbour.getyBoardPos() - currentNode.getyBoardPos())));
    }

    private void retracePath(Node startNode, Node endNode) {
        pathToTarget = new ArrayList<>();
        Node currentNode = endNode;
        while (currentNode != startNode) {
            pathToTarget.add(currentNode);
            currentNode = currentNode.getParent();
        }
        Collections.reverse(pathToTarget);
    }

    // credit to this tutorial for some of the code (adapted for java and pacman by me): https://www.youtube.com/watch?v=mZfyt03LDH4
    private void doAStar() {
        List<Node> openList = new ArrayList<>();
        openList.add(startingNode);
        List<Node> closedList = new ArrayList<>();

        while (openList.size() > 0) {
            Node currentNode = openList.get(0);
            for (int i = 0; i < openList.size(); i++) {
                if (openList.get(i).getfCost() < currentNode.getfCost() ||
                    (openList.get(i).getfCost() == currentNode.getfCost() && openList.get(i).gethCost() < currentNode.gethCost())) {
                        currentNode = openList.get(i);
                }
            }

            openList.remove(currentNode);

            closedList.add(currentNode);

            if (currentNode == targetNode) {
                retracePath(startingNode, targetNode);
                return;
            }

            for (Node neighbour : getNeighbours(currentNode)) {
                if (neighbour.isWall() || closedList.contains(neighbour)) {
                    continue;
                }
                int newMovementCostToNeighbour = currentNode.getgCost() + getDistance(currentNode, neighbour);
                if (newMovementCostToNeighbour < neighbour.getgCost() || !openList.contains(neighbour)) {
                    neighbour.setgCost(newMovementCostToNeighbour);
                    neighbour.sethCost(getDistance(neighbour, targetNode));
                    neighbour.setParent(currentNode);

                    if (!openList.contains(neighbour)) {
                        openList.add(neighbour);
                    }
                }
            }
        }
    }
}
