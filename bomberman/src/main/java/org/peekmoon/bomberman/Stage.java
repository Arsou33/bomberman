package org.peekmoon.bomberman;

import org.peekmoon.bomberman.key.KeyManager;

public abstract class Stage {
    
    public enum Name {
        CREATE_OR_JOIN_MENU,
        LOBBY,
        WAITING_ROOM,
        BOARD,  
    }

    private Name next;
    
    public Stage() {
        this.next = getName();
    }
    
    abstract public void init();
    abstract public void render();
    abstract public void release();
    
    abstract public KeyManager getKeyManager();

    abstract protected Name getName();
    
    public final Name next() {
        return next;
    }

    public boolean is(Name name) {
        return getName() == name;
    }
    
    public void switchToStage(Name stage) {
        this.next = stage;
    }
    

}
