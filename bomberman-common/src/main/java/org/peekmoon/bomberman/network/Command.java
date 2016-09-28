package org.peekmoon.bomberman.network;

import java.lang.reflect.Constructor;
import java.nio.ByteBuffer;

public abstract class Command {
    
    protected abstract void fillData(ByteBuffer buffer);
    
    public static Command extractFrom(ByteBuffer buffer) {
        try {
            CommandType type = CommandType.values()[buffer.getInt()];
             Constructor<? extends Command> ctor = type.getCommandClass().getConstructor(ByteBuffer.class);
             ctor.setAccessible(true);
             return ctor.newInstance(buffer);
        } catch (SecurityException | ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }

    public void fill(ByteBuffer buffer) {
        buffer.putInt(CommandType.get(this).ordinal());
        fillData(buffer);
    }
    
    public CommandType getType() {
        return CommandType.get(this);
    }

}
