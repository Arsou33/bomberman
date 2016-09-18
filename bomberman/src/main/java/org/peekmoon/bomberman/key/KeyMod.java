package org.peekmoon.bomberman.key;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_ALT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_CONTROL;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_SHIFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT_ALT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT_CONTROL;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT_SHIFT;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.glfwGetKey;

public enum KeyMod {
    SHIFT(GLFW_KEY_LEFT_SHIFT, GLFW_KEY_RIGHT_SHIFT),
    CONTROL(GLFW_KEY_LEFT_CONTROL, GLFW_KEY_RIGHT_CONTROL),
    ALT(GLFW_KEY_LEFT_ALT, GLFW_KEY_RIGHT_ALT);
    
    private int[] keys;
    
    KeyMod(int... keys) {
        this.keys = keys;
    }    
    
    public boolean isPressed(long window) {
        for (int currentKey : keys ) {
            if (glfwGetKey(window, currentKey) == GLFW_PRESS) return true;
        }
        return false;
    }
}
