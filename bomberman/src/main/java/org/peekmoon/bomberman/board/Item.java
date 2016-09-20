package org.peekmoon.bomberman.board;

import org.peekmoon.bomberman.Main;
import org.peekmoon.bomberman.shader.ProgramShader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Item {
    
    private final static Logger log = LoggerFactory.getLogger(Main.class);
    
    private final ProgramShader shader;
    private final Board board;
    private final Tile tile;
    private final int i,j;
    
    public abstract boolean isTraversable();
    public abstract boolean isPropagateFire();
    public abstract boolean fire();
    public abstract boolean update(float elapsed);
    public abstract void render();
    public abstract void release();

    public Item(ProgramShader shader, Board board, int i, int j) {
        log.debug("Creating new Item of type {} at position {},{}", this.getClass().getSimpleName(), i,j);
        this.shader = shader;
        this.board = board;
        this.i = i;
        this.j = j;
        this.tile = board.get(i, j);
    }
    
    public ProgramShader getShader() {
        return shader;
    }
    
    public Board getBoard() {
        return board;
    }
    
    public Tile getTile() {
        return tile;
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " [i=" + i + ", j=" + j + "]";
    }
    
    
    
    

}
