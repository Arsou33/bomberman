package org.peekmoon.bomberman;

import org.joml.Matrix4f;
import org.peekmoon.bomberman.shader.ProjectionViewMatrix;

public class Camera {
    
    private ProjectionViewMatrix projectionView;
    
    public Camera(ProjectionViewMatrix projectionView) {
        this.projectionView = projectionView;
        Matrix4f projection = new Matrix4f().perspective((float) Math.toRadians(45.0f), 1.0f, 0.1f, 1000.0f);
        projectionView.setProjection(projection);
        
        Matrix4f view = new Matrix4f()
                .setLookAt(0.0f, -25.0f, 65.0f,        // Eye
                           0.0f, 0.0f, 0.0f,          // LookAt
                           0.0f, 10.0f, 1.0f);         // Up
        projectionView.setView(view);
    }
    
}
