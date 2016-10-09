package org.peekmoon.bomberman.network.status;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.peekmoon.bomberman.network.Direction;

public class TileStatus {
	
    private final BoardStatus board;
	private final int i,j;
    private final List<ItemStatus> items;
    
    private boolean startFire;

    
    public TileStatus(BoardStatus board, int i, int j) {
    	this.board = board;
    	this.i = i;
    	this.j = j;
    	this.items = new ArrayList<>();
    	this.startFire = false;
    }

	public TileStatus(BoardStatus board, ByteBuffer buffer) {
		this(board, buffer.get(), buffer.get());
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
	
    public boolean isTraversable() {
        return items.stream().allMatch(item -> item.isTraversable());
    }

    public boolean isPropagateFire() {
	    return items.stream().allMatch(item -> item.isPropagateFire());
	}

	/**
     * A bomb can be drop only if nothing else is on the tile
     */
    public boolean canDropBomb() {
        return items.isEmpty();
    }

    public void dropBomb() {
        if (!items.isEmpty()) {
            String msg = "Items is not empty and contains : " + items.stream().map(item->item.toString()).collect(Collectors.joining("|"));
            throw new IllegalStateException(msg);
        }
        add(new BombItemStatus(this));
    }
    
    public void fire() {
        startFire = true;
    }
    
    public void update() {
        items.removeIf(item->item.update());
        if (startFire) {
            items.removeIf(item->item.fire());
            add(new FireItemStatus(this));
            startFire = false;
        }
    }

	public TileStatus get(Direction dir) {
	    switch (dir) {
	    case DOWN:
	        return board.get(i, j-1);
	    case LEFT:
	        return board.get(i-1, j);
	    case RIGHT:
	        return board.get(i+1, j);
	    case UP:
	        return board.get(i, j+1);
	    default:
	        throw new IllegalStateException("Unknown direction " + dir);
	        
	    }
	}

	public int getI() {
		return i;
	}
	
	public int getJ() {
		return j;
	}
}
