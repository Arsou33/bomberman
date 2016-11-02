package org.peekmoon.bomberman;

import org.peekmoon.bomberman.key.KeyManager;

public interface Stage {
    
    void init();
    void render();
    void release();
    
    KeyManager getKeyManager();

}
