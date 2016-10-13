package org.peekmoon.bomberman.model;

import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;
// TODO : Check if synchronized are necessary if yes put every where in xxxStatus
public class Player {
	
    private static final float speed = 0.1f;
    
	private final Board board;
    private float x,y;
    
    private final transient List<Direction> activeDirections = new LinkedList<>();

    public Player(Board board, ByteBuffer buffer) {
    	this.board = board;
        x = buffer.getFloat();
        y = buffer.getFloat();
    }
    
    public Player(Board board, int i, int j) {
		this.board = board;
		x = i;
		y = j;
	}

	public synchronized void fill(ByteBuffer buffer) {
        buffer.putFloat(x);
        buffer.putFloat(y);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    private void moveUp() {
        if (Math.ceil(y) != Math.round(y)) { // We are entering a cell
            Tile destTile = board.get(x, (float)Math.floor(y)).get(Direction.UP);
            if (!destTile.isTraversable()) return;
        }

        float distToMove = speed;
        float deltaToAlign = Math.round(x) - x;

        if (Math.abs(deltaToAlign) < distToMove) {
            x = Math.round(x);
            distToMove -= Math.abs(deltaToAlign);
        } else {
            x += distToMove * Math.signum(deltaToAlign);
            return;
        }
        y += distToMove;
    }
    
    
    private void moveDown() {
        if (Math.floor(y) != Math.round(y)) { // We are entering a cell
            Tile destTile = board.get(x, (float)Math.ceil(y)).get(Direction.DOWN);
            if (!destTile.isTraversable()) return;
        }
        
        float distToMove = speed;
        float deltaToAlign = Math.round(x) - x;

        if (Math.abs(deltaToAlign) < distToMove) {
            x = Math.round(x);
            distToMove -= Math.abs(deltaToAlign);
        } else {
            x += distToMove * Math.signum(deltaToAlign);
            return;
        }
        y -= distToMove;
    }

    private void moveLeft() {
        if (Math.floor(x) != Math.round(x)) { // We are entering a cell
            Tile destTile = board.get((float)Math.ceil(x), y).get(Direction.LEFT);
            if (!destTile.isTraversable()) return;
        }
        
        float distToMove = speed;
        float deltaToAlign = Math.round(y) - y;

        if (Math.abs(deltaToAlign) < distToMove) {
            y = Math.round(y);
            distToMove -= Math.abs(deltaToAlign);
        } else {
            y += distToMove * Math.signum(deltaToAlign);
            return;
        }
        x -= distToMove;
    }

    private void moveRight() {
        if (Math.ceil(x) != Math.round(x)) { // We are entering a cell
            Tile destTile = board.get((float)Math.floor(x), y).get(Direction.RIGHT);
            if (!destTile.isTraversable()) return;
        }        
        float distToMove = speed;
        float deltaToAlign = Math.round(y) - y;

        if (Math.abs(deltaToAlign) < distToMove) {
            y = Math.round(y);
            distToMove -= Math.abs(deltaToAlign);
        } else {
            y += distToMove * Math.signum(deltaToAlign);
            return;
        }
        x += distToMove;
    }

    public void dropBomb() {
        Tile currentTile = board.get(x, y);
        if (currentTile.canDropBomb()) {
            currentTile.dropBomb();
        }
    }
    
    public void startMove(Direction direction) {
        activeDirections.remove(direction); // Should not be in but just in case udp packet lost
        activeDirections.add(0, direction);
    }
    
    public void stopMove(Direction direction) {
    	activeDirections.remove(direction);
    }
    

	public void update() {
		if (activeDirections.isEmpty()) return;
		Direction direction = activeDirections.get(0);
		switch (direction) {
		case DOWN:
			moveDown();
			break;
		case LEFT:
			moveLeft();
			break;
		case RIGHT:
			moveRight();
			break;
		case UP:
			moveUp();
			break;
		default:
			throw new IllegalStateException("Direction " + direction + " is unknown");
		}
	}


}
