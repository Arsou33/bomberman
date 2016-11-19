package org.peekmoon.bomberman.menu;

import org.peekmoon.bomberman.TextRenderer;

public class MenuItem {
    
    private final String label;
    private final Object data;
    
    private boolean highlight;
    private float x, y;
    
    public MenuItem(String label) {
        this(label, null);
    }
    
    public MenuItem(String label, Object data) {
        this.label = label;
        highlight = false;
        this.data = data;
    }

    
    public void render(TextRenderer textRenderer) {
        textRenderer.render(x, y, (highlight?180:120), label);
    }
    
    public <T> T getData(Class<T> clazz) {
        return clazz.cast(data);
    }

    void highlight(boolean b) {
        highlight = b;
    }
    
    void setPos(float x, float y) {
        this.x = x;
        this.y = y;
    }

    

}
