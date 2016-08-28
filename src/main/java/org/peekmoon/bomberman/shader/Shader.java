package org.peekmoon.bomberman.shader;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import java.io.IOException;
import java.nio.file.Files;

public abstract class Shader {
    
    int shader;
    ShaderType type;
    
    
    public Shader(ShaderType type, String name) {
        this.type = type;
        shader = glCreateShader(type.getGLType());
        if (shader == 0) throw new IllegalStateException();
        glShaderSource(shader, readSource(name));
        if (glGetError() != GL_NO_ERROR) throw new IllegalStateException();
        glCompileShader(shader);
        if (glGetShaderi(shader, GL_COMPILE_STATUS) == GL_FALSE) {
            String msg = glGetShaderInfoLog(shader);
            glDeleteShader(shader);
            throw new IllegalStateException("Unable to compile shader " + name + "_" + type + " : " + msg);
        };
        
    }
    
    int getHandle() {
        return shader;
    }
    
    private String readSource(String name) {
        try {
            return new String(Files.readAllBytes(type.getPath(name)));
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

}
