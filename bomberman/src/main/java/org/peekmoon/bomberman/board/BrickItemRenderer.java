package org.peekmoon.bomberman.board;

import org.peekmoon.bomberman.Geometry;
import org.peekmoon.bomberman.Mesh;
import org.peekmoon.bomberman.Texture;
import org.peekmoon.bomberman.network.status.BrickItemStatus;
import org.peekmoon.bomberman.shader.ProgramShader;

public class BrickItemRenderer extends ItemRenderer<BrickItemStatus> {
    
    private static final Texture brickTexture = new Texture("brick.png");
    private static final Mesh brickBoxMesh = Mesh.get("cube", brickTexture);

    private final Geometry geometry; 
    
    
    public BrickItemRenderer(ProgramShader shader) {
        geometry = new Geometry(brickBoxMesh, shader);
    }
    
	@Override
	public void render(BrickItemStatus item) {
		geometry.setPosition(item.getI(), item.getJ(), 0);
		geometry.render();
	}

	/*

    @Override
    public boolean isPropagateFire() {
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

    @Override
    public boolean update(float elapsed) {
        return false; 
    }

    @Override
    public boolean fire() {
        return false;
    }
    */


    

}
