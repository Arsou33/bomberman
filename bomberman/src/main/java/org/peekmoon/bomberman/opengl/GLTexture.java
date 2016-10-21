package org.peekmoon.bomberman.opengl;

import static org.lwjgl.opengl.GL11.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;

public class GLTexture {
    
    private int id;
    
    public GLTexture(String texture) {
        try (InputStream is = getClass().getResourceAsStream("/texture/" + texture)) {
            init(is);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

    }
    
    public GLTexture(InputStream is) {
        try {
            init(is);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

    }
    
    private void init(InputStream is) throws IOException {
        PNGDecoder decoder = new PNGDecoder(is);
        ByteBuffer buf = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());
        decoder.decode(buf, decoder.getWidth() * 4, Format.RGBA);
        buf.flip();
        id = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, id);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        GLUtils.checkError("Unable parameter texture");
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, decoder.getWidth(), decoder.getHeight(), 0, GL_RGBA,
                GL_UNSIGNED_BYTE, buf);
        GLUtils.checkError("Unable transfer texture to GPU");
    }

    public void release() {
        glDeleteTextures(id);
    }

    public int getId() {
        return id;
    }
    
    

}
