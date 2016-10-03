package org.peekmoon.bomberman.network.command;

import java.nio.ByteBuffer;

import org.peekmoon.bomberman.network.Direction;

public class PlayerStartMoveCommand extends Command {
    
    private final Direction direction;
    
    public PlayerStartMoveCommand(Direction direction) {
        this.direction = direction;
    }
    
    public PlayerStartMoveCommand(ByteBuffer buffer) {
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
