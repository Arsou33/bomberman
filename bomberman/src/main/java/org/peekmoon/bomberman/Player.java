package org.peekmoon.bomberman;

import org.peekmoon.bomberman.shader.ProgramShader;

public class Player {
    
    private final Geometry geometry;
    private final Mesh cowboyMesh;
    private final Texture cowboyTexture;

    
    public Player(ProgramShader shader) {
        cowboyTexture = new Texture("cowboy.png");
        cowboyMesh = Mesh.get("cowboy", cowboyTexture);
        geometry = new Geometry(cowboyMesh, shader);
    }

    public void render() {
        geometry.setPosition(7, 5, 0);
        geometry.render();
    }
    
    public void release() {
        cowboyMesh.release();
        cowboyTexture.release();
    }

}
