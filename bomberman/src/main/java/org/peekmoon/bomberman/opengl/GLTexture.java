package org.peekmoon.bomberman.opengl;

import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;

public class GLTexture {
    
    private int id;
    
    public GLTexture(String texture) {
        try (InputStream in = getClass().getResourceAsStream("/texture/" + texture)) {
               PNGDecoder decoder = new PNGDecoder(in);
               ByteBuffer buf = ByteBuffer.allocateDirect(4*decoder.getWidth()*decoder.getHeight());
               decoder.decode(buf, decoder.getWidth()*4, Format.RGBA);
               buf.flip();
               id = glGenTextures();
               glBindTexture(GL_TEXTURE_2D, id);
               glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
               glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
               GLUtils.checkError("Unable parameter texture");
               glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, decoder.getWidth(), decoder.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buf);
               GLUtils.checkError("Unable transfer texture to GPU");
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
    
    public void release() {
        glDeleteTextures(id);
    }

    public int getId() {
        return id;
    }
    
    

}
