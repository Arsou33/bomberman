package org.peekmoon.bomberman.menu;

import org.peekmoon.bomberman.Stage;
import org.peekmoon.bomberman.TextRenderer;
import org.peekmoon.bomberman.key.KeyManager;

public class MenuStage extends Stage {
    
    private final long window;
    private final TextRenderer textRenderer;
    
    private MenuKeyManager keyManager;
    private Name next;
    
    
    public MenuStage(long window, TextRenderer textRenderer) {
        this.next = getName();
        this.window = window;
        this.textRenderer = textRenderer;
    }

    @Override
    public void init() {
        keyManager = new MenuKeyManager(window, this);        
    }

    @Override
    public Name next() {
        return next;
    }

    @Override
    public void render() {
        float y = -0.50f;
        float size = 10;
        for (int i=1; i<15; i++) {
            y+=size/800;
            size *= 1.5;
            textRenderer.render(-0.5f, y , size, String.format("Je suis un texte de taille : %04.0f", size));
        }
    }

    @Override
    public void release() {
    }

    @Override
    public KeyManager getKeyManager() {
        return keyManager;
    }

    public void setNext(Name name) {
        next = name;
    }

    @Override
    protected Name getName() {
        return Name.MENU;
    }

}
