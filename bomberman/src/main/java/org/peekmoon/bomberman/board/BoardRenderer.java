package org.peekmoon.bomberman.board;

import org.peekmoon.bomberman.Geometry;
import org.peekmoon.bomberman.Mesh;
import org.peekmoon.bomberman.Texture;
import org.peekmoon.bomberman.model.Board;
import org.peekmoon.bomberman.shader.ProgramShader;

public class BoardRenderer {
	
	private final TileRenderer tileRenderer;
    private final Mesh groundMesh;
    private final Texture grassTexture;
    private final Geometry ground;

    
    public BoardRenderer(ProgramShader shader) {
        this.grassTexture = new Texture("grass.png");
        
        groundMesh = Mesh.get(new float[] {
	                -0.5f,-0.5f,0, 0,1, 
	                0.5f,-0.5f,0,  1,1,
	                -0.5f,0.5f,0,  0,0, 
	                0.5f,0.5f,0,   1,0 
	            }, 
        		new short[] {0,1,2, 1,2,3},
        		grassTexture);
        
        ground = new Geometry(groundMesh, shader);
        this.tileRenderer = new TileRenderer(shader);
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
