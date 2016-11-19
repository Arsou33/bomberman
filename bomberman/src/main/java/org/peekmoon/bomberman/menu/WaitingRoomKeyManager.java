package org.peekmoon.bomberman.menu;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ENTER;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;

import org.peekmoon.bomberman.Stage.Name;
import org.peekmoon.bomberman.key.KeyManager;

public class WaitingRoomKeyManager extends KeyManager {
    
    public WaitingRoomKeyManager(long window, WaitingRoomStage stage) {
        super(window);
        addKeyEventAction(GLFW_KEY_ENTER, GLFW_PRESS, () -> stage.switchToStage(Name.BOARD));
    }

}
