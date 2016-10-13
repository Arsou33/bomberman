package org.peekmoon.bomberman.server.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.ByteBuffer;

import org.peekmoon.bomberman.model.Game;
import org.peekmoon.bomberman.server.Client;

public class StatusSender {
    
    private final int bufferSize = 1000; // TODO : Remove numeric (factorize)

    private final ByteBuffer buffer;
    private final Game status;
    private final DatagramSocket socket;
    
    public StatusSender(Game status, DatagramSocket socket) {
        this.status = status;
        this.buffer = ByteBuffer.allocate(bufferSize);
        this.socket = socket;
    }
    
    public void send(Client client) throws IOException {
        buffer.clear();
        status.fill(buffer);
        DatagramPacket packet = new DatagramPacket(buffer.array(), buffer.position(), client.getAddress(), client.getPort());
        socket.send(packet);
    }

}
