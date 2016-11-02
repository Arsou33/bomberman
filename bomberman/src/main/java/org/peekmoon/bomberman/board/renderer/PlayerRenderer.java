package org.peekmoon.bomberman.board.renderer;

import org.peekmoon.bomberman.Geometry;
import org.peekmoon.bomberman.Mesh;
import org.peekmoon.bomberman.model.Player;
import org.peekmoon.bomberman.opengl.GLIndexedMesh;
import org.peekmoon.bomberman.opengl.GLMesh;
import org.peekmoon.bomberman.opengl.GLTexture;
import org.peekmoon.bomberman.shader.BoardShader;

public class PlayerRenderer {
    
    private final Geometry geometry;
    private final GLMesh cowboyMesh;
    private final GLTexture cowboyTexture;
    
    public PlayerRenderer(BoardShader shader) {
        cowboyTexture = new GLTexture("cowboy.png");
        cowboyMesh = new GLIndexedMesh(Mesh.get("cowboy"), cowboyTexture);
        geometry = new Geometry(cowboyMesh, shader);
    }
    
    public void render(Player status) {
        geometry.setPosition(status.getX(), status.getY(), 0);
        geometry.render();
    }
    
    public void release() {
        cowboyMesh.release();
        cowboyTexture.release();
    }
    




}