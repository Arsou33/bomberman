package org.peekmoon.bomberman.board;

import org.peekmoon.bomberman.Geometry;
import org.peekmoon.bomberman.Mesh;
import org.peekmoon.bomberman.Texture;
import org.peekmoon.bomberman.network.status.BombItemStatus;
import org.peekmoon.bomberman.shader.ProgramShader;

public class BombItemRenderer extends ItemRenderer<BombItemStatus> {
	
    private static final Texture bombTexture = new Texture("bomb.png");
    private static final Mesh bombMesh = Mesh.get("bomb", bombTexture);

    private final Geometry geometry; 
    
    public BombItemRenderer(ProgramShader shader) {
        geometry = new Geometry(bombMesh, shader);
    }
    
	@Override
	public void render(BombItemStatus item) {
		geometry.setPosition(item.getI(), item.getJ(), 0);
		geometry.render();
	}
	

  //TODO : reimplement release
 /*
     @Override
    public void release() {
        bombMesh.release();
        bombTexture.release();
        
    }


*/

}
