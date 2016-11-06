package org.peekmoon.bomberman.board.renderer;

import org.peekmoon.bomberman.Geometry;
import org.peekmoon.bomberman.model.Board;
import org.peekmoon.bomberman.opengl.GLIndexedMesh;
import org.peekmoon.bomberman.opengl.GLTexture;
import org.peekmoon.bomberman.shader.BoardShader;

public class BoardRenderer {

    private final TileRenderer tileRenderer;
    private final GLIndexedMesh groundMesh;
    private final GLTexture grassTexture;
    private final Geometry ground;

    
    public BoardRenderer(BoardShader shader) {
        this.grassTexture = new GLTexture("grass.png");
        
        groundMesh = new GLIndexedMesh(new float[] {
                -0.5f,-0.5f,0, 0,1, 
                0.5f,-0.5f,0,  1,1,
                -0.5f,0.5f,0,  0,0, 
                0.5f,0.5f,0,   1,0
            }, 
            new short[] {0,1,2, 1,2,3},
            grassTexture,
            3, 2
        );
        
        ground = new Geometry(groundMesh, shader);
        tileRenderer = new TileRenderer(shader);
    }
    
    public void render(Board status) {
        for (int i=0; i<status.getWidth(); i++) {
            for (int j=0; j<status.getHeight(); j++) {
                ground.setPosition(i, j, 0);
                ground.render();
                tileRenderer.render(status.getTile(i,j));
            }
        }
    }
    
    public void release() {
        // TODO : release woodboxitem and brickboxitem
        groundMesh.release();
        grassTexture.release();
    }

    public TileRenderer get(float posX, float posY) {
        return get(Math.round(posX), Math.round(posY));
    }
    

}
