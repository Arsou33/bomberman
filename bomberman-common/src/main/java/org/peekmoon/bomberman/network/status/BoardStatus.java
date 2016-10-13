package org.peekmoon.bomberman.network.status;

import java.nio.ByteBuffer;
import java.util.Random;

public class BoardStatus {

	private final static int nbTilesWidth = 21;
	private final static int nbTilesHeight = 15;

	private final int width;
	private final int height;
	private final TileStatus[][] tiles;

	private BoardStatus(int width, int height) {
		this.width = width;
		this.height = height;
		tiles = new TileStatus[width][height];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				if (tiles[i][j] == null) {
					tiles[i][j] = new TileStatus(this, i, j);
				}
			}
		}
	}

	public BoardStatus() {
		this(nbTilesWidth, nbTilesHeight);
		// Add brick box to geometry
		Random rand = new Random(0);
		for (int i = 0; i < nbTilesWidth; i++) {
			for (int j = 0; j < nbTilesHeight; j++) {
				if (i == 0 || j == 0 || i == nbTilesWidth - 1 || j == nbTilesHeight - 1 || (i % 2 == 0 && j % 2 == 0)) {
					tiles[i][j].add(new BrickItemStatus(getTile(i, j)));
				} else if (distance(1, 1, i, j) > 4 && distance(nbTilesWidth - 2, nbTilesHeight - 2, i, j) > 4
						&& rand.nextInt(10) < 3) {
					tiles[i][j].add(new WoodItemStatus(getTile(i, j)));
				}
			}
		}
	}

	public BoardStatus(ByteBuffer buffer) {
		this(buffer.get(), buffer.get());

		int nbTiles = buffer.getInt();
		while (nbTiles > 0) {
			TileStatus tile = new TileStatus(this, buffer);
			tiles[tile.getI()][tile.getJ()] = tile;
			nbTiles--;
		}

	}

	public void fill(ByteBuffer buffer) {
		buffer.put((byte) width);
		buffer.put((byte) height);
		int nbTilesPosition = buffer.position();
		buffer.putInt(0); // Temporary value will be replaced later
		int nbTiles = 0;
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				if (!tiles[i][j].isEmpty()) {
					nbTiles++;
					tiles[i][j].fill(buffer);
				}
			}
		}
		buffer.putInt(nbTilesPosition, nbTiles);
	}

	public void update() {
		for (int i = 0; i < nbTilesWidth; i++) {
			for (int j = 0; j < nbTilesHeight; j++) {
				getTile(i, j).update();
			}
		}
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

	private int distance(int i1, int j1, int i2, int j2) {
		return Math.abs(i1 - i2) + Math.abs(j1 - j2);
	}

}
