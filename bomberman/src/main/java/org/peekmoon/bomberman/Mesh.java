package org.peekmoon.bomberman;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;


public class Mesh implements Serializable {

    private static final long serialVersionUID = 3L;
    
    private float[] vertices;
    private short[] indices;
    
    private int vboId;
    private int iboId;
    private int vaoId;
    private int textureId;
    
    public static Mesh get(String name) {
        // TODO : Move getResourceAsStream in a resource provider
        try (ObjectInputStream ois = new ObjectInputStream(Mesh.class.getResourceAsStream("/mesh/" + name + ".mesh" ))) {
            Mesh mesh = (Mesh) ois.readObject() ;
            mesh.init();
            return mesh;
        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }
    
    private void init() {
        
        GLUtils.checkError("Init start");
        
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

        //Generate textures
        try {
            // TODO : Use resource provider
            BufferedImage image = ImageIO.read(Mesh.class.getResourceAsStream("/mesh/wall.jpg"));
            ByteBuffer imageBuffer = ByteBuffer.allocateDirect(4*image.getHeight()*image.getWidth());
            byte[] imageByteArray = (byte[])image.getRaster().getDataElements(0,0,image.getWidth(),image.getHeight(),null);
            imageBuffer.put(imageByteArray);
            imageBuffer.rewind();

            textureId = glGenTextures();
            glBindTexture(GL_TEXTURE_2D, textureId);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            GLUtils.checkError("Unable parameter texture");
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, image.getWidth(), image.getHeight(), 0, GL_RGB, GL_UNSIGNED_BYTE, imageBuffer);
            GLUtils.checkError("Unable transfer texture to GPU");
        
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        
        GLUtils.checkError("Unable to init object");
    }
    
    public void release() {
        glDeleteBuffers(vboId);
        glDeleteBuffers(iboId);
        glDeleteTextures(textureId);
    }
    
    public void draw() {
        // Texture
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, textureId);
        
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
        
        GLUtils.checkError("DRAW");
    }
    
    
    // TODO : Only use by asset builder find better implementation
    public void setVertices(float[] vertices) {
        this.vertices = vertices;
    }

    public void setIndices(short[] indices) {
        this.indices = indices;
    }
    
}
