package org.peekmoon.bomberman;

import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.peekmoon.bomberman.opengl.GLIndexedMesh;
import org.peekmoon.bomberman.opengl.GLTexture;
import org.peekmoon.bomberman.shader.TextShader;

public class FpsRenderer {

    private final float[] vertices = new float[] { 
            -0.5f, -0.5f,   0, 1, 
             0.5f, -0.5f,   1, 1, 
            -0.5f,  0.5f,   0, 0,
             0.5f,  0.5f,   1, 0 };

    private final GLTexture texture;
    private final GLIndexedMesh mesh;
    
    private final int charWidth, charHeight;
    
    private final double statEvery = 0.5;
    private double lastTime = glfwGetTime();
    private double timeToStat = statEvery;
    private int nbFrame = 0;
    private double fps = 0;

    public FpsRenderer(TextShader shader) {

        try {
            
             Font font = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/SourceCodePro-Regular.otf")); 
             font = font.deriveFont(25.0f);
             //Font font = new Font("Consolas", Font.PLAIN, 25);

            int width = 1024;
            int height = 1024;

            BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = img.createGraphics();
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, width, height);
            g.setFont(font);
            FontMetrics fontMetrics = g.getFontMetrics(font);
            FontRenderContext frc = g.getFontRenderContext();

            Rectangle2D bound = font.getMaxCharBounds(frc);

            charHeight = fontMetrics.getHeight();
            charWidth = fontMetrics.charWidth('W');
            int posX = 0;
            int posY = charHeight;
            for (int c = 0; c < 1024; c++) {
                // if (font.canDisplay(c) && (charWidth = fontMetrics.charWidth(c)) > 0)
                {
                    String charAsString = new String(Character.toChars(c));
                    TextLayout textLayout = new TextLayout(charAsString, font, frc);
                    bound = textLayout.getPixelBounds(null, posX, posY);

                    if (posX + charWidth > width) {
                        posX = 0;
                        posY += charHeight;
                    }
                    bound = textLayout.getPixelBounds(null, posX, posY);
                    g.setColor(Color.CYAN);
                    // g.drawRect((int)Math.round(bound.getX()),
                    // (int)Math.round(bound.getY()),
                    // (int)Math.round(bound.getWidth()),
                    // (int)Math.round(bound.getHeight())); //posX, posY,
                    // (int)Math.ceil(bound.getWidth()),
                    // (int)Math.ceil(bound.getHeight()));
                    g.setColor(Color.PINK);
                    g.drawRect(posX, posY-charHeight, charWidth, charHeight);

                    g.setColor(Color.BLACK);
                    g.drawString(charAsString, posX, posY-fontMetrics.getDescent());
                    posX += charWidth;
                }
            }
            g.dispose();
            ImageIO.write(img, "png", new File("Test.png"));
            
            try (InputStream is = new FileInputStream(new File("Test.png"))) {
                texture = new GLTexture(is);
            };
            
            mesh = new GLIndexedMesh(// TODO : Check if an GLMesh should be better else merge the two ?
                vertices, new short[] { 0, 1, 2, 1, 2, 3 }, texture, 2, 2);
            
        } catch (IOException | FontFormatException e) {
            throw new IllegalStateException(e);
        }

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
        
        String string = String.format("FPS : %04.0f", fps);
        
        float x = 0.01f;
        glDisable(GL_DEPTH_TEST);

        
        for (char c : string.toCharArray()) {
            int noChar = (int)c;
            int ligne = noChar / (1024 / charWidth);
            int col = noChar % (1024 / charWidth);
            setCharPos(x, 0.96f);
            x+=0.015f;
                    
            // Tex coord 
            vertices[2] = col * charWidth / 1024.0f;
            vertices[3] = (ligne + 1) * charHeight/ 1024.0f;
            vertices[6] = (col + 1) * charWidth/ 1024.0f;
            vertices[7] = (ligne + 1) * charHeight/ 1024.0f;
            vertices[10] = col * charWidth/ 1024.0f;
            vertices[11] = ligne * charHeight/ 1024.0f;
            vertices[14] = (col + 1) * charWidth/ 1024.0f;
            vertices[15] = ligne * charHeight/ 1024.0f;

            mesh.updateVertices(vertices);
            mesh.draw();            
        }
        
        glEnable(GL_DEPTH_TEST);
        
    }

    public void release() {
        texture.release();
    }
    
    private void setCharPos(float x, float y) {
        
        float width = 0.03f;
        float height = width * 16.0f / 9.0f;
         
        vertices[0]  = (x - 0.5f)*2;
        vertices[1]  = (y - 0.5f)*2;
        vertices[4]  = (x - 0.5f)*2+width;
        vertices[5]  = (y - 0.5f)*2;
        vertices[8]  = (x - 0.5f)*2;
        vertices[9]  = (y - 0.5f)*2+height;
        vertices[12] = (x - 0.5f)*2+width;
        vertices[13] = (y - 0.5f)*2+height;
    }

}
