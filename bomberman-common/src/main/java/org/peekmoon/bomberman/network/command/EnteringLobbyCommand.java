package org.peekmoon.bomberman.network.command;

import java.nio.ByteBuffer;

public class EnteringLobbyCommand extends Command  {
    
    public EnteringLobbyCommand() {
        // Nothing to do
    }
    
    public EnteringLobbyCommand(ByteBuffer buffer) {
        // Nothing to do
    }

    @Override
    protected void fillData(ByteBuffer buffer) {
    }

    @Override
    public void apply(CommandListener listener) {
        listener.enteringLobby(this);
    }

}
