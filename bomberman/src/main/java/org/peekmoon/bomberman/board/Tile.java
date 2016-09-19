package org.peekmoon.bomberman.board;

import java.util.ArrayList;
import java.util.List;

import org.peekmoon.bomberman.shader.ProgramShader;

public class Tile {
    
    private final ProgramShader shader;
    private final Board board;
    private final int i;
    private final int j;
    
    private boolean startFire;
    
    private final List<Item> items;
    
    public Tile(ProgramShader shader, Board board, int i, int j) {
        this.shader = shader;
        this.board = board;
        this.i = i;
        this.j = j;
        this.startFire = false;
        this.items = new ArrayList<>();
    }
    
    public void add(Item item) {
        items.add(item);
    }
    
    public void remove(Item item) {
        items.remove(item);
    }
    
    
    public boolean isTraversable() {
        return items.stream().allMatch(item -> item.isTraversable());
    }
    
    public void update(float elapsed) {
        items.removeIf(item->item.update(elapsed));
        if (startFire) {
            items.removeIf(item->item.fire());
            add(new FireItem(board, shader, i, j));
            startFire = false;
        }
    }

    public void render() {
        items.stream().forEach(item -> item.render());
    }

    public Tile get(Direction dir) {
        switch (dir) {
        case DOWN:
            return board.get(i, j-1);
        case LEFT:
            return board.get(i-1, j);
        case RIGHT:
            return board.get(i+1, j);
        case UP:
            return board.get(i, j+1);
        default:
            throw new IllegalStateException("Unknown direction " + dir);
        
        }
    }

    public void fire() {
        startFire = true;
    }

    public boolean isPropagateFire() {
        return items.stream().allMatch(item -> item.isPropagateFire());
    }

}
