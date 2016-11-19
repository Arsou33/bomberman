package org.peekmoon.bomberman.menu;

import org.peekmoon.bomberman.Stage;
import org.peekmoon.bomberman.TextRenderer;
import org.peekmoon.bomberman.key.KeyManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateOrJoinStage extends Stage {
    
    private final static Logger log = LoggerFactory.getLogger(CreateOrJoinStage.class);
    
    private final long window;
    private final Menu menu;
    private CreateOrJoinKeyManager keyManager;

    public CreateOrJoinStage(long window, TextRenderer textRenderer) {
        this.window = window;
        menu = new Menu(textRenderer, 
                new MenuItem("Create a new Game", Name.WAITING_ROOM), 
                new MenuItem("Join an existing Game", Name.LOBBY)
        );
    }

    @Override
    public void init() {
        keyManager = new CreateOrJoinKeyManager(window, this);
    }

    @Override
    public void render() {
        menu.render();
    }

    @Override
    public void release() {
    }

    @Override
    public KeyManager getKeyManager() {
        return keyManager;
    }

    @Override
    protected Name getName() {
        return Name.CREATE_OR_JOIN_MENU;
    }
    
    Menu getMenu() {
        return menu;
    }
    
    public void selectMenu() {
        switchToStage(menu.getCurrent().getData(Name.class));
    }

}
