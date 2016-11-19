package org.peekmoon.bomberman.model;

import java.nio.ByteBuffer;
import java.util.Random;

public class Board {

	private final static int nbTilesWidth = 21;
	private final static int nbTilesHeight = 15;

	private final int width;
	private final int height;
	private final Tile[][] tiles;

	private Board(int width, int height) {
		this.width = width;
		this.height = height;
		tiles = new Tile[width][height];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				if (tiles[i][j] == null) {
					tiles[i][j] = new Tile(this, i, j);
				}
			}
		}
	}

	public Board() {
		this(nbTilesWidth, nbTilesHeight);
		// Add brick box to geometry
		Random rand = new Random(0);
		for (int i = 0; i < nbTilesWidth; i++) {
			for (int j = 0; j < nbTilesHeight; j++) {
				if (i == 0 || j == 0 || i == nbTilesWidth - 1 || j == nbTilesHeight - 1 || (i % 2 == 0 && j % 2 == 0)) {
					tiles[i][j].add(new BrickItem(getTile(i, j)));
				} else if (distance(1, 1, i, j) > 4 && distance(nbTilesWidth - 2, nbTilesHeight - 2, i, j) > 4
						&& rand.nextInt(10) < 3) {
					tiles[i][j].add(new WoodItem(getTile(i, j)));
				}
			}
		}
	}

	public Board(ByteBuffer buffer) {
		this(buffer.get(), buffer.get());

		for (int nbTiles = buffer.getInt(); nbTiles > 0; nbTiles--) {
			Tile tile = new Tile(this, buffer);
			tiles[tile.getI()][tile.getJ()] = tile;
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

	public Tile getTile(int i, int j) {
		return tiles[i][j];
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public Tile get(float x, float y) {
		return tiles[Math.round(x)][Math.round(y)];
	}

	private int distance(int i1, int j1, int i2, int j2) {
		return Math.abs(i1 - i2) + Math.abs(j1 - j2);
	}

}
