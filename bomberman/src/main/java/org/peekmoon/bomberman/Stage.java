package org.peekmoon.bomberman;

import org.peekmoon.bomberman.key.KeyManager;

public abstract class Stage {
    
    public enum Name {
        MENU,
        BOARD
    }
    
    abstract public void init();
    abstract public void render();
    abstract public void release();
    
    abstract public KeyManager getKeyManager();
    abstract public Name next();
    

    abstract protected Name getName();
    
    public boolean is(Name name) {
        return getName() == name;
    }
    

}
