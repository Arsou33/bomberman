package org.peekmoon.bomberman.network.command;

import java.nio.ByteBuffer;

import org.peekmoon.bomberman.network.Message;

public abstract class Command extends Message {
    
    public abstract void apply(CommandListener listener);
    
    @Override
    protected void fillData(ByteBuffer buffer) {
        // By default nothing to do
    }

}
