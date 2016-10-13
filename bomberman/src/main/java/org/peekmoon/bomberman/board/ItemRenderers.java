package org.peekmoon.bomberman.board;

import org.peekmoon.bomberman.model.BombItem;
import org.peekmoon.bomberman.model.BrickItem;
import org.peekmoon.bomberman.model.FireItem;
import org.peekmoon.bomberman.model.Item;
import org.peekmoon.bomberman.model.WoodItem;
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
    public void render(Item itemStatus) {
    	switch (itemStatus.getType()) {
		case BRICK:
			brickItemRenderer.render((BrickItem) itemStatus);
			break;
		case WOOD:
			woodItemRenderer.render((WoodItem) itemStatus);
			break;
		case BOMB:
			bombItemRenderer.render((BombItem) itemStatus);
			break;
		case FIRE:
			fireItemRenderer.render((FireItem) itemStatus);
			break;
		default:
			throw new IllegalStateException("Item status type is unknow to renderer " + itemStatus.getType());
    	
    	}
    }
    
    public void release() {
    	brickItemRenderer.release();
    	woodItemRenderer.release();
    	bombItemRenderer.release();
    	fireItemRenderer.release();
    }


}
