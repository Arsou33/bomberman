package org.peekmoon.bomberman.opengl.shader;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;


import org.joml.Matrix4f;
import org.peekmoon.bomberman.opengl.GLUtils;

public class ProgramShader {
    
    private final int programShader;

    // TODO : Generalize uniform by name or enum
    public ProgramShader(VertexShader vs, FragmentShader fs) {
        programShader = glCreateProgram();
        if (programShader == 0) throw new IllegalStateException();
        attach(vs, fs);
        link();
    }

    public ProgramShader(String name) {
        this(new VertexShader(name), new FragmentShader(name));
    }

    public void use() {
        glUseProgram(programShader);
        GLUtils.checkError("Unable to use program");
    }
    
    
    protected int getUniformLocation(String uniformName) {
        int uniformLocation = glGetUniformLocation(programShader, uniformName);
        if (uniformLocation == -1) throw new IllegalArgumentException("Unknow uniform name :" + uniformName);
        return uniformLocation;
    }
    
    protected void setMatrix4fvUniform(int uniform, Matrix4f matrix) {
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
