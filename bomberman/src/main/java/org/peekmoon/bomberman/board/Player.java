package org.peekmoon.bomberman.board;

import org.peekmoon.bomberman.Geometry;
import org.peekmoon.bomberman.Mesh;
import org.peekmoon.bomberman.Texture;
import org.peekmoon.bomberman.shader.ProgramShader;

public class Player {
    
    private static final float speed = 4;
    
    private final Board board;
    private final Geometry geometry;
    private final Mesh cowboyMesh;
    private final Texture cowboyTexture;
    
    private float posX;
    private float posY;

    
    public Player(Board board, ProgramShader shader) {
        this.board = board;
        cowboyTexture = new Texture("cowboy.png");
        cowboyMesh = Mesh.get("cowboy", cowboyTexture);
        geometry = new Geometry(cowboyMesh, shader);
        posX = 1;
        posY = 1;
    }

    public void render() {
        geometry.setPosition(posX, posY, 0);
        geometry.render();
    }
    
    public void release() {
        cowboyMesh.release();
        cowboyTexture.release();
    }

    public void moveUp(float elapsed) {
        float distToMove = elapsed * speed;
        float distToAlign = Math.round(posX) - posX;
        if (distToAlign<0) {
            moveLeft(elapsed);
            return;
        } else if (distToAlign>0) {
            moveRight(elapsed);
            return;
        } else {
            if (board.get(posX, posY).get(Direction.UP).isTraversable()) {
                posY+=distToMove;
            }
        }
        
    }

    public void moveDown(float elapsed) {
        if (posY-elapsed>1) posY -= elapsed;
    }

    public void moveLeft(float elapsed) {
        if (posX-elapsed>1) posX -= elapsed;
    }

    public void moveRight(float elapsed) {
        if (posX-elapsed<5) posX += elapsed;
    }

}