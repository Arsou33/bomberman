package org.peekmoon.bomberman.network.command;

import java.nio.ByteBuffer;

public class CreateWaitingRoomCommand extends Command {
    
    public CreateWaitingRoomCommand() {
        // Nothing to do
    }

    
    public CreateWaitingRoomCommand(ByteBuffer buffer) {
        // Nothing to do
    }

    @Override
    protected void fillData(ByteBuffer buffer) {
        // Nothing to do
    }

    @Override
    public void apply(CommandListener listener) {
        listener.createWaitingRoom(this);
    }

}
