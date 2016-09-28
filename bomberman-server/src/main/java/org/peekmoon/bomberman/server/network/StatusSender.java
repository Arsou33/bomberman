package org.peekmoon.bomberman.server.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;

import org.peekmoon.bomberman.network.PlayerStatus;
import org.peekmoon.bomberman.network.RegisterCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StatusSender {
    
    private final static Logger log = LoggerFactory.getLogger(StatusSender.class);
    private final int bufferSize = 1000;

    
    private final InetAddress address;
    private final int port;
    private final ByteBuffer buffer;
    private final PlayerStatus status;
    private final DatagramSocket socket;
    
    public StatusSender(PlayerStatus status, DatagramSocket socket, RegisterCommand registerCommand) {
        this.status = status;
        this.address = registerCommand.getAddress();
        this.port = registerCommand.getPort();
        this.buffer = ByteBuffer.allocate(bufferSize);
        this.socket = socket;
    }
    
    public void send() throws IOException {
        //log.debug("Send status");
        buffer.clear();
        status.fill(buffer);
        DatagramPacket packet = new DatagramPacket(buffer.array(), buffer.position(), address, port);
        socket.send(packet);
    }

}
