package org.peekmoon.bomberman.board;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_PAGE_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_PAGE_UP;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

import org.peekmoon.bomberman.Camera;
import org.peekmoon.bomberman.key.KeyManager;
import org.peekmoon.bomberman.key.KeyMod;
import org.peekmoon.bomberman.model.Direction;
import org.peekmoon.bomberman.network.CommandSender;

public class BoardKeyManager extends KeyManager {
    
    private final float speed = 0.05f;

    BoardKeyManager(long window, CommandSender command, Camera camera) {
        super(window);
        // Camera movement
        addKeyStateAction(GLFW_KEY_UP, () -> camera.translate(0,speed,0), KeyMod.CONTROL);
        addKeyStateAction(GLFW_KEY_DOWN, () -> camera.translate(0, -speed, 0), KeyMod.CONTROL);
        addKeyStateAction(GLFW_KEY_LEFT, () -> camera.translate(-speed, 0, 0), KeyMod.CONTROL);
        addKeyStateAction(GLFW_KEY_RIGHT, () -> camera.translate(speed, 0, 0), KeyMod.CONTROL);

        addKeyStateAction(GLFW_KEY_PAGE_UP, () -> camera.translate(0,0,speed), KeyMod.CONTROL);
        addKeyStateAction(GLFW_KEY_PAGE_DOWN, () -> camera.translate(0,0,-speed), KeyMod.CONTROL);

        
        addKeyStateAction(GLFW_KEY_UP, () -> camera.targetTranslate(0,speed,0), KeyMod.SHIFT);
        addKeyStateAction(GLFW_KEY_DOWN, () -> camera.targetTranslate(0, -speed, 0), KeyMod.SHIFT);
        addKeyStateAction(GLFW_KEY_LEFT, () -> camera.targetTranslate(-speed, 0, 0), KeyMod.SHIFT);
        addKeyStateAction(GLFW_KEY_RIGHT, () -> camera.targetTranslate(speed, 0, 0), KeyMod.SHIFT);

        addKeyStateAction(GLFW_KEY_PAGE_UP, () -> camera.targetTranslate(0,0,speed), KeyMod.SHIFT);
        addKeyStateAction(GLFW_KEY_PAGE_DOWN, () -> camera.targetTranslate(0,0,-speed), KeyMod.SHIFT);
        
        // Event action
        addKeyEventAction(GLFW_KEY_UP, GLFW_PRESS, () -> command.playerStartMove(Direction.UP));
        addKeyEventAction(GLFW_KEY_DOWN, GLFW_PRESS, () -> command.playerStartMove(Direction.DOWN));
        addKeyEventAction(GLFW_KEY_LEFT, GLFW_PRESS, () -> command.playerStartMove(Direction.LEFT));
        addKeyEventAction(GLFW_KEY_RIGHT, GLFW_PRESS, () -> command.playerStartMove(Direction.RIGHT));
        
        addKeyEventAction(GLFW_KEY_UP, GLFW_RELEASE, () -> command.playerStopMove(Direction.UP));
        addKeyEventAction(GLFW_KEY_DOWN, GLFW_RELEASE, () -> command.playerStopMove(Direction.DOWN));
        addKeyEventAction(GLFW_KEY_LEFT, GLFW_RELEASE, () -> command.playerStopMove(Direction.LEFT));
        addKeyEventAction(GLFW_KEY_RIGHT, GLFW_RELEASE, () -> command.playerStopMove(Direction.RIGHT));
        
        addKeyEventAction(GLFW_KEY_SPACE, GLFW_PRESS, () -> command.playerDropBomb());
        
    }

}
