package org.peekmoon.bomberman.board;

import org.peekmoon.bomberman.model.Game;
import org.peekmoon.bomberman.shader.ProgramShader;

public class GameRenderer {
	
    private BoardRenderer board;
    private PlayerRenderer player;
    
    public GameRenderer(ProgramShader shader) {
    	board = new BoardRenderer(shader);
    	player = new PlayerRenderer(shader);
    }
	
	public void render(Game game) {
		board.render(game.getBoardStatus());
		player.render(game.getPlayerStatus());
	}

	public void release() {
		player.release();
		board.release();
	}

}
