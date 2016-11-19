package org.peekmoon.bomberman.model;

import java.nio.ByteBuffer;

import org.peekmoon.bomberman.network.Message;

public class WaitingRoom extends Message {
    
    public WaitingRoom() {
    }

    public WaitingRoom(ByteBuffer buffer) {
    }

    @Override
    public void fillData(ByteBuffer buffer) {
    }

}
