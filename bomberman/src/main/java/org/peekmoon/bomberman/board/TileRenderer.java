package org.peekmoon.bomberman.board;

import org.peekmoon.bomberman.network.status.TileStatus;
import org.peekmoon.bomberman.shader.ProgramShader;

public class TileRenderer {
	
	private final ItemRenderers itemRenderers;
    
    //private final ProgramShader shader;
    //private final BoardRenderer board;
    //private final int i;
    //private final int j;
    
    //private boolean startFire;
    
    //private final List<ItemRenderer> items;
    
	/*
    public TileRenderer(ProgramShader shader, BoardRenderer board, int i, int j) {
        this.shader = shader;
        this.board = board;
        this.i = i;
        this.j = j;
        this.startFire = false;
        this.items = new ArrayList<>();
    }
    */
    
    public TileRenderer(ProgramShader shader) {
    	itemRenderers = new ItemRenderers(shader);
    }
    
    public void render(TileStatus status) {
        status.getItems().stream().forEach(item -> itemRenderers.render(item));
    }
    
    /*

    public void add(ItemRenderer item) {
        items.add(item);
    }
    
    public void fire() {
        startFire = true;
    }
    */


    /**
     * A bomb can be drop only if nothing else is on the tile
     * @return
     */
    /*
    public boolean canDropBomb() {
        return items.isEmpty();
    }

    public void dropBomb() {
        if (!items.isEmpty()) {
            String msg = "Items is not empty and contains : " + items.stream().map(item->item.toString()).collect(Collectors.joining("|"));
            throw new IllegalStateException(msg);
        }
        add(new BombItem(board, shader, i, j));
        
    }


    
    public boolean isPropagateFire() {
        return items.stream().allMatch(item -> item.isPropagateFire());
    }

    public void update(float elapsed) {
        items.removeIf(item->item.update(elapsed));
        if (startFire) {
            items.removeIf(item->item.fire());
            add(new FireItem(board, shader, i, j));
            startFire = false;
        }
    }
    */

}
