package org.peekmoon.bomberman.board;

import org.peekmoon.bomberman.Geometry;
import org.peekmoon.bomberman.Mesh;
import org.peekmoon.bomberman.Texture;
import org.peekmoon.bomberman.network.Direction;
import org.peekmoon.bomberman.shader.ProgramShader;

public class BombItem extends ItemRenderer {
    
    private static final float lifeTime = 4;
    private static final int power = 3;
    
    private static final Texture bombTexture = new Texture("bomb.png");
    private static final Mesh bombMesh = Mesh.get("bomb", bombTexture);

    private final Geometry geometry; 
    private float countdown;
    
    public BombItem(BoardRenderer board, ProgramShader shader, int i, int j) {
        super(shader, board, i, j);
        this.countdown = lifeTime;
        geometry = new Geometry(bombMesh, shader);
        geometry.setPosition(i, j, 0);
    }

    @Override
    public boolean isTraversable() {
        return false;
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
        bombMesh.release();
        bombTexture.release();
        
    }

    @Override
    public boolean update(float elapsed) {
        countdown -= elapsed;
        if (countdown <= 0) {
            getTile().fire();
        }
        return false; // Will be remove when fire
    }

    public boolean fire() {
        for (Direction direction : Direction.values()) {
            TileRenderer currentTile = getTile();
            for (int i=0; i<power; i++) {
                currentTile = currentTile.get(direction);
                currentTile.fire();
                if (!currentTile.isPropagateFire()) break;
            }
        }
        return true;
    }

}
