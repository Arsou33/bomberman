package org.peekmoon.bomberman.menu;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ENTER;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;

import org.peekmoon.bomberman.key.KeyManager;

public class CreateOrJoinKeyManager extends KeyManager {
    
    public CreateOrJoinKeyManager(long window, CreateOrJoinStage stage) {
        super(window);
        Menu menu = stage.getMenu();
        addKeyEventAction(GLFW_KEY_ENTER, GLFW_PRESS, () -> stage.selectMenu());
        addKeyEventAction(GLFW_KEY_UP, GLFW_PRESS, () -> menu.selectPrevious());
        addKeyEventAction(GLFW_KEY_DOWN, GLFW_PRESS, () -> menu.selectNext());
    }

}
