package org.peekmoon.bomberman.board;

import java.util.ArrayList;
import java.util.List;

public class Tile {
    
    private final Board board;
    private final int i;
    private final int j;
    
    private final List<Item> items;
    
    public Tile(Board board, int i, int j) {
        this.board = board;
        this.i = i;
        this.j = j;
        this.items = new ArrayList<>();
    }
    
    public void addItem(Item item) {
        items.add(item);
    }
    
    
    public boolean isTraversable() {
        return items.stream().allMatch(item -> item.isTraversable());
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

}
