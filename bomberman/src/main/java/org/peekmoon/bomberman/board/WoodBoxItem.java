package org.peekmoon.bomberman.board;

import org.peekmoon.bomberman.Geometry;
import org.peekmoon.bomberman.Mesh;
import org.peekmoon.bomberman.Texture;
import org.peekmoon.bomberman.shader.ProgramShader;

public class WoodBoxItem extends Item {
    
    private static final Texture woodTexture = new Texture("wood-box.png");
    private static final Mesh woodBoxMesh = Mesh.get("cube", woodTexture);
    
    private final Geometry geometry; 

    public WoodBoxItem(Board board, ProgramShader shader, int i, int j) {
        super(shader, board, i, j);
        geometry = new Geometry(woodBoxMesh, shader);
        geometry.setPosition(i, j, 0);
    }

    @Override
    public boolean isTraversable() {
        return false;
    }

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
        woodBoxMesh.release();
        woodTexture.release();
    }

    @Override
    public boolean update(float elapsed) {
        return false;
    }

    @Override
    public boolean fire() {
        return true;
    }

}
