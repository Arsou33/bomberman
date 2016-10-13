package org.peekmoon.bomberman.network.command;

import java.nio.ByteBuffer;

public class RegisterCommand extends Command {
    
    
    public RegisterCommand() {
        // Nothing to do
    }
    
    public RegisterCommand(ByteBuffer buffer) {
        // Nothing to do
    }


    @Override
    protected void fillData(ByteBuffer buffer) {
    }

}
