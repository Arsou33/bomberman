package org.peekmoon.bomberman.board;

import org.peekmoon.bomberman.Geometry;
import org.peekmoon.bomberman.Mesh;
import org.peekmoon.bomberman.Texture;
import org.peekmoon.bomberman.shader.ProgramShader;

public class BrickBoxItem extends Item {
    
    private static final Texture brickTexture = new Texture("brick.png");
    private static final Mesh brickBoxMesh = Mesh.get("cube", brickTexture);

    private final Geometry geometry; 
    
    
    public BrickBoxItem(ProgramShader shader, int i, int j) {
        geometry = new Geometry(brickBoxMesh, shader);
        geometry.setPosition(i, j, 0);
    }

    @Override
    public boolean isTraversable() {
        return false;
    }

    @Override
    public void render() {
        geometry.render();
    }

    @Override
    public void release() {
        brickBoxMesh.release();
        brickTexture.release();
    }
    

}
