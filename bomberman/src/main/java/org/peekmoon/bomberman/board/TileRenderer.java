package org.peekmoon.bomberman.board;

import org.peekmoon.bomberman.model.Tile;
import org.peekmoon.bomberman.shader.BombermanShader;

public class TileRenderer {
	
	private final ItemRenderers itemRenderers;
    
    public TileRenderer(BombermanShader shader) {
    	itemRenderers = new ItemRenderers(shader);
    }
    
    public void render(Tile status) {
        status.getItems().stream().forEach(item -> itemRenderers.render(item));
    }
    
    
}
