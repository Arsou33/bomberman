package org.peekmoon.bomberman.opengl;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_SHORT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;

import org.peekmoon.bomberman.Mesh;

public class GLIndexedMesh extends GLMesh {
    
    private final int iboId;
    private final int nbIndices;

    public GLIndexedMesh(Mesh mesh, GLTexture texture) {
        this(mesh.getVertices(), mesh.getIndices(), texture, 3, 2);
    }
    
    public GLIndexedMesh(float[] vertices, short[] indices, GLTexture texture, int... attribSizes) {
        super(vertices, texture, attribSizes);
        
        this.nbIndices = indices.length;
        
        // Generate indices buffer object
        iboId = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, iboId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        GLUtils.checkError("Unable to generate indice BO");


        GLUtils.checkError("Unable to init object");
    }
    
    @Override
    protected void internalDraw() {
        // draw IBO
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, iboId);
        glDrawElements(GL_TRIANGLES, nbIndices, GL_UNSIGNED_SHORT, 0);
        GLUtils.checkError("draw");
    }
    
    @Override
    public void release() {
        glDeleteBuffers(iboId);
        super.release();
    }


}
