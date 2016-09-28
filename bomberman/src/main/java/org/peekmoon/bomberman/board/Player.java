package org.peekmoon.bomberman.board;

import org.peekmoon.bomberman.Geometry;
import org.peekmoon.bomberman.Mesh;
import org.peekmoon.bomberman.Texture;
import org.peekmoon.bomberman.network.PlayerStatus;
import org.peekmoon.bomberman.shader.ProgramShader;

public class Player {
    
    private static final float speed = 4;
    
    private final Board board;
    private final Geometry geometry;
    private final Mesh cowboyMesh;
    private final Texture cowboyTexture;
    
    private final ProgramShader shader;
    
    private PlayerStatus status;

    
    public Player(Board board, ProgramShader shader) {
        this.board = board;
        this.shader = shader;
        cowboyTexture = new Texture("cowboy.png");
        cowboyMesh = Mesh.get("cowboy", cowboyTexture);
        geometry = new Geometry(cowboyMesh, shader);
    }
    
    public void updateStatus(PlayerStatus status) {
        this.status = status;
    }

    public void render() {
        geometry.setPosition(status.getX(), status.getY(), 0);
        geometry.render();
    }
    
    public void release() {
        cowboyMesh.release();
        cowboyTexture.release();
    }
    




}