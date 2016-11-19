package org.peekmoon.bomberman.network.command;

import java.nio.ByteBuffer;

public class CreateGameCommand extends Command {
    
    public CreateGameCommand() {
        // Nothing to do
    }

    
    public CreateGameCommand(ByteBuffer buffer) {
        // Nothing to do
    }

    @Override
    protected void fillData(ByteBuffer buffer) {
        // Nothing to do
    }


    @Override
    public void apply(CommandListener listener) {
        listener.createGame(this);
    }

}
