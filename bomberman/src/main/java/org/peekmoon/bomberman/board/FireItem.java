package org.peekmoon.bomberman.board;

import org.peekmoon.bomberman.Geometry;
import org.peekmoon.bomberman.Mesh;
import org.peekmoon.bomberman.Texture;
import org.peekmoon.bomberman.shader.ProgramShader;

public class FireItem extends Item {
    
    private static final float lifeTime = 1.5f;
    
    private static final Texture fireTexture = new Texture("test.png");
    private static final Mesh fireMesh = Mesh.get("cube", fireTexture);

    private final Geometry geometry; 
    private float countdown;
    
    public FireItem(Board board, ProgramShader shader, int i, int j) {
        super(shader, board, i, j);
        this.countdown = lifeTime;
        geometry = new Geometry(fireMesh, shader);
        geometry.setPosition(i, j, 0);
    }

    @Override
    public boolean isTraversable() {
        return true;
    }

    @Override
    public boolean isPropagateFire() {
        return true;
    }

    @Override
    public void render() {
        geometry.render();
    }

    @Override
    public void release() {
        fireMesh.release();
        fireTexture.release();
        
    }

    @Override
    public boolean update(float elapsed) {
        countdown -= elapsed;
        if (countdown <= 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean fire() {
        return false;
    }

}
