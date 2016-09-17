package org.peekmoon.bomberman;

import static org.lwjgl.glfw.GLFW.*;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFWKeyCallback;

public class KeyManager extends GLFWKeyCallback {
    
    private final float speed = 0.05f;
    private final long window;
    //private final Map<KeyEvent, Consumer<KeyEvent>> keyActions = new HashMap<>();
    private final List<KeyStateAction> keyStateActions = new ArrayList<>();
    
    
    private class KeyStateAction {
        private final int key;
        private final KeyMod[] mods;
        private final Runnable action;
        
        
        public KeyStateAction(int key, Runnable action, KeyMod... mods) {
            this.key = key;
            this.mods = mods;
            this.action = action;
            
        }
        
        public void update() {
            if (keyIsPressed() && ModIsOk()) action.run();
        }

        private boolean keyIsPressed() {
            return glfwGetKey(window, key) == GLFW_PRESS;
        }
        
        private boolean ModIsOk() {
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

    
    private enum KeyMod {
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
    
    public KeyManager(long window, Player player, Camera camera) {
        this.window = window;
        addKeyAction(GLFW_KEY_ESCAPE, () -> glfwSetWindowShouldClose(window, true));
        
        // Camera movement
        
        addKeyAction(GLFW_KEY_UP, () -> camera.translate(0,speed,0), KeyMod.CONTROL);
        addKeyAction(GLFW_KEY_DOWN, () -> camera.translate(0, -speed, 0), KeyMod.CONTROL);
        addKeyAction(GLFW_KEY_LEFT, () -> camera.translate(-speed, 0, 0), KeyMod.CONTROL);
        addKeyAction(GLFW_KEY_RIGHT, () -> camera.translate(speed, 0, 0), KeyMod.CONTROL);

        addKeyAction(GLFW_KEY_PAGE_UP, () -> camera.translate(0,0,speed), KeyMod.CONTROL);
        addKeyAction(GLFW_KEY_PAGE_DOWN, () -> camera.translate(0,0,-speed), KeyMod.CONTROL);

        
        addKeyAction(GLFW_KEY_UP, () -> camera.targetTranslate(0,speed,0), KeyMod.SHIFT);
        addKeyAction(GLFW_KEY_DOWN, () -> camera.targetTranslate(0, -speed, 0), KeyMod.SHIFT);
        addKeyAction(GLFW_KEY_LEFT, () -> camera.targetTranslate(-speed, 0, 0), KeyMod.SHIFT);
        addKeyAction(GLFW_KEY_RIGHT, () -> camera.targetTranslate(speed, 0, 0), KeyMod.SHIFT);

        addKeyAction(GLFW_KEY_PAGE_UP, () -> camera.targetTranslate(0,0,speed), KeyMod.SHIFT);
        addKeyAction(GLFW_KEY_PAGE_DOWN, () -> camera.targetTranslate(0,0,-speed), KeyMod.SHIFT);
        
        
        // Player movement

        addKeyAction(GLFW_KEY_UP, () -> player.moveUp());
        addKeyAction(GLFW_KEY_DOWN, () -> player.moveDown());
        addKeyAction(GLFW_KEY_LEFT, () -> player.moveLeft());
        addKeyAction(GLFW_KEY_RIGHT, () -> player.moveRight());
    
    }


    private void addKeyAction(int key, Runnable action, KeyMod... mods) {
        keyStateActions.add(new KeyStateAction(key, action, mods));
    }

    @Override
    public void invoke(long window, int key, int scancode, int action, int mods) {
        System.out.println("Invoke Key : " + key + " scancode : " + scancode + " action : " + action + " mods : " + mods);
    }
    
    public void update() {
        for (KeyStateAction keyStateAction : keyStateActions) {
            keyStateAction.update();
        }        
    }
    

  



}
