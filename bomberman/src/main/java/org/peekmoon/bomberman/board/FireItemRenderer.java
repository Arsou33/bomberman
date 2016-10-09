package org.peekmoon.bomberman.board;

import org.peekmoon.bomberman.Geometry;
import org.peekmoon.bomberman.Mesh;
import org.peekmoon.bomberman.Texture;
import org.peekmoon.bomberman.network.status.FireItemStatus;
import org.peekmoon.bomberman.shader.ProgramShader;

public class FireItemRenderer extends ItemRenderer<FireItemStatus> {
    
    private final Texture fireTexture = new Texture("test.png");
    private final Mesh fireMesh = Mesh.get("cube", fireTexture);

    private final Geometry geometry; 

    public FireItemRenderer(ProgramShader shader) {
        geometry = new Geometry(fireMesh, shader);
    }

	@Override
	public void render(FireItemStatus item) {
		geometry.setPosition(item.getI(), item.getJ(), 0);
		geometry.render();
	}

    @Override
    public void release() {
        fireMesh.release();
        fireTexture.release();
        
    }

}
