package org.peekmoon.bomberman;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.peekmoon.bomberman.shader.ProgramShader;

public class Board {
    
    // Add ground geometries
    private final static int nbTilesWidth = 21;
    private final static int nbTilesHeight = 15;
    
    private final List<Geometry> geometries;
    private final ProgramShader shader;
    
    private final Mesh groundMesh;
    private final Mesh woodBoxMesh;
    private final Mesh brickBoxMesh;
    
    private final Texture grassTexture;
    private final Texture brickTexture;
    private final Texture woodTexture;

    
    
    public Board(ProgramShader shader) {
        this.shader = shader;
        this.geometries = new ArrayList<>();
        this.brickTexture = new Texture("brick.png");
        this.woodTexture = new Texture("wood-box.png");
        this.grassTexture = new Texture("grass.png");
        
        woodBoxMesh = Mesh.get("cube", woodTexture);
        brickBoxMesh = Mesh.get("cube", brickTexture);
        
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
                    Geometry geometry = new Geometry(brickBoxMesh, shader);
                    geometry.setPosition(i, j, 0);
                    geometries.add(geometry);
                } else if (distance(1,1, i,j)>4 && distance(nbTilesWidth-2, nbTilesHeight-2, i,j)>4 && rand.nextInt(10)<8) {
                    Geometry geometry = new Geometry(woodBoxMesh, shader);
                    geometry.setPosition(i, j, 0);
                    geometries.add(geometry);
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
    }
    
    public void release() {
        woodBoxMesh.release();
        brickBoxMesh.release();
        groundMesh.release();
        brickTexture.release();
        grassTexture.release();
    }

}
