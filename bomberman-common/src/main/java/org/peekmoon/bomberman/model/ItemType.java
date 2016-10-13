package org.peekmoon.bomberman.model;

public enum ItemType {
	BRICK(BrickItem.class),
	WOOD(WoodItem.class),
	BOMB(BombItem.class),
	FIRE(FireItem.class);
	
	private Class<? extends Item> clazz;

	ItemType(Class<? extends Item> clazz) {
		this.clazz = clazz;
	}

	public static ItemType get(Class<? extends Item> itemClass) {
		for (ItemType itemType : ItemType.values()) {
			if (itemType.clazz.equals(itemClass)) return itemType;
		}
		throw new IllegalArgumentException("ItemClass " + itemClass + " is unknown" );
	}

	public static ItemType get(byte code) {
		return ItemType.values()[code];
	}

	public byte getCode() {
		return (byte) ordinal();
	}

	public Class<? extends Item> getItemClass() {
		return clazz;
	}
	
}
