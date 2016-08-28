package org.peekmoon.bomberman;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;


import static org.lwjgl.system.MemoryUtil.*;

import java.io.IOException;
import java.net.URISyntaxException;

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
        
        /*
        float points[] = {
          0.0f,  0.5f,  0.0f,
          0.5f, -0.5f,  0.0f,
          -0.5f, -0.5f,  0.0f,
          0.0f, -1.5f, 0.0f
        };
        */
        
        float points[] = {
          1f, 1f, -1f, 
          1f, -1f, -1f, 
          -1f, -0.9999998f, -1f, 
          -0.9999997f, 1f, -1f, 
          1f, 0.9999995f, 1f,
          0.9999994f, -1.000001f, 1f,
          -1f, -0.9999997f, 1f,
          -1f, 1f, 1f
        };
        
        // TODO : delete buffer
        int positionVbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, positionVbo);
        glBufferData(GL_ARRAY_BUFFER, points, GL_STATIC_DRAW);
        
        // TODO : delete buffer 
        int vao = glGenVertexArrays();
        glBindVertexArray(vao);
        glEnableVertexAttribArray(0);
        glBindBuffer(GL_ARRAY_BUFFER, vao);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, NULL);

        /*
        short indices[] = {
          0, 1, 2, 3, 2, 1
        };
        */
        
        short indices[] = {
           0,2,3,7,5,4,4,1,0,5,2,1,2,7,3,0,7,4,0,1,2,7,6,5,4,5,1,5,6,2,2,6,7,0,3,7
        };
        
        int indicesVbo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indicesVbo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
        
        
        ProgramShader shader = new ProgramShader(new VertexShader("shader"), new FragmentShader("shader"));
        shader.use();
        shader.setUniformMatrix4();
        
        while (!glfwWindowShouldClose(window)) {
            double time = glfwGetTime();
            glClearColor(0.5f, 0.5f, 0.6f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            glBindVertexArray(vao);
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indicesVbo);
            //glDrawArrays(GL_TRIANGLES, 0, 3);
            glDrawElements(GL_TRIANGLES, 36, GL_UNSIGNED_SHORT, 0);
            
            glfwPollEvents();
            glfwSwapBuffers(window);
        }
        
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
