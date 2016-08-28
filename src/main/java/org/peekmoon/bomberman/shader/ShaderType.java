package org.peekmoon.bomberman.shader;

import static org.lwjgl.opengl.GL20.*;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

enum ShaderType {
    VERTEX("vert", GL_VERTEX_SHADER),
    FRAGMENT("frag", GL_FRAGMENT_SHADER);
    
    private String ext;
    private int glType;
    
    ShaderType(String ext, int glType) {
        this.ext = ext;
        this.glType = glType;
    }
    
    Path getPath(String name) {
        try {
            String resourceName = "/shader/" + name + "." + ext;
            URL url = getClass().getResource(resourceName);
            if (url == null) {
                throw new IllegalArgumentException("Shader " + resourceName + " is not found"); 
            }
            return Paths.get(url.toURI());
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public int getGLType() {
        return glType;
    }
    
}