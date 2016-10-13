package org.peekmoon.bomberman.board;

import org.peekmoon.bomberman.model.Tile;
import org.peekmoon.bomberman.shader.ProgramShader;

public class TileRenderer {
	
	private final ItemRenderers itemRenderers;
    
    public TileRenderer(ProgramShader shader) {
    	itemRenderers = new ItemRenderers(shader);
    }
    
    public void render(Tile status) {
        status.getItems().stream().forEach(item -> itemRenderers.render(item));
    }
    
    
}
