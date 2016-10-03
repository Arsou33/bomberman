package org.peekmoon.bomberman.network.status;

import java.nio.ByteBuffer;

public abstract class ItemStatus {
	
	private final TileStatus tileStatus;
	
	public abstract boolean isTraversable();

	public ItemStatus(TileStatus tileStatus) {
		this.tileStatus = tileStatus;
	}

	public void fill(ByteBuffer buffer) {
		buffer.put(getType().getCode());
	}

	public static ItemStatus from(ByteBuffer buffer, TileStatus tileStatus) {
		ItemType type = ItemType.get(buffer.get());
		try {
			return type.getItemClass().getConstructor(ByteBuffer.class, TileStatus.class).newInstance(buffer, tileStatus);
		} catch (ReflectiveOperationException e) {
			throw new IllegalStateException("Unable to instanciate ItemStatus for type " + type.getItemClass(), e);
		}
	}
	
	public ItemType getType() {
		return ItemType.get(this.getClass());
	}

	public int getI() {
		return tileStatus.getI();
	}
	
	public int getJ() {
		return tileStatus.getJ();
	}
}
