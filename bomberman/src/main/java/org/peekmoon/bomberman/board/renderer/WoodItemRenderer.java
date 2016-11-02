package org.peekmoon.bomberman.board.renderer;

import org.peekmoon.bomberman.Geometry;
import org.peekmoon.bomberman.Mesh;
import org.peekmoon.bomberman.model.WoodItem;
import org.peekmoon.bomberman.opengl.GLIndexedMesh;
import org.peekmoon.bomberman.opengl.GLMesh;
import org.peekmoon.bomberman.opengl.GLTexture;
import org.peekmoon.bomberman.shader.BoardShader;

public class WoodItemRenderer extends ItemRenderer<WoodItem> {

    private static final GLTexture woodTexture = new GLTexture("wood-box.png");
    private static final GLMesh woodBoxMesh = new GLIndexedMesh(Mesh.get("cube"), woodTexture);

    private final Geometry geometry;

    public WoodItemRenderer(BoardShader shader) {
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
