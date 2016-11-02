package org.peekmoon.bomberman.key;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_REPEAT;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.glfw.GLFWKeyCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KeyManager extends GLFWKeyCallback {
    
    private final static Logger log = LoggerFactory.getLogger(KeyManager.class);

    private final long window;
    private final Map<Integer, List<KeyEventActivity>> keyEventActivitiesMap = new HashMap<>();
    private final List<KeyStateActivity> keyStateActivities = new ArrayList<>();
    
    public KeyManager(long window) {
        this.window = window;
        addKeyEventAction(GLFW_KEY_ESCAPE, GLFW_PRESS, () -> glfwSetWindowShouldClose(window, true));
    }

    protected void addKeyStateAction(int key, KeyActivity activity, KeyMod... mods) {
        keyStateActivities.add(new KeyStateActivity(window, key, activity, mods));
    }

    protected void addKeyEventAction(int key, int action, KeyActivity activity) {
        List<KeyEventActivity> keyEventActivities = keyEventActivitiesMap.get(key);
        if (keyEventActivities == null) {
            keyEventActivities = new ArrayList<>();
            keyEventActivitiesMap.put(key, keyEventActivities);
        }
        keyEventActivities.add(new KeyEventActivity(action, activity));
    }


    @Override
    public void invoke(long window, int key, int scancode, int action, int mods) {
        if (action == GLFW_REPEAT) return; // Ignore repeat action
        List<KeyEventActivity> activities = keyEventActivitiesMap.get(key);
        if (activities == null) return;
        
        log.debug("Invoke Key : {} scancode : {} action : {} mods : {}", key, scancode, action, mods);
        for (KeyEventActivity keyEventActivities : activities) {
            if (keyEventActivities.isTriggerState(action)) {
                keyEventActivities.fire();
            }
        }
    }
    
    public void update() {
        for (KeyStateActivity keyStateActivity : keyStateActivities) {
            keyStateActivity.update();
        }        
    }
    

  



}
