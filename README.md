# Pacman!

Team Members: Youssef, Ruben, Jeremy

## How the user interacts with the program

The user starts the game by running the main method of the Pacman.java file. You can also run the jar file from a command line. The player moves with the arrow keys, these are the only controls in the game.

If you experience a bug where pacman and the ghosts don’t look like they should, take the images out of the /res folder and put them back in. Some of us have experienced this and fixed it this way.

## Concept

Pacman is an arcade game developed by Namco, in which the player (who controls Pac-Man) must eat all the dots inside the maze while avoiding the ghosts.
In this version, the player has three tries to finish eating all the dots before the ghosts catch him. As the player’s score gets higher, the ghosts start chasing the player more closely.

## Algorithm

This was a major point of the intial planning. For the ghost AI, we use a pathfinding algorithm called A* (a star). Each ghost keeps track of all the tiles on the board as “nodes”, and calculates the path with the lowest cost for it to reach its destination. The destinations are randomly selected within a certain radius of the player. As the player collects more dots, this radius decreases, making the ghosts move directly to the player. The path to reach the destination is recalculated each frame to allow for the ghosts to vary from the original intended path. For example, if the original path has the ghost turning around, they can’t do this so the path must be recalculated and it only starts following the path when it doesn't have to turn around to do so anymore. There are cases where the ghost would get “stuck” as in the direction it wants to move to follow the path it has found to the end point is to turn around. In this case, the ghost picks a random direction to move and starts moving that direction.

## Movement

The player and ghosts each move two pixels per frame. If the player presses a key to turn in a certain direction, they will turn there at their next opportunity. If the player eats a dot, they’ll stop for a frame, allowing ghosts to catch up. That being said, the player can start to take a corner before ghosts and move slightly diagonally to gain more time. The player and ghosts never speed up. The player moves using the arrow keys.

## Implementation

The ‘PacMan.java’ class manages the game. ‘Sprite.java’ is the superclass that encompasses the 'Player.java' and 'Ghost.java' classes, where most of their characteristcs are defined. The 'Tile.java' class manages assigning the tiles different types based on their functionality.

## Challenges

As mentioned above, the possiblity of implementing a more advanced ghost AI rather than simple randomizatin was a concern during the planning phase. We were also ware of the complications of general movement and making sure the sprites interacted with the tiles as wanted.