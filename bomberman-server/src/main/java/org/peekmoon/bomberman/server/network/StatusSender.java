package org.peekmoon.bomberman.server.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.ByteBuffer;

import org.peekmoon.bomberman.network.Message;
import org.peekmoon.bomberman.server.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StatusSender {
    
    private final static Logger log = LoggerFactory.getLogger(StatusSender.class);
    private final int bufferSize = 1000; // TODO : Remove numeric (factorize)

    private final ByteBuffer buffer;
    private final DatagramSocket socket;
    
    public StatusSender(DatagramSocket socket) {
        this.buffer = ByteBuffer.allocate(bufferSize);
        this.socket = socket;
    }
    
    public void send(Message message, Client client) throws IOException {
        buffer.clear();
        message.fill(buffer);
        log.debug("Send message {} to {} size = {}", message.getType(), client, buffer.position());
        DatagramPacket packet = new DatagramPacket(buffer.array(), buffer.position(), client.getAddress(), client.getPort());
        socket.send(packet);
    }

}
