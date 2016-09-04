package org.peekmoon.bomberman.asset.builder;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class Vertex {
    
    private Vector3f position;
    private Vector2f texCoord;
    
    public Vertex(Vector3f position, Vector2f texCoord) {
        this.position = position;
        this.texCoord = texCoord;
    }
    
    public Vertex(Vector3f position) {
        this(position, null);
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector2f getTexCoord() {
        return texCoord;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((position == null) ? 0 : position.hashCode());
        result = prime * result + ((texCoord == null) ? 0 : texCoord.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof Vertex))
            return false;
        Vertex other = (Vertex) obj;
        if (position == null) {
            if (other.position != null)
                return false;
        } else if (!position.equals(other.position))
            return false;
        if (texCoord == null) {
            if (other.texCoord != null)
                return false;
        } else if (!texCoord.equals(other.texCoord))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Vertex [position=" + position + ", texCoord=" + texCoord + "]";
    }
    
    
    
    
    

}
