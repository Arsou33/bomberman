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
        float ratio = 16.0f/9.0f;
        float zoom = 10.0f;

        this.programProjectionView = programProjectionView;
        //projection = new Matrix4f().perspective((float) Math.toRadians(75.0f), ratio, 1f, 100.0f);
        projection = new Matrix4f().ortho(-zoom, zoom, -zoom/ratio, zoom/ratio, -10, 100);
        
        this.position = new Vector3f(10.0f, 1.0f, 7.0f);
        this.lookAt = new Vector3f(10.0f, 7.0f, 0.0f);
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
