package org.peekmoon.bomberman.board;

import org.peekmoon.bomberman.network.status.ItemStatus;

public abstract class ItemRenderer<T extends ItemStatus> {
    
    public abstract void render(T itemStatus);
     
}
