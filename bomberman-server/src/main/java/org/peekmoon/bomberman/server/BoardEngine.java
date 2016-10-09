package org.peekmoon.bomberman.server;

import java.util.Random;

import org.peekmoon.bomberman.network.status.BoardStatus;
import org.peekmoon.bomberman.network.status.BrickItemStatus;
import org.peekmoon.bomberman.network.status.WoodItemStatus;

public class BoardEngine {
	
    private final static int nbTilesWidth = 21;
    private final static int nbTilesHeight = 15;
	
	private final BoardStatus status;
	
	public BoardEngine() {
		this.status = new BoardStatus(nbTilesWidth, nbTilesHeight);
		
        // Add brick box to geometry
        Random rand = new Random(0);
        for (int i=0; i<nbTilesWidth; i++) {
            for (int j=0; j<nbTilesHeight; j++) {
                if (i==0 || j==0 || i==nbTilesWidth-1 || j==nbTilesHeight-1 || (i%2==0 && j%2==0)) {
                    status.add(i,j, new BrickItemStatus(status.getTile(i, j)));
                } else if (distance(1,1, i,j)>4 && distance(nbTilesWidth-2, nbTilesHeight-2, i,j)>4 && rand.nextInt(10)<3) {
                    status.add(i,j, new WoodItemStatus(status.getTile(i, j)));
                }
            }
        }
	}
	
   private int distance(int i1, int j1, int i2, int j2) {
        return Math.abs(i1-i2) + Math.abs(j1-j2);
    }

	public BoardStatus getStatus() {
		return status;
	}

	public void update() {
        for (int i=0; i<nbTilesWidth; i++) {
            for (int j=0; j<nbTilesHeight; j++) {
            	status.getTile(i,j).update();
            }
        }
	}
	
	

}
