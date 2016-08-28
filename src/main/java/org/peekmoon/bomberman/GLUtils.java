package org.peekmoon.bomberman;

import static org.lwjgl.opengl.GL11.GL_NO_ERROR;
import static org.lwjgl.opengl.GL11.glGetError;

public class GLUtils {
    
    public static void checkError(String msg) {
        if (glGetError() != GL_NO_ERROR) throw new IllegalStateException(msg);
    }

    public static void checkError() {
        checkError(null);
    }

}
