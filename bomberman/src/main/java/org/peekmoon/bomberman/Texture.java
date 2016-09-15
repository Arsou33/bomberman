package org.peekmoon.bomberman;

import static org.lwjgl.opengl.GL11.*;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;

public class Texture {
    
    private int id;
    
    public Texture(String texture) {
        try {
            InputStream in = getClass().getResourceAsStream("/texture/" + texture);
            try {
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
               
            } finally {
               in.close();
            }            
        
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
