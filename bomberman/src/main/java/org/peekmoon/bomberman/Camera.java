package org.peekmoon.bomberman;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.peekmoon.bomberman.shader.ProgramProjectionView;

public class Camera {
    
    private ProgramProjectionView programProjectionView;
    private Matrix4f projection;
    private Vector3f position;
    private Vector3f lookAt;
    private Vector3f up;
    
    private Matrix4f view;
    
    public Camera(ProgramProjectionView programProjectionView) {
        
        this.programProjectionView = programProjectionView;
        projection = new Matrix4f().perspective((float) Math.toRadians(75.0f), 16.0f/9.0f, 1f, 100.0f);
        
        this.position = new Vector3f(12.0f, 2.0f, 5.0f);
        this.lookAt = new Vector3f(12.0f, 5.0f, 0.0f);
        this.up = new Vector3f(0.0f, 1.0f, 1.0f);

        view = new Matrix4f();
    }
    
    public void update() {
        programProjectionView.setProjection(projection);

        updateView();
        programProjectionView.setView(view);
    }
    
    public void translate(float x, float y, float z) {
        position.add(x, y, z);
    }

    public void targetTranslate(float x, float y, float z) {
        lookAt.add(x, y, z);
    }
    
    private void updateView() {
        view.setLookAt(position, lookAt, up);
    }
    
}
