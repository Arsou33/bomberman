package org.peekmoon.bomberman.model;

import java.nio.ByteBuffer;

public class BombItem extends Item {
	
    private static final float lifeTime = 4;
    private static final int power = 3;
    private float countdown;

	public BombItem(Tile tileStatus) {
		super(tileStatus);
        this.countdown = lifeTime;
	}
	
	public BombItem(ByteBuffer buffer, Tile tileStatus) {
		super(tileStatus);
	}
	
    @Override
    public boolean update() {
    	// TODO : remove numeric
        countdown -= 0.05; 
        if (countdown <= 0) {
           getTile().fire();
        }
        return false; // Will be remove when fire
    }

	@Override
	public boolean fire() {
	    for (Direction direction : Direction.values()) {
	        Tile currentTile = getTile();
	        for (int i=0; i<power; i++) {
	            currentTile = currentTile.get(direction);
	            currentTile.fire();
	            if (!currentTile.isPropagateFire()) break;
	        }
	    }
	    return true;
	}

	@Override
	public boolean isTraversable() {
		return false;
	}
	
	@Override
	public boolean isPropagateFire() {
	    return true;
	}

}
