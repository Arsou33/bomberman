package org.peekmoon.bomberman.model;

import java.nio.ByteBuffer;

public class FireItem extends Item {
	
    private static final float lifeTime = 1.5f;
    private float countdown;

	
	public FireItem(Tile tileStatus) {
		super(tileStatus);
		countdown = lifeTime;
	}
	
	public FireItem(ByteBuffer buffer, Tile tileStatus) {
		super(tileStatus);
	}
	
    @Override
    public boolean isTraversable() {
        return true;
    }

    @Override
    public boolean isPropagateFire() {
        return true;
    }
    
    @Override
    public boolean update() {
        countdown -= 0.05; // TODO : remove numeric
        return countdown <= 0;
    }

    @Override
    public boolean fire() {
        return false;
    }


}
