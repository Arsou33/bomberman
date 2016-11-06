package org.peekmoon.bomberman.board.renderer;

import org.peekmoon.bomberman.model.Tile;
import org.peekmoon.bomberman.shader.BoardShader;

public class TileRenderer {
	
	private final ItemRenderers itemRenderers;
    
    public TileRenderer(BoardShader shader) {
    	itemRenderers = new ItemRenderers(shader);
    }
    
    public void render(Tile status) {
        status.getItems().stream().forEach(item -> itemRenderers.render(item));
    }
    
    
}
