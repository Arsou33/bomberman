package org.peekmoon.bomberman;

import org.joml.Matrix4f;
import org.peekmoon.bomberman.shader.ProjectionView;

public class Camera {
    
    private ProjectionView projectionView;
    
    public Camera(ProjectionView projectionView) {
        this.projectionView = projectionView;
        Matrix4f projection = new Matrix4f().perspective((float) Math.toRadians(45.0f), 1.0f, 0.1f, 100.0f);
        projectionView.setProjection(projection);
        
        Matrix4f view = new Matrix4f()
                .setLookAt(7.0f, 5.0f, 8.0f,        // Eye
                           0.0f, 0.0f, 0.0f,          // LookAt
                           0.0f, 1.0f, 0.0f);         // Up
        projectionView.setView(view);
    }
    
}
