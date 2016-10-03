package org.peekmoon.bomberman.board;

import java.util.HashMap;
import java.util.Map;

import org.peekmoon.bomberman.Main;
import org.peekmoon.bomberman.network.status.ItemStatus;
import org.peekmoon.bomberman.network.status.ItemType;
import org.peekmoon.bomberman.shader.ProgramShader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ItemRenderer<T extends ItemStatus> {
    
    private final static Logger log = LoggerFactory.getLogger(Main.class);
    
    public abstract void render(T itemStatus);
        
     
}
