package org.peekmoon.bomberman.network.status;

import java.nio.ByteBuffer;

public class BrickItemStatus extends ItemStatus {

	public BrickItemStatus(TileStatus tileStatus) {
		super(tileStatus);
	}
	
	public BrickItemStatus(ByteBuffer buffer, TileStatus tileStatus) {
		super(tileStatus);
	}
	
    @Override
    public boolean isTraversable() {
        return false;
    }

}
