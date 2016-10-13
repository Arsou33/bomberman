package org.peekmoon.bomberman.model;

import java.nio.ByteBuffer;

public abstract class Item {
	
	private final Tile tileStatus;
	
	public abstract boolean isTraversable();
	public abstract boolean isPropagateFire();
	public abstract boolean fire();

	public Item(Tile tileStatus) {
		this.tileStatus = tileStatus;
	}

	public void fill(ByteBuffer buffer) {
		buffer.put(getType().getCode());
	}

	public static Item from(ByteBuffer buffer, Tile tileStatus) {
		ItemType type = ItemType.get(buffer.get());
		try {
			return type.getItemClass().getConstructor(ByteBuffer.class, Tile.class).newInstance(buffer, tileStatus);
		} catch (ReflectiveOperationException e) {
			throw new IllegalStateException("Unable to instanciate ItemStatus for type " + type.getItemClass(), e);
		}
	}
	
	public boolean update() {
		return false;
	}
	
	public ItemType getType() {
		return ItemType.get(this.getClass());
	}
	
	protected Tile getTile() {
		return tileStatus;
	}

	public int getI() {
		return tileStatus.getI();
	}
	
	public int getJ() {
		return tileStatus.getJ();
	}
	
}
