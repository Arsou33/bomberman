package org.peekmoon.bomberman.board;

import org.peekmoon.bomberman.network.status.BrickItemStatus;
import org.peekmoon.bomberman.network.status.ItemStatus;
import org.peekmoon.bomberman.network.status.WoodItemStatus;
import org.peekmoon.bomberman.shader.ProgramShader;

public class ItemRenderers {
	
	private BrickItemRenderer brickItemRenderer;
	private WoodItemRenderer woodItemRenderer;

	
    public ItemRenderers(ProgramShader shader) {
    	brickItemRenderer = new BrickItemRenderer(shader);
    	woodItemRenderer = new WoodItemRenderer(shader);
    }
    
    public void render(ItemStatus itemStatus) {
    	switch (itemStatus.getType()) {
		case BRICK:
			brickItemRenderer.render((BrickItemStatus) itemStatus);
			break;
		case WOOD:
			woodItemRenderer.render((WoodItemStatus) itemStatus);
			break;
		default:
			throw new IllegalStateException("Item status type is unknow to renderer " + itemStatus.getType());
    	
    	}
    }


}
