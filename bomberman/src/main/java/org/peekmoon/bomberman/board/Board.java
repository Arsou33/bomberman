package org.peekmoon.bomberman.board;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.peekmoon.bomberman.Geometry;
import org.peekmoon.bomberman.Mesh;
import org.peekmoon.bomberman.Texture;
import org.peekmoon.bomberman.shader.ProgramShader;

public class Board {
    
    // Add ground geometries
    private final static int nbTilesWidth = 21;
    private final static int nbTilesHeight = 15;
    
    
    private final Tile[][] tiles;
    
    private final List<Geometry> geometries;
    private final ProgramShader shader;
    
    private final Mesh groundMesh;
    private final Texture grassTexture;

    
    
    public Board(ProgramShader shader) {
        this.tiles = new Tile[nbTilesWidth][nbTilesHeight];
        for (int i=0; i<nbTilesWidth; i++) {
            for (int j=0; j<nbTilesHeight; j++) {
               tiles[i][j] = new Tile(this,i,j); 
            }
        }
        
        this.shader = shader;
        this.geometries = new ArrayList<>();
        this.grassTexture = new Texture("grass.png");
        
        groundMesh = Mesh.get(new float[] {
                -0.5f,-0.5f,0, 0,1, 
                0.5f,-0.5f,0,  1,1,
                -0.5f,0.5f,0,  0,0, 
                0.5f,0.5f,0,   1,0 }, new short[] {0,1,2, 1,2,3}, grassTexture);
        
        // Add geometry for ground
        for (int i=0; i<nbTilesWidth; i++) {
            for (int j=0; j<nbTilesHeight; j++) {
                Geometry ground = new Geometry(groundMesh, shader);
                ground.setPosition(i, j, 0);
                geometries.add(ground);
            }
        }
        
        // Add brick box to geometry
        Random rand = new Random(0);
        for (int i=0; i<nbTilesWidth; i++) {
            for (int j=0; j<nbTilesHeight; j++) {
                if (i==0 || j==0 || i==nbTilesWidth-1 || j==nbTilesHeight-1 || (i%2==0 && j%2==0)) {
                    tiles[i][j].addItem(new BrickBoxItem(shader, i, j));
                } else if (distance(1,1, i,j)>4 && distance(nbTilesWidth-2, nbTilesHeight-2, i,j)>4 && rand.nextInt(10)<3) {
                    tiles[i][j].addItem(new WoodBoxItem(shader, i, j));
                }
            }
        }
        
    }
    
    private int distance(int i1, int j1, int i2, int j2) {
        return Math.abs(i1-i2) + Math.abs(j1-j2);
    }

    public void render() {
        for (Geometry geometry : geometries) {
            geometry.render();
        }
        for (int i=0; i<nbTilesWidth; i++) {
            for (int j=0; j<nbTilesHeight; j++) {
                tiles[i][j].render();
            }
        }
    }
    
    public void release() {
        // TODO : release woodboxitem and brickboxitem
        groundMesh.release();
        grassTexture.release();
    }

    public Tile get(float posX, float posY) {
        return get(Math.round(posX), Math.round(posY));
    }
    
    
    public Tile get(int i, int j) {
        return tiles[i][j];
    }

}
