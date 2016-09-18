package org.peekmoon.bomberman.key;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.glfwGetKey;

public class KeyStateAction {
    
    private long window;
    private final int key;
    private final KeyMod[] mods;
    private final KeyAction action;
    
    
    public KeyStateAction(long window, int key, KeyAction action, KeyMod... mods) {
        this.window = window;
        this.key = key;
        this.mods = mods;
        this.action = action;
        
    }
    
    public void update(float elapsed) {
        if (isKeyPressed() && isModOk()) action.fire(elapsed);
    }

    private boolean isKeyPressed() {
        return glfwGetKey(window, key) == GLFW_PRESS;
    }
    
    private boolean isModOk() {
        for (KeyMod modToCheck : KeyMod.values()) {
            if (this.have(modToCheck) != modToCheck.isPressed(window) ) return false;
        }
        return true;
    }
    
    private boolean have(KeyMod keyMod) {
        for (KeyMod currentKeyMod : mods) {
            if (currentKeyMod == keyMod) return true;                
        }
        return false;
    }
}
