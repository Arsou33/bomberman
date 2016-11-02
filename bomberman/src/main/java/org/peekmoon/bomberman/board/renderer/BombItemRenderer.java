package org.peekmoon.bomberman.board.renderer;

import org.peekmoon.bomberman.Geometry;
import org.peekmoon.bomberman.Mesh;
import org.peekmoon.bomberman.model.BombItem;
import org.peekmoon.bomberman.opengl.GLIndexedMesh;
import org.peekmoon.bomberman.opengl.GLMesh;
import org.peekmoon.bomberman.opengl.GLTexture;
import org.peekmoon.bomberman.shader.BoardShader;

public class BombItemRenderer extends ItemRenderer<BombItem> {

    private final GLTexture bombTexture = new GLTexture("bomb.png");
    private final GLMesh bombMesh = new GLIndexedMesh(Mesh.get("bomb"), bombTexture);

    private final Geometry geometry;

    public BombItemRenderer(BoardShader shader) {
        geometry = new Geometry(bombMesh, shader);
    }

    @Override
    public void render(BombItem item) {
        geometry.setPosition(item.getI(), item.getJ(), 0);
        geometry.render();
    }

    @Override
    public void release() {
        bombMesh.release();
        bombTexture.release();
    }

}
