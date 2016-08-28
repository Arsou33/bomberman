package org.peekmoon.bomberman.shader;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.peekmoon.bomberman.GLUtils;

public abstract class Shader {
    
    ShaderType type;
    String name;
    int shader;
    
    
    public Shader(ShaderType type, String name) {
        this.type = type;
        this.name = name;
        shader = glCreateShader(type.getGLType());
        if (shader == 0) throw new IllegalStateException();
        glShaderSource(shader, readSource(name));
        GLUtils.checkError();
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
            return new String(Files.readAllBytes(getPath(name)));
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }
    
    Path getPath(String name) {
        try {
            String resourceName = "/shader/" + name + "." + type.getExt();
            URL url = getClass().getResource(resourceName);
            if (url == null) {
                throw new IllegalArgumentException("Shader " + resourceName + " is not found"); 
            }
            return Paths.get(url.toURI());
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e);
        }
    }
    
    
    @Override
    public String toString() {
        return "Shader " + name + " of type " + type;
    }

}
