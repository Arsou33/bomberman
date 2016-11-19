package org.peekmoon.bomberman.board;

import org.peekmoon.bomberman.Camera;
import org.peekmoon.bomberman.Stage;
import org.peekmoon.bomberman.board.renderer.GameRenderer;
import org.peekmoon.bomberman.key.KeyManager;
import org.peekmoon.bomberman.model.Game;
import org.peekmoon.bomberman.network.CommandSender;
import org.peekmoon.bomberman.network.StatusReceiver;
import org.peekmoon.bomberman.opengl.GLUtils;
import org.peekmoon.bomberman.shader.BoardShader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BoardStage extends Stage {
    
    private final static Logger log = LoggerFactory.getLogger(BoardStage.class);
    
    private final long window;
    private final CommandSender commandSender;

    private GameRenderer gameRenderer;
    private BoardShader shader;
    private Camera camera;
    private KeyManager keyManager;
    private StatusReceiver statusReceiver;
    
    
    public BoardStage(long window, StatusReceiver statusReceiver, CommandSender commandSender) {
        this.window = window;
        this.commandSender = commandSender;
        this.statusReceiver = statusReceiver;
    }

    @Override
    public void init() {
        shader = new BoardShader();
        gameRenderer = new GameRenderer(shader);
        camera = new Camera(shader);
        keyManager = new BoardKeyManager(window, commandSender, camera);
        commandSender.createGame();
    }

    @Override
    public void render() {
        shader.use();
        camera.update();
        GLUtils.checkError();
        gameRenderer.render(statusReceiver.getStatus(Game.class));
    }

    @Override
    public void release() {
        gameRenderer.release();
    }

    @Override
    public KeyManager getKeyManager() {
        return keyManager;
    }


    @Override
    protected Name getName() {
        return Name.BOARD;
    }

}
