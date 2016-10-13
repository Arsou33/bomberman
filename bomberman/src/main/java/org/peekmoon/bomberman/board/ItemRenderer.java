package org.peekmoon.bomberman.board;

import org.peekmoon.bomberman.model.Item;

public abstract class ItemRenderer<T extends Item> {
    
    public abstract void render(T itemStatus);
	public abstract void release();
     
}
