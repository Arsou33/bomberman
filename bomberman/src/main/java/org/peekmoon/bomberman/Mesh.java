package org.peekmoon.bomberman;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

public class Mesh implements Serializable {

    private static final long serialVersionUID = 5L;
    
    private float[] vertices;
    private short[] indices;
    
    transient private int vboId;
    transient private int iboId;
    transient private int vaoId;
    transient private Texture texture;
    
    public static Mesh get(String name, Texture texture) {
        // TODO : Move getResourceAsStream in a resource provider
        try (ObjectInputStream ois = new ObjectInputStream(Mesh.class.getResourceAsStream("/mesh/" + name + ".mesh" ))) {
            Mesh mesh = (Mesh) ois.readObject() ;
            mesh.init(texture);
            return mesh;
        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }
    
    public static Mesh get(float[] vertices, short[] indices, Texture texture) {
        Mesh mesh = new Mesh();
        mesh.setVertices(vertices);
        mesh.setIndices(indices);
        mesh.init(texture);
        return mesh;
    }
    
    private void init(Texture texture) {
        this.texture = texture;
        
        GLUtils.checkError("Init start failed");
        
        // Generate, bind and fill vertex buffer object
        vboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
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
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        GLUtils.checkError("Unable to generate indice BO");


        GLUtils.checkError("Unable to init object");
    }
    
    public void release() {
        glDeleteBuffers(vboId);
        glDeleteBuffers(iboId);
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
        glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_SHORT, 0);
        
        // Deselect everything
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
        
        GLUtils.checkError("draw");
    }
    
    
    // TODO : Only use by asset builder find better implementation
    public void setVertices(float[] vertices) {
        this.vertices = vertices;
    }

    public void setIndices(short[] indices) {
        this.indices = indices;
    }
    
}
