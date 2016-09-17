package org.peekmoon.bomberman;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;


import static org.lwjgl.system.MemoryUtil.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.opengl.GL;
import org.peekmoon.bomberman.shader.FragmentShader;
import org.peekmoon.bomberman.shader.ProgramShader;
import org.peekmoon.bomberman.shader.VertexShader;

public class Main {
    

	public static void main(String[] args) throws Exception {
		System.out.println("Bomberman starting...");
		new Main().start();
		System.out.println("Bomberman finished...");
	}
	
	private long window;
	private GLFWErrorCallback errorCallback;
	
    private Board board;
    private Player player;
    private Camera camera;
    
    private Texture testTexture;

    private void start() throws IOException, URISyntaxException {
        initOpenGlWindow();
        
        ProgramShader shader = new ProgramShader(new VertexShader("shader"), new FragmentShader("shader"));
        shader.use();
        
        testTexture = new Texture("test.png");
        
        board = new Board(shader);
        player = new Player(shader);
        camera = new Camera(shader);
        KeyManager keyManager = new KeyManager(window, camera);
        glfwSetKeyCallback(window, keyManager);
        
        double lastTime = glfwGetTime();
        double timeToStat = 1;
        int nbFrame = 0;
        while (!glfwWindowShouldClose(window)) {
            double time = glfwGetTime();
            double delta = time - lastTime;
            lastTime = time;
            timeToStat -= delta;
            nbFrame++;
            if (timeToStat < 0) {
                System.out.println("FPS : " + nbFrame);
                nbFrame = 0;
                timeToStat=1;
            }
            
            glClearColor(0.5f, 0.5f, 0.6f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            camera.update();
            keyManager.update();
            
            board.render();
            player.render();

            glfwSwapBuffers(window);
            glfwPollEvents();
        }
        
        release();        

    }

    private void initOpenGlWindow() {
        errorCallback = GLFWErrorCallback.createPrint(System.err);
        glfwSetErrorCallback(errorCallback);
        
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }
        
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
        
        
        window = glfwCreateWindow(896, 504, "Simple example", NULL, NULL);
        if (window == NULL) {
            glfwTerminate();
            throw new RuntimeException("Failed to create the GLFW window");
        }
        
        glfwSetWindowAspectRatio(window, 16, 9);
        glfwSetFramebufferSizeCallback(window, (win, width, height) -> glViewport(0, 0, width, height));
        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
        GL.createCapabilities();
        glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_LESS);
    }

    private void release() {
        board.release();
        player.release();
        testTexture.release();
        glfwDestroyWindow(window);
        Callbacks.glfwFreeCallbacks(window);
        glfwTerminate();
        errorCallback.free();
    }
    
}
