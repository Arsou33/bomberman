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
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
        
        
        long window = glfwCreateWindow(640, 480, "Simple example", NULL, NULL);
        if (window == NULL) {
            glfwTerminate();
            throw new RuntimeException("Failed to create the GLFW window");
        }
        
        glfwSetKeyCallback(window, keyCallback);
        glfwMakeContextCurrent(window);
        GL.createCapabilities();
        glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_LESS);
        
        ProgramShader shader = new ProgramShader(new VertexShader("shader"), new FragmentShader("shader"));
        shader.use();
        
        
        Mesh cubeMesh = Mesh.get("cube");

        List<Geometry> geometries = new ArrayList<>();
        for (int i=-5; i<5; i++) {
            for (int j=-5; j<5; j++) {
                Geometry geometry = new Geometry(cubeMesh, shader);
                geometry.setPosition(i*4, j*4, 0);
                geometries.add(geometry);
            }
        }
        
        Camera camera = new Camera(shader);
        
        while (!glfwWindowShouldClose(window)) {
            double time = glfwGetTime();
            glClearColor(0.5f, 0.5f, 0.6f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            
            for (Geometry geometry : geometries) {
                geometry.draw();
            }
            
            glfwPollEvents();
            glfwSwapBuffers(window);
        }
        
        cubeMesh.release();
        
        glfwDestroyWindow(window);
        Callbacks.glfwFreeCallbacks(window);
        glfwTerminate();
        errorCallback.free();
    }
    
    private GLFWKeyCallback keyCallback = new GLFWKeyCallback() {
        @Override
        public void invoke(long window, int key, int scancode, int action, int mods) {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_PRESS) {
                glfwSetWindowShouldClose(window, true);
            }
        }
    };

}
