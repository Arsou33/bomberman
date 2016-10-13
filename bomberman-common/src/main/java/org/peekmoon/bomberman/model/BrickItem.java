package org.peekmoon.bomberman.model;

import java.nio.ByteBuffer;

public class BrickItem extends Item {

	public BrickItem(Tile tileStatus) {
		super(tileStatus);
	}
	
	public BrickItem(ByteBuffer buffer, Tile tileStatus) {
		super(tileStatus);
	}
	
    @Override
    public boolean isTraversable() {
        return false;
    }
    
    @Override
    public boolean isPropagateFire() {
        return false;
    }
    
    @Override
    public boolean fire() {
        return false;
    }

}
