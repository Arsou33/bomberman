package org.peekmoon.bomberman.shader;

import org.joml.Matrix4f;

public interface ProjectionView {
    
    void setProjection(Matrix4f matrix);
    void setView(Matrix4f matrix);

}
