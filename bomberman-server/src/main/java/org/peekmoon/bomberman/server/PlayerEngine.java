package org.peekmoon.bomberman.server;

import java.util.LinkedList;
import java.util.List;

import org.peekmoon.bomberman.board.Direction;
import org.peekmoon.bomberman.network.Command;
import org.peekmoon.bomberman.network.PlayerMoveCommand;
import org.peekmoon.bomberman.network.PlayerStatus;

public class PlayerEngine {
    
    private static final float speed = 3;

    private final PlayerStatus player = new PlayerStatus();
    private final List<Direction> activeDirections = new LinkedList<>();
    
    private float elapsed = 0.02f; // TODO : Fix elapsed
    
    public void move(Direction direction) {
        if (direction == null) return;
        switch (direction) {
        case DOWN:
            moveDown(elapsed);
            break;
        case LEFT:
            moveLeft(elapsed);
            break;
        case RIGHT:
            moveRight(elapsed);
            break;
        case UP:
            moveUp(elapsed);
            break;
        default:
            throw new IllegalStateException("Direction " + direction + " is unknown");
        
        }
    }
    
    // TODO : Reimplement check on tile
    
    public void moveUp(float elapsed) {
        if (Math.ceil(player.getY()) != Math.round(player.getY())) { // We are entering a cell
            //Tile destTile = board.get(x, (float)Math.ceil(player.getY())).get(Direction.DOWN);
            //if (!destTile.isTraversable()) return;
        }
        
        float distToMove = elapsed * speed;
        float deltaToAlign = Math.round(player.getX()) - player.getX();

        if (Math.abs(deltaToAlign) < distToMove) {
            player.setX(Math.round(player.getX()));
            distToMove -= Math.abs(deltaToAlign);
        } else {
            player.deltaX(distToMove * Math.signum(deltaToAlign));
            return;
        }
        player.deltaY(distToMove);
    }
    
    
    public void moveDown(float elapsed) {
        if (Math.floor(player.getY()) != Math.round(player.getY())) { // We are entering a cell
            //Tile destTile = board.get(x, (float)Math.ceil(player.getY())).get(Direction.DOWN);
            //if (!destTile.isTraversable()) return;
        }
        
        float distToMove = elapsed * speed;
        float deltaToAlign = Math.round(player.getX()) - player.getX();

        if (Math.abs(deltaToAlign) < distToMove) {
            player.setX(Math.round(player.getX()));
            distToMove -= Math.abs(deltaToAlign);
        } else {
            player.deltaX(distToMove * Math.signum(deltaToAlign));
            return;
        }
        player.deltaY(-distToMove);
    }

    public void moveLeft(float elapsed) {
        if (Math.floor(player.getX()) != Math.round(player.getX())) { // We are entering a cell
            //Tile destTile = board.get((float)Math.ceil(posX), posY).get(Direction.LEFT);
            //if (!destTile.isTraversable()) return;
        }
        
        float distToMove = elapsed * speed;
        float deltaToAlign = Math.round(player.getY()) - player.getY();

        if (Math.abs(deltaToAlign) < distToMove) {
            player.setY(Math.round(player.getY()));
            distToMove -= Math.abs(deltaToAlign);
        } else {
            player.deltaY(distToMove * Math.signum(deltaToAlign));
            return;
        }
        player.deltaX(-distToMove);
    }

    public void moveRight(float elapsed) {
        if (Math.ceil(player.getX()) != Math.round(player.getX())) { // We are entering a cell
            //Tile destTile = board.get((float)Math.floor(posX), posY).get(Direction.RIGHT);
            //if (!destTile.isTraversable()) return;
        }        
        float distToMove = elapsed * speed;
        float deltaToAlign = Math.round(player.getY()) - player.getY();

        if (Math.abs(deltaToAlign) < distToMove) {
            player.setY(Math.round(player.getY()));
            distToMove -= Math.abs(deltaToAlign);
        } else {
            player.deltaY(distToMove * Math.signum(deltaToAlign));
            return;
        }
        player.deltaX(distToMove);
    }

    public void dropBomb() {
        /*
        Tile currentTile = board.get(posX, posY);
        if (currentTile.canDropBomb()) {
            currentTile.dropBomb();
        }
        */
    }

    public void apply(Command command) {
        // TODO : Use polymorphism to apply instead of instanceof
        switch (command.getType()) {
        case PLAYER_DROP_BOMB:
            dropBomb();
            break;
        case PLAYER_MOVE:
            PlayerMoveCommand playerMoveCommand = (PlayerMoveCommand)command;
            activeDirections.remove(playerMoveCommand.getDirection());
            if (playerMoveCommand.isStart()) {
                activeDirections.add(0, playerMoveCommand.getDirection());
            }
            break;
        default:
            throw new IllegalStateException("Unknown type " + command.getType());
        
        }
    }

    public PlayerStatus getStatus() {
        return player;
    }

    public void update() {
        if (!activeDirections.isEmpty()) {
            move(activeDirections.get(0));
        }
    }


}
