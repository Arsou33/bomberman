package org.peekmoon.bomberman.shader;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

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
