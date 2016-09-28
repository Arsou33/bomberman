package org.peekmoon.bomberman.key;

import static org.lwjgl.glfw.GLFW.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.glfw.GLFWKeyCallback;
import org.peekmoon.bomberman.Camera;
import org.peekmoon.bomberman.board.Direction;
import org.peekmoon.bomberman.network.CommandSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KeyManager extends GLFWKeyCallback {
    
    private final static Logger log = LoggerFactory.getLogger(KeyManager.class);

    
    private final float speed = 0.05f;
    private final long window;
    private final Map<Integer, List<KeyEventActivity>> keyEventActivitiesMap = new HashMap<>();
    private final List<KeyStateActivity> keyStateActivities = new ArrayList<>();
    
    public KeyManager(long window, CommandSender command, Camera camera) {
        this.window = window;
        
        // Camera movement
        
        addKeyStateAction(GLFW_KEY_UP, elapsed -> camera.translate(0,speed,0), KeyMod.CONTROL);
        addKeyStateAction(GLFW_KEY_DOWN, elapsed -> camera.translate(0, -speed, 0), KeyMod.CONTROL);
        addKeyStateAction(GLFW_KEY_LEFT, elapsed -> camera.translate(-speed, 0, 0), KeyMod.CONTROL);
        addKeyStateAction(GLFW_KEY_RIGHT, elapsed -> camera.translate(speed, 0, 0), KeyMod.CONTROL);

        addKeyStateAction(GLFW_KEY_PAGE_UP, elapsed -> camera.translate(0,0,speed), KeyMod.CONTROL);
        addKeyStateAction(GLFW_KEY_PAGE_DOWN, elapsed -> camera.translate(0,0,-speed), KeyMod.CONTROL);

        
        addKeyStateAction(GLFW_KEY_UP, elapsed -> camera.targetTranslate(0,speed,0), KeyMod.SHIFT);
        addKeyStateAction(GLFW_KEY_DOWN, elapsed -> camera.targetTranslate(0, -speed, 0), KeyMod.SHIFT);
        addKeyStateAction(GLFW_KEY_LEFT, elapsed -> camera.targetTranslate(-speed, 0, 0), KeyMod.SHIFT);
        addKeyStateAction(GLFW_KEY_RIGHT, elapsed -> camera.targetTranslate(speed, 0, 0), KeyMod.SHIFT);

        addKeyStateAction(GLFW_KEY_PAGE_UP, elapsed -> camera.targetTranslate(0,0,speed), KeyMod.SHIFT);
        addKeyStateAction(GLFW_KEY_PAGE_DOWN, elapsed -> camera.targetTranslate(0,0,-speed), KeyMod.SHIFT);
        
        // Event action
        addKeyEventAction(GLFW_KEY_ESCAPE, GLFW_PRESS, elapsed -> glfwSetWindowShouldClose(window, true));
        
        addKeyEventAction(GLFW_KEY_UP, GLFW_PRESS, elapsed -> command.playerStartMove(Direction.UP));
        addKeyEventAction(GLFW_KEY_DOWN, GLFW_PRESS, elapsed -> command.playerStartMove(Direction.DOWN));
        addKeyEventAction(GLFW_KEY_LEFT, GLFW_PRESS, elapsed -> command.playerStartMove(Direction.LEFT));
        addKeyEventAction(GLFW_KEY_RIGHT, GLFW_PRESS, elapsed -> command.playerStartMove(Direction.RIGHT));
        
        addKeyEventAction(GLFW_KEY_UP, GLFW_RELEASE, elapsed -> command.playerStopMove(Direction.UP));
        addKeyEventAction(GLFW_KEY_DOWN, GLFW_RELEASE, elapsed -> command.playerStopMove(Direction.DOWN));
        addKeyEventAction(GLFW_KEY_LEFT, GLFW_RELEASE, elapsed -> command.playerStopMove(Direction.LEFT));
        addKeyEventAction(GLFW_KEY_RIGHT, GLFW_RELEASE, elapsed -> command.playerStopMove(Direction.RIGHT));
        
        addKeyEventAction(GLFW_KEY_SPACE, GLFW_PRESS, elapsed -> command.playerDropBomb());
    }

    private void addKeyStateAction(int key, KeyActivity activity, KeyMod... mods) {
        keyStateActivities.add(new KeyStateActivity(window, key, activity, mods));
    }

    private void addKeyEventAction(int key, int action, KeyActivity activity) {
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
        
        log.debug("Invoke Key : {} scancode : {} action : {} mods : {}", key, scancode, action, mods);
        for (KeyEventActivity keyEventActivities : keyEventActivitiesMap.get(key)) {
            if (keyEventActivities.isTriggerState(action)) {
                keyEventActivities.fire(speed); // TODO : Check if have an elapsed on event mean something
            }
        }
    }
    
    public void update(float elapsed) {
        for (KeyStateActivity keyStateActivity : keyStateActivities) {
            keyStateActivity.update(elapsed);
        }        
    }
    

  



}
