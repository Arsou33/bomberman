package org.peekmoon.bomberman;

import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.peekmoon.bomberman.opengl.GLIndexedMesh;
import org.peekmoon.bomberman.opengl.GLTexture;

public class TextRenderer {
    
    private static final int textureWidth = 1024;
    private static final int textureHeight = 1024;
    
    private final float[] vertices = new float[] { 
            -0.5f, -0.5f,   0, 1, 
             0.5f, -0.5f,   1, 1, 
            -0.5f,  0.5f,   0, 0,
             0.5f,  0.5f,   1, 0 };

    private final GLTexture texture;
    private final GLIndexedMesh mesh;
    
    private final int charWidth, charHeight, nbCharByLine;
    private final float charWidthTextureCoord, charHeightTextureCoord;
    private final float textureFontSize = 50;
    
    
    public TextRenderer() {
        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/SourceCodePro-Regular.otf")); 
            font = font.deriveFont(textureFontSize);
            //Font font = new Font("Consolas", Font.PLAIN, 25);

           BufferedImage img = new BufferedImage(textureWidth, textureHeight, BufferedImage.TYPE_INT_ARGB);
           Graphics2D g = img.createGraphics();
           g.setColor(Color.WHITE);
           g.fillRect(0, 0, textureWidth, textureHeight);
           g.setFont(font);
           FontMetrics fontMetrics = g.getFontMetrics(font);

           charHeight = fontMetrics.getHeight();
           charWidth = fontMetrics.charWidth('W');
           nbCharByLine = textureWidth / charWidth;
           charWidthTextureCoord = charWidth / (float)textureWidth;
           charHeightTextureCoord = charHeight / (float)textureHeight;
           
           int posX = 0;
           int posY = charHeight;
           for (int c = 0; c < 1024; c++) {
               String charAsString = new String(Character.toChars(c));
               if (posX + charWidth > textureWidth) {
                   posX = 0;
                   posY += charHeight;
               }
               g.setColor(Color.PINK);
               g.drawRect(posX, posY-charHeight, charWidth, charHeight);

               g.setColor(Color.BLACK);
               g.drawString(charAsString, posX, posY-fontMetrics.getDescent());
               posX += charWidth;
           }
           g.dispose();
           ImageIO.write(img, "png", new File("Test.png")); // TODO : Do not use file
           
           try (InputStream is = new FileInputStream(new File("Test.png"))) {
               texture = new GLTexture(is);
           };
           
           mesh = new GLIndexedMesh(// TODO : Check if an GLMesh should be better else merge the two ?
               vertices, new short[] { 0, 1, 2, 1, 2, 3 }, texture, 2, 2);
           
       } catch (IOException | FontFormatException e) {
           throw new IllegalStateException(e);
       }
        
    }

    public void render(float x, float y, float size, String string) {
        glDisable(GL_DEPTH_TEST);
        for (char c : string.toCharArray()) {
            updateTextureCoord(c);
            updateCharPos(x, y, size/textureFontSize);
            mesh.updateVertices(vertices);
            mesh.draw();            
            x+=charWidth*size/(textureWidth*textureFontSize);
        }  
        glEnable(GL_DEPTH_TEST);
    }
    
    private void updateTextureCoord(char c) {
        int noChar = (int)c;
        int ligne = noChar / nbCharByLine;
        int col = noChar % nbCharByLine;  
        vertices[2]  = col         * charWidthTextureCoord;
        vertices[3]  = (ligne + 1) * charHeightTextureCoord;
        vertices[6]  = (col + 1)   * charWidthTextureCoord;
        vertices[7]  = (ligne + 1) * charHeightTextureCoord;
        vertices[10] = col         * charWidthTextureCoord;
        vertices[11] = ligne       * charHeightTextureCoord;
        vertices[14] = (col + 1 )  * charWidthTextureCoord;
        vertices[15] = ligne       * charHeightTextureCoord;
    }

    public void release() {
        texture.release();
    }
    
    private void updateCharPos(float x, float y, float size) {
        vertices[0]  = (x - 0.5f);
        vertices[1]  = (y - 0.5f);
        vertices[4]  = (x - 0.5f)+charWidthTextureCoord*size;
        vertices[5]  = (y - 0.5f);
        vertices[8]  = (x - 0.5f);
        vertices[9]  = (y - 0.5f)+charHeightTextureCoord*size;
        vertices[12] = (x - 0.5f)+charWidthTextureCoord*size;
        vertices[13] = (y - 0.5f)+charHeightTextureCoord*size;
    }

}
