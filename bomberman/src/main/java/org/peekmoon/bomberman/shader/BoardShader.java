package org.peekmoon.bomberman.shader;

import org.joml.Matrix4f;
import org.peekmoon.bomberman.opengl.shader.ProgramShader;

public class BoardShader extends ProgramShader implements ModelMatrix, ProgramProjectionView {
    
    
    private final int viewMatrixUniform;
    private final int projectionMatrixUniform;
    private final int modelMatrixUniform;
    
    public BoardShader() {
        super("shader");
        projectionMatrixUniform = getUniformLocation("projectionMatrix");
        viewMatrixUniform = getUniformLocation("viewMatrix");
        modelMatrixUniform = getUniformLocation("modelMatrix");
    }
    
    
    public void setProjection(Matrix4f projection) {
        setMatrix4fvUniform(projectionMatrixUniform, projection);
    }

    public void setView(Matrix4f view) {
        setMatrix4fvUniform(viewMatrixUniform, view);
    }
    
    public void setModelMatrix(Matrix4f model) {
        setMatrix4fvUniform(modelMatrixUniform, model);
    }


}
