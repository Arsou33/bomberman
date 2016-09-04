package org.peekmoon.bomberman.shader;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.peekmoon.bomberman.GLUtils;

public class ProgramShader {
    
    int programShader;
    
    public ProgramShader(VertexShader vs, FragmentShader fs) {
        programShader = glCreateProgram();
        if (programShader == 0) throw new IllegalStateException();
        attach(vs, fs);
        link();
    }

    public void use() {
        glUseProgram(programShader);
        GLUtils.checkError("Unable to use program");
    }
    
    public void setUniformMatrix4() {
        // TODO : That's a draft/test better implementation is required
        FloatBuffer fb = BufferUtils.createFloatBuffer(16);
        int projectionMatrixUniform = glGetUniformLocation(programShader, "projectionMatrix");
        Matrix4f projectionMatrix = new Matrix4f().perspective((float) Math.toRadians(45.0f), 1.0f, 0.1f, 100.0f);
        projectionMatrix.get(fb);
        glUniformMatrix4fv(projectionMatrixUniform, false, fb);
        
        
        Matrix4f viewMatrix = new Matrix4f()
                   .setLookAt(7.0f, 5.0f, 10.0f,        // Eye
                              0.0f, 0.0f, 0.0f,          // LookAt
                              0.0f, 1.0f, 0.0f);         // Up
        int viewMatrixUniform = glGetUniformLocation(programShader, "viewMatrix");
        viewMatrix.get(fb);
        glUniformMatrix4fv(viewMatrixUniform, false, fb);
    }

    private void link() {
        glLinkProgram(programShader);
        if (glGetProgrami(programShader, GL_LINK_STATUS) == GL_FALSE) {
            String msg = glGetProgramInfoLog(programShader);
            glDeleteProgram(programShader);
            throw new IllegalStateException("Unable to link program : " + msg);
        };
    }

    private void attach(VertexShader vs, FragmentShader fs) {
        for (Shader shader : new Shader[]{vs,fs}) {
            glAttachShader(programShader, shader.getHandle());
            if (glGetError() != GL_NO_ERROR) throw new IllegalStateException("Unable to attach " + shader);            
        }
    }
    
}
