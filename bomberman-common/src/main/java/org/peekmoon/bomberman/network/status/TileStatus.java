package org.peekmoon.bomberman.network.status;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class TileStatus {
	
    private final int i,j;
    private final List<ItemStatus> items;
    
    public TileStatus(int i, int j) {
    	this.i = i;
    	this.j = j;
    	this.items = new ArrayList<>();
    }

	public TileStatus(ByteBuffer buffer) {
		this(buffer.get(), buffer.get());
		int nbItems = buffer.get();
		while (nbItems>0) {
			items.add(ItemStatus.from(buffer, this));
			nbItems--;
		}
	}

	public void fill(ByteBuffer buffer) {
		buffer.put((byte) i);
		buffer.put((byte) j);
		buffer.put((byte) items.size());
		for (ItemStatus item : items) {
			item.fill(buffer);
		}
	}

	public void add(ItemStatus item) {
		items.add(item);
	}

	public boolean isEmpty() {
		return items.isEmpty();
	}
	
	public List<ItemStatus> getItems() {
		return items;
	}
	
	public int getI() {
		return i;
	}
	
	public int getJ() {
		return j;
	}

}
