package org.peekmoon.bomberman;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

public class Mesh implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private float[] positions;
    private short[] indices;
    
    
    public static Mesh get(String name) {
        // TODO : Move getResourceAsStream in a resource provider
        // TODO : Try catch resource
        try {
            ObjectInputStream ois = new ObjectInputStream(Mesh.class.getResourceAsStream("/mesh/" + name + ".mesh" ));
            return (Mesh) ois.readObject() ;
        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }
    
    // TODO : Only use by asset builder move
    public void setIndices(short[] indices) {
        this.indices = indices;
    }


    public void setPositions(float[] positions) {
        this.positions = positions;
    }
    
    
    public float[] getPositions() {
        return positions;
    }
    
    public short[] getIndices() {
        return indices;
    }
    
    
    

}
