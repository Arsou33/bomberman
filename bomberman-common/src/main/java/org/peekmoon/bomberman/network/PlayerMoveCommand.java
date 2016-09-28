package org.peekmoon.bomberman.network;

import java.nio.ByteBuffer;

import org.peekmoon.bomberman.board.Direction;

public class PlayerMoveCommand extends Command {
    
    private final Direction direction;
    private final boolean start;
    
    public PlayerMoveCommand(Direction direction, boolean start) {
        this.direction = direction;
        this.start = start;
    }
    
    public PlayerMoveCommand(ByteBuffer buffer) {
        this(Direction.values()[buffer.getInt()], buffer.get() == 1);
    }

    @Override
    protected void fillData(ByteBuffer buffer) {
        buffer.putInt(direction.ordinal());
        buffer.put((byte)(start?1:0));
    }
    
    public Direction getDirection() {
        return direction;
    }

    public boolean isStart() {
        return start;
    }
    

}
