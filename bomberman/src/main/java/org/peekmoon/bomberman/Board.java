package org.peekmoon.bomberman;

import java.util.ArrayList;
import java.util.List;

import org.peekmoon.bomberman.shader.ProgramShader;

public class Board {
    
    // Add ground geometries
    private final static int nbTilesWidth = 24;
    private final static int nbTilesHeight = 18;
    
    private final List<Geometry> geometries;
    private final ProgramShader shader;
    
    private final Mesh cubeMesh;
    private final Mesh quadMesh;
    
    private final Texture wallTexture;
    private final Texture grassTexture;
    
    
    public Board(ProgramShader shader) {
        this.shader = shader;
        this.geometries = new ArrayList<>();
        this.wallTexture = new Texture("wall.png");
        this.grassTexture = new Texture("grass.png");
        
        cubeMesh = Mesh.get("cube", wallTexture);
        
        quadMesh = Mesh.get(new float[] {
                -0.5f,-0.5f,0, 0,1, 
                0.5f,-0.5f,0,  1,1,
                -0.5f,0.5f,0,  0,0, 
                0.5f,0.5f,0,   1,0 }, new short[] {0,1,2, 1,2,3}, grassTexture);
        
        for (int i=0; i<nbTilesWidth; i++) {
            for (int j=0; j<nbTilesHeight; j++) {
                Geometry ground = new Geometry(quadMesh, shader);
                ground.setPosition(i, j, 0);
                geometries.add(ground);
            }
        }
        
        for (int i=0; i<nbTilesWidth; i+=2) {
            for (int j=0; j<nbTilesHeight; j+=2) {
                Geometry geometry = new Geometry(cubeMesh, shader);
                geometry.setPosition(i, j, -0.2f);
                geometries.add(geometry);
            }
        }
        
    }
    
    public void render() {
        for (Geometry geometry : geometries) {
            geometry.render();
        }
    }
    
    public void release() {
        cubeMesh.release();
        quadMesh.release();
        wallTexture.release();
        grassTexture.release();
    }

}
