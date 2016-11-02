package org.peekmoon.bomberman.board.renderer;

import org.peekmoon.bomberman.model.Game;
import org.peekmoon.bomberman.shader.BoardShader;

public class GameRenderer {

    private BoardRenderer boardRenderer;
    private PlayerRenderer playerRenderer;

    public GameRenderer(BoardShader shader) {
        boardRenderer = new BoardRenderer(shader);
        playerRenderer = new PlayerRenderer(shader);
    }

    public void render(Game game) {
        boardRenderer.render(game.getBoardStatus());
        game.getPlayers().forEach(player -> playerRenderer.render(player));
    }

    public void release() {
        playerRenderer.release();
        boardRenderer.release();
    }

}
