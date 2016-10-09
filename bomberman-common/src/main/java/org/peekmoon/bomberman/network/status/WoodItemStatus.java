package org.peekmoon.bomberman.network.status;

import java.nio.ByteBuffer;

public class WoodItemStatus extends ItemStatus {

	public WoodItemStatus(TileStatus tileStatus) {
		super(tileStatus);
	}
	
	public WoodItemStatus(ByteBuffer buffer, TileStatus tileStatus) {
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
