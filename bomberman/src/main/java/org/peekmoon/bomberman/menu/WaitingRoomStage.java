package org.peekmoon.bomberman.menu;

import org.peekmoon.bomberman.Stage;
import org.peekmoon.bomberman.TextRenderer;
import org.peekmoon.bomberman.key.KeyManager;
import org.peekmoon.bomberman.network.CommandSender;
import org.peekmoon.bomberman.network.StatusReceiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WaitingRoomStage extends Stage {

    private final static Logger log = LoggerFactory.getLogger(LobbyStage.class);
    
    private final long window;
    private final StatusReceiver statusReceiver;
    private final TextRenderer textRenderer;
    private final CommandSender commandSender;

    private WaitingRoomKeyManager keyManager;

    public WaitingRoomStage(long window, StatusReceiver statusReceiver, TextRenderer textRenderer, CommandSender commandSender) {
        this.window = window;
        this.statusReceiver = statusReceiver;
        this.textRenderer = textRenderer;
        this.commandSender = commandSender;
    }

    @Override
    public void init() {
        commandSender.createWaitingRoom();
        keyManager = new WaitingRoomKeyManager(window, this);
    }

    @Override
    public void render() {
        float y = -0.50f;
        float size = 10;
        for (int i=1; i<15; i++) {
            y+=size/800;
            size *= 1.5;
            textRenderer.render(-0.5f, y , size, String.format("Patience"));
        }
    }

    @Override
    public void release() {

    }

    @Override
    public KeyManager getKeyManager() {
        return keyManager;
    }

    @Override
    protected Name getName() {
        return Name.LOBBY;
    }


}
