package org.peekmoon.bomberman.network.command;

import java.nio.ByteBuffer;

public class PingCommand extends Command {
    
    public PingCommand() {
        // Nothing to do
    }

    
    public PingCommand(ByteBuffer buffer) {
        // Nothing to do
    }

    @Override
    protected void fillData(ByteBuffer buffer) {
        // Nothing to do
    }

}
