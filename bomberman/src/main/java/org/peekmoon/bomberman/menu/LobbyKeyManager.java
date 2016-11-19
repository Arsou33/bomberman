package org.peekmoon.bomberman.menu;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ENTER;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;

import org.peekmoon.bomberman.Stage.Name;
import org.peekmoon.bomberman.key.KeyManager;

public class LobbyKeyManager extends KeyManager {
    
    
    public LobbyKeyManager(long window, LobbyStage stage) {
        super(window);
        addKeyEventAction(GLFW_KEY_ENTER, GLFW_PRESS, () -> stage.switchToStage(Name.BOARD));
    }

}
