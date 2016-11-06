package org.peekmoon.bomberman.board;

import java.net.DatagramSocket;

import org.peekmoon.bomberman.Camera;
import org.peekmoon.bomberman.Stage;
import org.peekmoon.bomberman.board.renderer.GameRenderer;
import org.peekmoon.bomberman.key.KeyManager;
import org.peekmoon.bomberman.network.CommandSender;
import org.peekmoon.bomberman.network.StatusReceiver;
import org.peekmoon.bomberman.opengl.GLUtils;
import org.peekmoon.bomberman.shader.BoardShader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BoardStage extends Stage {
    
    private final static Logger log = LoggerFactory.getLogger(BoardStage.class);
    
    private final long window;

    private GameRenderer gameRenderer;
    private BoardShader shader;
    private Camera camera;
    private KeyManager keyManager;
    private StatusReceiver statusReceiver;
    private Thread statusReceiverThread;
    private DatagramSocket socket;
    
    private String server;
    private int port;
    
    public BoardStage(long window, DatagramSocket socket, String server, int port) {
        this.window = window;
        this.server = server;
        this.port = port;
        this.socket = socket;
    }


    @Override
    public void init() {
        shader = new BoardShader();
        gameRenderer = new GameRenderer(shader);
        
        CommandSender commandSender = new CommandSender(socket, server, port);
        commandSender.register();
        statusReceiver = new StatusReceiver(socket);
        statusReceiver.next(); // We are waiting to received a status before follow up
        statusReceiverThread = new Thread(statusReceiver, "Status receiver");
        statusReceiverThread.setDaemon(true); // TODO : clean stop
        statusReceiverThread.start();
        camera = new Camera(shader);
        keyManager = new BoardKeyManager(window, commandSender, camera);
    }

    @Override
    public Name next() {
        return Name.BOARD;
    }


    @Override
    public void render() {
        shader.use();
        camera.update();
        GLUtils.checkError();
        gameRenderer.render(statusReceiver.getStatus());        
    }

    @Override
    public void release() {
        gameRenderer.release();
        log.info("Stopping statusReceiver");
        statusReceiver.stop();
        try {
            statusReceiverThread.join();
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
        
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
