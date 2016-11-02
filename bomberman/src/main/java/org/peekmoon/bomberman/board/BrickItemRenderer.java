package org.peekmoon.bomberman.board;

import org.peekmoon.bomberman.Geometry;
import org.peekmoon.bomberman.Mesh;
import org.peekmoon.bomberman.model.BrickItem;
import org.peekmoon.bomberman.opengl.GLIndexedMesh;
import org.peekmoon.bomberman.opengl.GLMesh;
import org.peekmoon.bomberman.opengl.GLTexture;
import org.peekmoon.bomberman.shader.BombermanShader;

public class BrickItemRenderer extends ItemRenderer<BrickItem> {
    
    private final GLTexture brickTexture = new GLTexture("brick.png");
    private final GLMesh brickBoxMesh = new GLIndexedMesh(Mesh.get("cube"), brickTexture);

    private final Geometry geometry; 
    
    
    public BrickItemRenderer(BombermanShader shader) {
        geometry = new Geometry(brickBoxMesh, shader);
    }
    
	@Override
	public void render(BrickItem item) {
		geometry.setPosition(item.getI(), item.getJ(), 0);
		geometry.render();
	}
	
    public void release() {
        brickBoxMesh.release();
        brickTexture.release();
    }

}
