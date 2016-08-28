package org.peekmoon.bomberman.shader;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

public class ProgramShader {
    
    int programShader;
    
    public ProgramShader(Shader... shaders) {
        programShader = glCreateProgram();
        if (programShader == 0) throw new IllegalStateException();
        for (Shader shader : shaders) {
            glAttachShader(programShader, shader.getHandle());
            if (glGetError() != GL_NO_ERROR) throw new IllegalStateException();            
        }
        glLinkProgram(programShader);
        if (glGetProgrami(programShader, GL_LINK_STATUS) == GL_FALSE) {
            String msg = glGetProgramInfoLog(programShader);
            glDeleteProgram(programShader);
            throw new IllegalStateException("Unable to link program : " + msg);
        };
    }
    
    public void use() {
        glUseProgram(programShader);
    }
    
}
