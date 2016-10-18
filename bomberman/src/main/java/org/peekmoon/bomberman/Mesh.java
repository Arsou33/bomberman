package org.peekmoon.bomberman;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

public class Mesh implements Serializable {

    private static final long serialVersionUID = 7L;
    
    private float[] vertices;
    private short[] indices;

    public static Mesh get(String name) {
        // TODO : Move getResourceAsStream in a resource provider
        try (ObjectInputStream ois = new ObjectInputStream(Mesh.class.getResourceAsStream("/mesh/" + name + ".mesh" ))) {
            Mesh mesh = (Mesh) ois.readObject() ;
            return mesh;
        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }
    
    public void setVertices(float[] vertices) {
        this.vertices = vertices;
    }

    public void setIndices(short[] indices) {
        this.indices = indices;
    }
    
    public float[] getVertices() {
        return vertices;
    }
    
    public short[] getIndices() {
        return indices;
    }

    
}
