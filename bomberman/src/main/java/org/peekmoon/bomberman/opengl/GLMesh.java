package org.peekmoon.bomberman.opengl;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glBufferSubData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class GLMesh {
    
    private final int vboId;
    private final int vaoId;
    private GLTexture texture;
    
    public GLMesh(float[] vertices, GLTexture texture, int...  attribSizes) {
        GLUtils.checkError("Open GL Error before mesh creation");
        this.texture = texture;

        // Generate, bind and fill vertex buffer object
        vboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
        GLUtils.checkError("Unable to fill vertex buffer");

        // Generate and bind vertex array
        vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);
        
        int stride = 0;
        for (int size : attribSizes) {
            stride += size;
        }
        int floatSize = 4;
        int currentOffset = 0;
        for (int iAttrib = 0; iAttrib<attribSizes.length; iAttrib++) {
            glVertexAttribPointer(iAttrib, attribSizes[iAttrib], GL_FLOAT, false, stride*floatSize, currentOffset);
            currentOffset+=attribSizes[iAttrib]*floatSize;
        }
        GLUtils.checkError("Unable to create vertex array");
        
        // Unbind buffer object and vertex array
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }
    
    public void draw() {
        bind();
        internalDraw();
        unbind();
    }
    
    /**
     * Update vertices in the vbo buffer
     * Use buffer respecification as : https://www.opengl.org/wiki/Buffer_Object_Streaming#Buffer_re-specification
     * @param vertices
     */
    public void updateVertices(float[] vertices) {
        GLUtils.checkError();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER,(long)(vertices.length << 2), GL_STATIC_DRAW); 
        glBufferSubData(GL_ARRAY_BUFFER, 0, vertices); // TODO : Compare with a glMapBuffer ou glMapBufferRange
        GLUtils.checkError();

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        GLUtils.checkError("Unable to update vertices");

    }

    public void release() {
        glDeleteBuffers(vboId);
    }

    protected void bind() {
        GLUtils.checkError();
        // Texture
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, texture.getId());
        
        // Vertex array
        glBindVertexArray(vaoId);
        glEnableVertexAttribArray(0); // TODO : Parametre en fonction du nombre d'attributs
        glEnableVertexAttribArray(1);
        GLUtils.checkError("Unable to bindBuffer");
    }
    
    protected void internalDraw() {
        //TODO : Internal draw without ibo
    }
    
    protected void unbind() {
        // Deselect everything
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
    }

}
