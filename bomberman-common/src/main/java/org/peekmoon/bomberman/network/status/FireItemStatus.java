package org.peekmoon.bomberman.network.status;

import java.nio.ByteBuffer;

public class FireItemStatus extends ItemStatus {
	
    private static final float lifeTime = 1.5f;
    private float countdown;

	
	public FireItemStatus(TileStatus tileStatus) {
		super(tileStatus);
		countdown = lifeTime;
	}
	
	public FireItemStatus(ByteBuffer buffer, TileStatus tileStatus) {
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
