package org.peekmoon.bomberman.model;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.peekmoon.bomberman.network.Message;

public class Lobby extends Message {
    
    private final List<WaitingRoom> waitingRooms = new ArrayList<>();
    
    public Lobby() {
    }

    public Lobby(ByteBuffer buffer) {
    }

    @Override
    public void fillData(ByteBuffer buffer) {
    }

    public WaitingRoom createWaitingRoom() {
        WaitingRoom waitingRoom = new WaitingRoom();
        waitingRooms.add(waitingRoom);
        return waitingRoom;
    }

}
