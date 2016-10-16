package org.peekmoon.bomberman.board;

import org.peekmoon.bomberman.Geometry;
import org.peekmoon.bomberman.Mesh;
import org.peekmoon.bomberman.model.FireItem;
import org.peekmoon.bomberman.opengl.GLMesh;
import org.peekmoon.bomberman.opengl.GLTexture;
import org.peekmoon.bomberman.shader.ProgramShader;

public class FireItemRenderer extends ItemRenderer<FireItem> {
    
    private final GLTexture fireTexture = new GLTexture("test.png");
    private final GLMesh fireMesh = new GLMesh(Mesh.get("cube"), fireTexture);

    private final Geometry geometry; 

    public FireItemRenderer(ProgramShader shader) {
        geometry = new Geometry(fireMesh, shader);
    }

	@Override
	public void render(FireItem item) {
		geometry.setPosition(item.getI(), item.getJ(), 0);
		geometry.render();
	}

    @Override
    public void release() {
        fireMesh.release();
        fireTexture.release();
        
    }

}
