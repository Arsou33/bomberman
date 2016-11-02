package org.peekmoon.bomberman.opengl.shader;

import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;

enum ShaderType {
    VERTEX("vert", GL_VERTEX_SHADER),
    FRAGMENT("frag", GL_FRAGMENT_SHADER);
    
    private String ext;
    private int glType;
    
    ShaderType(String ext, int glType) {
        this.ext = ext;
        this.glType = glType;
    }
    
    public String getExt() {
        return ext;
    }
    
    public int getGLType() {
        return glType;
    }
    
}