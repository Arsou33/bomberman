package org.peekmoon.bomberman.menu;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ENTER;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;

import org.peekmoon.bomberman.Stage.Name;
import org.peekmoon.bomberman.key.KeyManager;

public class MenuKeyManager extends KeyManager {
    
    
    public MenuKeyManager(long window, MenuStage stage) {
        super(window);
        addKeyEventAction(GLFW_KEY_ENTER, GLFW_PRESS, () -> stage.setNext(Name.BOARD));
    }

}
