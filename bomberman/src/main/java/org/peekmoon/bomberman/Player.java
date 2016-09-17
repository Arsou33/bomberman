package org.peekmoon.bomberman;

import org.peekmoon.bomberman.shader.ProgramShader;

public class Player {
    
    private final static float speed = 0.1f;
    
    private final Geometry geometry;
    private final Mesh cowboyMesh;
    private final Texture cowboyTexture;

    
    public Player(ProgramShader shader) {
        cowboyTexture = new Texture("cowboy.png");
        cowboyMesh = Mesh.get("cowboy", cowboyTexture);
        geometry = new Geometry(cowboyMesh, shader);
        geometry.setPosition(1, 1, 0);
    }

    public void render() {
        geometry.render();
    }
    
    public void release() {
        cowboyMesh.release();
        cowboyTexture.release();
    }

    public void moveUp() {
        geometry.translate(0, speed, 0);
    }

    public void moveDown() {
        geometry.translate(0, -speed,0);
    }

    public void moveLeft() {
        geometry.translate(-speed, 0,0);
    }

    public void moveRight() {
        geometry.translate(speed, 0,0);
    }

}