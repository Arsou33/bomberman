package org.peekmoon.bomberman.opengl;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_SHORT;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import org.peekmoon.bomberman.Mesh;

public class GLMesh {
    
    private final int vboId;
    private final int iboId;
    private final int vaoId;
    private GLTexture texture;
    private final int nbIndices;
    
    public GLMesh(Mesh mesh, GLTexture texture) {
        this.texture = texture;
        this.nbIndices = mesh.getIndices().length;
        
        GLUtils.checkError("Init start failed");
        
        // Generate, bind and fill vertex buffer object
        vboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, mesh.getVertices(), GL_STATIC_DRAW);
        GLUtils.checkError("Unable to fill vertex buffer");
        
        // Generate and bind vertex array
        vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 5*4, 0);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 5*4, 3*4);
        GLUtils.checkError("Unable to create vertex array");
        
        // Unbind buffer object and vertex array
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
        
        // Generate indices buffer object
        iboId = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, iboId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, mesh.getIndices(), GL_STATIC_DRAW);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        GLUtils.checkError("Unable to generate indice BO");


        GLUtils.checkError("Unable to init object");
    }
    
    public void draw() {
        // Texture
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, texture.getId());
        
        // Vertex array
        glBindVertexArray(vaoId);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        
        // IBO
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, iboId);
        glDrawElements(GL_TRIANGLES, nbIndices, GL_UNSIGNED_SHORT, 0);
        
        // Deselect everything
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
        
        GLUtils.checkError("draw");
    }

    public void release() {
        glDeleteBuffers(vboId);
        glDeleteBuffers(iboId);
    }

}
