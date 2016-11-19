package org.peekmoon.bomberman.network.command;

import java.nio.ByteBuffer;

public class PlayerDropBombCommand extends Command {
    
    public PlayerDropBombCommand() {
        // Nothing to do
    }

    
    public PlayerDropBombCommand(ByteBuffer buffer) {
        // Nothing to do
    }


    @Override
    public void apply(CommandListener listener) {
        listener.playerDropBomb(this);
    }

}
