package org.peekmoon.bomberman.network.status;

import java.nio.ByteBuffer;

public class BoardStatus {
	
    private final int width;
    private final int height;
	private final TileStatus[][] tiles;

    public BoardStatus(int width, int height) {
    	this.width = width;
    	this.height = height;
    	tiles = new TileStatus[width][height];
    	fillNullTiles();
    }
    
	public BoardStatus(ByteBuffer buffer) {
    	this(buffer.get(),buffer.get());
		int nbTiles = buffer.getInt();
		while (nbTiles>0) {
			TileStatus tile = new TileStatus(this, buffer);
			tiles[tile.getI()][tile.getJ()] = tile;
			nbTiles--;
		}
        fillNullTiles();
	}

	public void fill(ByteBuffer buffer) {
		buffer.put((byte) width);
		buffer.put((byte) height);
		int nbTilesPosition = buffer.position();
		buffer.putInt(0); // Temporary value will be replaced later
		int nbTiles = 0;
        for (int i=0; i<width; i++) {
            for (int j=0; j<height; j++) {
            	if (!tiles[i][j].isEmpty()) {
            		nbTiles++;
            		tiles[i][j].fill(buffer); 
            	}
            }
        }
        buffer.putInt(nbTilesPosition, nbTiles);
	}

	private void fillNullTiles() {
		for (int i=0; i<width; i++) {
	        for (int j=0; j<height; j++) {
	        	if (tiles[i][j] == null) {
	        		tiles[i][j] = new TileStatus(this, i,j);
	        	}
	        }
	    }
	}

	public void add(int i, int j, ItemStatus item) {
		tiles[i][j].add(item);
	}
	
	public TileStatus getTile(int i, int j) {
		return tiles[i][j];
	}

	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}

	public TileStatus get(float x, float y) {
		return tiles[Math.round(x)][Math.round(y)];
	}


}
