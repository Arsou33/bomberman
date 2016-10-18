package org.peekmoon.bomberman.opengl;

import static org.lwjgl.opengl.GL11.glGetError;

public class GLUtils {
    
    public static void checkError(String msg) {
        GLError err = GLError.get(glGetError());
        if (err.isError()) {
            throw new IllegalStateException(err + " : " + msg);
        }
    }

    public static void checkError() {
        checkError(null);
    }
    
    private static enum GLError {
        GL_NO_ERROR(0x000, "No error"),
        GL_INVALID_ENUM(0x0500, "Given when an enumeration parameter is not a legal enumeration for that function. This is given only for local problems; if the spec allows the enumeration in certain circumstances, where other parameters or state dictate those circumstances, then GL_INVALID_OPERATION is the result instead."),
        GL_INVALID_VALUE(0x0501, "Given when a value parameter is not a legal value for that function. This is only given for local problems; if the spec allows the value in certain circumstances, where other parameters or state dictate those circumstances, then GL_INVALID_OPERATION is the result instead."),
        GL_INVALID_OPERATION(0x0502, "Given when the set of state for a command is not legal for the parameters given to that command. It is also given for commands where combinations of parameters define what the legal parameters are."),
        GL_STACK_OVERFLOW(0x0503, "Given when a stack pushing operation cannot be done because it would overflow the limit of that stack's size."),
        GL_STACK_UNDERFLOW(0x0504, "Given when a stack popping operation cannot be done because the stack is already at its lowest point."),
        GL_OUT_OF_MEMORY(0x0505, "Given when performing an operation that can allocate memory, and the memory cannot be allocated. The results of OpenGL functions that return this error are undefined; it is allowable for partial operations to happen."),
        GL_INVALID_FRAMEBUFFER_OPERATION(0x0506, "Given when doing anything that would attempt to read from or write/render to a framebuffer that is not complete."),
        GL_CONTEXT_LOST(0x0507, "Given if the OpenGL context has been lost, due to a graphics card reset.");
        
        private int code;
        private String message;
        
        GLError(int code, String message) {
            this.code = code;
            this.message=  message;
        }
        
        public String getMessage() {
            return message;
        }
        
        public static GLError get(int code) {
            for (GLError error : GLError.values()) {
                if (error.code == code) return error;
            }
            throw new IllegalArgumentException("Unknow code : " + code);
        }
        
        public boolean isError() {
            return this != GL_NO_ERROR;
        }
        
        @Override
        public String toString() {
            return String.format("[%s(0x%04X)]", name(), code);
        }
    }

}
