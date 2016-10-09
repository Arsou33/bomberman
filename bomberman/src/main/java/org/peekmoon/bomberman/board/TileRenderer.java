package org.peekmoon.bomberman.board;

import org.peekmoon.bomberman.network.status.TileStatus;
import org.peekmoon.bomberman.shader.ProgramShader;

public class TileRenderer {
	
	private final ItemRenderers itemRenderers;
    
    public TileRenderer(ProgramShader shader) {
    	itemRenderers = new ItemRenderers(shader);
    }
    
    public void render(TileStatus status) {
        status.getItems().stream().forEach(item -> itemRenderers.render(item));
    }
    
    
}
