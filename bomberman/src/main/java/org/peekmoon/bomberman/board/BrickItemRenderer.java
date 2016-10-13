package org.peekmoon.bomberman.board;

import org.peekmoon.bomberman.Geometry;
import org.peekmoon.bomberman.Mesh;
import org.peekmoon.bomberman.Texture;
import org.peekmoon.bomberman.model.BrickItem;
import org.peekmoon.bomberman.shader.ProgramShader;

public class BrickItemRenderer extends ItemRenderer<BrickItem> {
    
    private final Texture brickTexture = new Texture("brick.png");
    private final Mesh brickBoxMesh = Mesh.get("cube", brickTexture);

    private final Geometry geometry; 
    
    
    public BrickItemRenderer(ProgramShader shader) {
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
