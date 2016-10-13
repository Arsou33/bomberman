package org.peekmoon.bomberman.model;

import java.nio.ByteBuffer;

public class WoodItem extends Item {

	public WoodItem(Tile tileStatus) {
		super(tileStatus);
	}
	
	public WoodItem(ByteBuffer buffer, Tile tileStatus) {
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
        return true;
    }


}
