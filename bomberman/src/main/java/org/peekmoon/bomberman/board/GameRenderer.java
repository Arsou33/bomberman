package org.peekmoon.bomberman.board;

import org.peekmoon.bomberman.model.Game;
import org.peekmoon.bomberman.shader.BombermanShader;

public class GameRenderer {
	
    private BoardRenderer boardRenderer;
    private PlayerRenderer playerRenderer;
    
    public GameRenderer(BombermanShader shader) {
    	boardRenderer = new BoardRenderer(shader);
    	playerRenderer = new PlayerRenderer(shader);
    }
	
	public void render(Game game) {
		boardRenderer.render(game.getBoardStatus());
		game.getPlayers().forEach(player->playerRenderer.render(player));
	}

	public void release() {
		playerRenderer.release();
		boardRenderer.release();
	}

}
