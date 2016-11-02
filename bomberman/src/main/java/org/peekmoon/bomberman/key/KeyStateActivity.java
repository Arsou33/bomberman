package org.peekmoon.bomberman.key;

import static org.lwjgl.glfw.GLFW.*;

public class KeyStateActivity {
    
    private long window;
    private final int key;
    private final KeyMod[] mods;
    private final KeyActivity activity;
    
    
    public KeyStateActivity(long window, int key, KeyActivity activity, KeyMod... mods) {
        this.window = window;
        this.key = key;
        this.mods = mods;
        this.activity = activity;
    }
    
    public void update() {
        if (isKeyPressed() && isModOk()) activity.fire();
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
