package org.peekmoon.bomberman.network.status;

public enum ItemType {
	BRICK(BrickItemStatus.class),
	WOOD(WoodItemStatus.class);
	
	private Class<? extends ItemStatus> clazz;

	ItemType(Class<? extends ItemStatus> clazz) {
		this.clazz = clazz;
	}

	public static ItemType get(Class<? extends ItemStatus> itemClass) {
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

	public Class<? extends ItemStatus> getItemClass() {
		return clazz;
	}
	
}
