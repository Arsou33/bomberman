package org.peekmoon.bomberman;

import org.joml.Matrix4f;
import org.peekmoon.bomberman.shader.ProgramProjectionView;

public class Camera {
    
    // TODO : ProjectionView should be calculate here and a unique matrix should be used in GLSL for projectionView
    
    private ProgramProjectionView programProjectionView;
    private Matrix4f projection;
    private Matrix4f view;
    
    public Camera(ProgramProjectionView programProjectionView) {
        this.programProjectionView = programProjectionView;
        projection = new Matrix4f().perspective((float) Math.toRadians(45.0f), 16.0f/9.0f, 0.1f, 1000.0f);
        
        view = new Matrix4f()
                .setLookAt(0.0f, -25.0f, 65.0f,       // Eye
                           0.0f, 0.0f, 0.0f,          // LookAt
                           0.0f, 10.0f, 1.0f);        // Up
    }
    
    public void update() {
        programProjectionView.setProjection(projection);
        programProjectionView.setView(view);
    }
    
}
