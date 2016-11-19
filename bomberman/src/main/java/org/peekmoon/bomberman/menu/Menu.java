package org.peekmoon.bomberman.menu;

import java.util.Arrays;
import java.util.List;

import org.peekmoon.bomberman.TextRenderer;

public class Menu {
    
    private final TextRenderer textRenderer;
    private final List<MenuItem> items;
    
    private int selected;

    public Menu(TextRenderer textRenderer, MenuItem... items) {
        this.textRenderer = textRenderer;
        this.items = Arrays.asList(items);
        selected = 0;
        items[0].highlight(true);
        float y = 1;
        for (MenuItem item : items) {
            item.setPos(-0.4f, y = y-0.5f);
        }
    }

    public void render() {
        for (MenuItem item : items) {
            item.render(textRenderer);
        }
    }
    
    public MenuItem getCurrent() {
        return items.get(selected);
    }
    
    public void selectNext() {
        getCurrent().highlight(false);
        selected = Math.min(items.size()-1, selected+1);
        getCurrent().highlight(true);
    }
    
    public void selectPrevious() {
        getCurrent().highlight(false);
        selected = Math.max(0, selected-1);
        getCurrent().highlight(true);
    }
    
    
}
