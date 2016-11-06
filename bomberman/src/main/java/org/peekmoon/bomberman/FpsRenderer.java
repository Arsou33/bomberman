package org.peekmoon.bomberman;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class FpsRenderer {

    private final double statEvery = 0.5;
    private final TextRenderer textRenderer;
    
    private double lastTime = glfwGetTime();
    private double timeToStat = statEvery;
    private int nbFrame = 0;
    private double fps = 0;


    public FpsRenderer(TextRenderer textRenderer) {
        this.textRenderer = textRenderer;
    }

    public void render() {
        double time = glfwGetTime();
        float elapsed = (float) (time - lastTime);
        lastTime = time;
        timeToStat -= elapsed;
        nbFrame++;
        if (timeToStat < 0) {
            fps = nbFrame / statEvery;
            nbFrame = 0;
            timeToStat = statEvery;
        }

        textRenderer.render(-0.48f, 1.4f, 55, String.format("FPS : %04.0f", fps));

    }
}
