package org.peekmoon.bomberman.board;

import org.peekmoon.bomberman.network.status.BombItemStatus;
import org.peekmoon.bomberman.network.status.BrickItemStatus;
import org.peekmoon.bomberman.network.status.FireItemStatus;
import org.peekmoon.bomberman.network.status.ItemStatus;
import org.peekmoon.bomberman.network.status.WoodItemStatus;
import org.peekmoon.bomberman.shader.ProgramShader;

public class ItemRenderers {
	
	private final BrickItemRenderer brickItemRenderer;
	private final WoodItemRenderer woodItemRenderer;
	private final BombItemRenderer bombItemRenderer;
	private final FireItemRenderer fireItemRenderer;

	
    public ItemRenderers(ProgramShader shader) {
    	brickItemRenderer = new BrickItemRenderer(shader);
    	woodItemRenderer = new WoodItemRenderer(shader);
    	bombItemRenderer = new BombItemRenderer(shader);
    	fireItemRenderer = new FireItemRenderer(shader);
    }

    // TODO : search better implementation
    public void render(ItemStatus itemStatus) {
    	switch (itemStatus.getType()) {
		case BRICK:
			brickItemRenderer.render((BrickItemStatus) itemStatus);
			break;
		case WOOD:
			woodItemRenderer.render((WoodItemStatus) itemStatus);
			break;
		case BOMB:
			bombItemRenderer.render((BombItemStatus) itemStatus);
			break;
		case FIRE:
			fireItemRenderer.render((FireItemStatus) itemStatus);
			break;
		default:
			throw new IllegalStateException("Item status type is unknow to renderer " + itemStatus.getType());
    	
    	}
    }


}
