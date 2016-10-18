package org.peekmoon.bomberman;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.io.IOException;

import org.peekmoon.bomberman.opengl.GLIndexedMesh;
import org.peekmoon.bomberman.opengl.GLTexture;
import org.peekmoon.bomberman.shader.ProgramShader;


public class FpsRenderer {

    private final float[] vertices = new float[] {
            -0.5f,-0.5f, 0,1, 
            0.5f,-0.5f,  1,1,
            -0.5f,0.5f,  0,0, 
            0.5f,0.5f,   1,0
    };
    
    private final GLTexture texture = new GLTexture("test.png");
    private final GLIndexedMesh mesh = new GLIndexedMesh( // TODO : Check if an GLMesh should be better else merge the two ?
        vertices, 
        new short[] {0,1,2, 1,2,3},
        texture, 2, 2
    );
    
    public FpsRenderer(ProgramShader shader) {
        
        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT , getClass().getResourceAsStream("/SmallTypeWriting.ttf"));
            font = font.deriveFont(12.0f);
            FontRenderContext frc = new FontRenderContext(null, true, true);
            
            TextLayout textLayout = new TextLayout("Chaine de test", font, frc);
            Rectangle2D rect = textLayout.getBounds();
            System.out.println("Rect : " + rect);
            
            String chaine = "pb Ma chaîne avec des i &";
            for (char c : chaine.toCharArray()) {
                TextLayout textLayout2 = new TextLayout(Character.toString(c), font, frc);
                Rectangle2D rect2 =  textLayout2.getBounds();
                System.out.println("C : " + c + " Rect : " + rect2);
            }
        
        } catch (FontFormatException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        
        
        
    }

    public void render() {
        int noChar = 12;
        int ligne = noChar/8;
        int col = noChar%8;
        float charSize = 1.0f/8;
                
        vertices[2] = col * charSize;
        vertices[3] = (ligne+1) * charSize;
        vertices[6] = (col+1) * charSize;
        vertices[7] = (ligne+1) * charSize;
        vertices[10] = col * charSize;
        vertices[11] = ligne * charSize;
        vertices[14] = (col+1) * charSize;
        vertices[15] = ligne * charSize;
        
        mesh.updateVertices(vertices);
        mesh.draw();
    }

    public void release() {
        texture.release();
    }

}
