package org.peekmoon.bomberman.shader;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.peekmoon.bomberman.GLUtils;

public class ProgramShader implements ProjectionViewMatrix, ModelMatrix {
    
    int programShader;
    int viewMatrixUniform;
    int projectionMatrixUniform;
    int modelMatrixUniform;
    
    
    public ProgramShader(VertexShader vs, FragmentShader fs) {
        programShader = glCreateProgram();
        if (programShader == 0) throw new IllegalStateException();
        attach(vs, fs);
        link();
        projectionMatrixUniform = glGetUniformLocation(programShader, "projectionMatrix");
        viewMatrixUniform = glGetUniformLocation(programShader, "viewMatrix");
        modelMatrixUniform = glGetUniformLocation(programShader, "modelMatrix");
    }

    public void use() {
        glUseProgram(programShader);
        GLUtils.checkError("Unable to use program");
    }
    
    @Override
    public void setProjection(Matrix4f projection) {
        // TODO : Fix viewport is not resized when window is resizing
        setMatrix4fvUniform(projectionMatrixUniform, projection);
    }

    @Override
    public void setView(Matrix4f view) {
        setMatrix4fvUniform(viewMatrixUniform, view);
    }
    
    @Override
    public void setModelMatrix(Matrix4f model) {
        setMatrix4fvUniform(modelMatrixUniform, model);
    }
    
    private void setMatrix4fvUniform(int uniform, Matrix4f matrix) {
        float[] modelBuffer = new float[16];
        matrix.get(modelBuffer);
        glUniformMatrix4fv(uniform, false, modelBuffer);        
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
