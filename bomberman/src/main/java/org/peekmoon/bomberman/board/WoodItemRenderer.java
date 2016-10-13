package org.peekmoon.bomberman.board;

import org.peekmoon.bomberman.Geometry;
import org.peekmoon.bomberman.Mesh;
import org.peekmoon.bomberman.Texture;
import org.peekmoon.bomberman.model.WoodItem;
import org.peekmoon.bomberman.shader.ProgramShader;

public class WoodItemRenderer extends ItemRenderer<WoodItem> {
    
    private static final Texture woodTexture = new Texture("wood-box.png");
    private static final Mesh woodBoxMesh = Mesh.get("cube", woodTexture);
    
    private final Geometry geometry; 

    public WoodItemRenderer(ProgramShader shader) {
        geometry = new Geometry(woodBoxMesh, shader);
    }

	@Override
	public void render(WoodItem itemStatus) {
		geometry.setPosition(itemStatus.getI(), itemStatus.getJ(), 0);
        geometry.render();
	}

    @Override
    public void release() {
        woodBoxMesh.release();
        woodTexture.release();
    }

}
