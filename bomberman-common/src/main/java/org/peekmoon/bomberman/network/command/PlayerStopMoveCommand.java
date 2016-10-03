package org.peekmoon.bomberman.network.command;

import java.nio.ByteBuffer;

import org.peekmoon.bomberman.network.Direction;

public class PlayerStopMoveCommand extends Command {
    
    private final Direction direction;
    
    public PlayerStopMoveCommand(Direction direction) {
        this.direction = direction;
    }
    
    public PlayerStopMoveCommand(ByteBuffer buffer) {
        this(Direction.values()[buffer.getInt()]);
    }

    @Override
    protected void fillData(ByteBuffer buffer) {
        buffer.putInt(direction.ordinal());
    }
    
    public Direction getDirection() {
        return direction;
    }

}
