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
import org.lwjgl.glfw.GLFWKeyCallback;
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

    private void start() throws IOException, URISyntaxException {
        GLFWErrorCallback errorCallback = GLFWErrorCallback.createPrint(System.err);
        glfwSetErrorCallback(errorCallback);
        
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }
        
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
        
        
        long window = glfwCreateWindow(896, 504, "Simple example", NULL, NULL);
        if (window == NULL) {
            glfwTerminate();
            throw new RuntimeException("Failed to create the GLFW window");
        }
        
        glfwSetWindowAspectRatio(window, 16, 9);
        glfwSetFramebufferSizeCallback(window, framebufferSizeCallback);        
        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
        GL.createCapabilities();
        glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_LESS);
        
        ProgramShader shader = new ProgramShader(new VertexShader("shader"), new FragmentShader("shader"));
        shader.use();
        
        
        Mesh cubeMesh = Mesh.get("cube");
        Mesh quadMesh = Mesh.get(new float[] {
                -0.5f,-0.5f,0, 0,0, 
                0.5f,-0.5f,0,  1.5f,0,
                -0.5f,0.5f,0,  0,1.5f, 
                0.5f,0.5f,0,   1.5f,1.5f }, new short[] {0,1,2, 1,2,3}, "grass.png");
        

        List<Geometry> geometries = new ArrayList<>();
        
        // Add ground geometries
        int nbTilesWidth = 24;
        int nbTilesHeight = 18;
        
        for (int i=0; i<nbTilesWidth; i++) {
            for (int j=0; j<nbTilesHeight; j++) {
                Geometry ground = new Geometry(quadMesh, shader);
                ground.setPosition(i, j, 0);
                geometries.add(ground);
            }
        }
        
        for (int i=0; i<nbTilesWidth; i+=2) {
            for (int j=0; j<nbTilesHeight; j+=2) {
                Geometry geometry = new Geometry(cubeMesh, shader);
                geometry.setPosition(i, j, -0.2f);
                geometries.add(geometry);
            }
        }

        Camera camera = new Camera(shader);
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
            
            for (Geometry geometry : geometries) {
                geometry.draw();
            }
            
            glfwSwapBuffers(window);
            glfwPollEvents();
        }
        
        cubeMesh.release();
        
        glfwDestroyWindow(window);
        Callbacks.glfwFreeCallbacks(window);
        glfwTerminate();
        errorCallback.free();
    }
    
    
    private GLFWFramebufferSizeCallback framebufferSizeCallback = new GLFWFramebufferSizeCallback() {
        
        @Override
        public void invoke(long window, int width, int height) {
            glViewport(0, 0, width, height);
        }
    };

}
