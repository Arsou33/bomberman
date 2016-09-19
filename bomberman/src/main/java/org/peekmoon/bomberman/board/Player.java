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
    
    private final ProgramShader shader;
    
    private float posX;
    private float posY;

    
    public Player(Board board, ProgramShader shader) {
        this.board = board;
        this.shader = shader;
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
        if (Math.ceil(posY) != Math.round(posY)) { // We are entering a cell
            Tile destTile = board.get(posX, (float)Math.floor(posY)).get(Direction.UP);
            if (!destTile.isTraversable()) return;
        }
        
        float distToMove = elapsed * speed;
        float deltaToAlign = Math.round(posX) - posX;

        if (Math.abs(deltaToAlign) < distToMove) {
            posX = Math.round(posX);
            distToMove -= Math.abs(deltaToAlign);
        } else {
            posX += distToMove * Math.signum(deltaToAlign);
            return;
        }
        posY+=distToMove;
    }

    public void moveDown(float elapsed) {
        if (Math.floor(posY) != Math.round(posY)) { // We are entering a cell
            Tile destTile = board.get(posX, (float)Math.ceil(posY)).get(Direction.DOWN);
            if (!destTile.isTraversable()) return;
        }
        
        float distToMove = elapsed * speed;
        float deltaToAlign = Math.round(posX) - posX;

        if (Math.abs(deltaToAlign) < distToMove) {
            posX = Math.round(posX);
            distToMove -= Math.abs(deltaToAlign);
        } else {
            posX += distToMove * Math.signum(deltaToAlign);
            return;
        }
        posY-=distToMove;
    }

    public void moveLeft(float elapsed) {
        if (Math.floor(posX) != Math.round(posX)) { // We are entering a cell
            Tile destTile = board.get((float)Math.ceil(posX), posY).get(Direction.LEFT);
            if (!destTile.isTraversable()) return;
        }
        
        float distToMove = elapsed * speed;
        float deltaToAlign = Math.round(posY) - posY;

        if (Math.abs(deltaToAlign) < distToMove) {
            posY = Math.round(posY);
            distToMove -= Math.abs(deltaToAlign);
        } else {
            posY += distToMove * Math.signum(deltaToAlign);
            return;
        }
        posX-=distToMove;
    }

    public void moveRight(float elapsed) {
        if (Math.ceil(posX) != Math.round(posX)) { // We are entering a cell
            Tile destTile = board.get((float)Math.floor(posX), posY).get(Direction.RIGHT);
            if (!destTile.isTraversable()) return;
        }        
        float distToMove = elapsed * speed;
        float deltaToAlign = Math.round(posY) - posY;

        if (Math.abs(deltaToAlign) < distToMove) {
            posY = Math.round(posY);
            distToMove -= Math.abs(deltaToAlign);
        } else {
            posY += distToMove * Math.signum(deltaToAlign);
            return;
        }
        posX+=distToMove;
    }

    public void dropBomb() {
        board.get(posX, posY).add(new BombItem(board, shader, Math.round(posX), Math.round(posY)));
    }

}