package org.peekmoon.bomberman.server.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;

import org.peekmoon.bomberman.model.Game;
import org.peekmoon.bomberman.network.command.RegisterCommand;

public class StatusSender {
    
    private final int bufferSize = 1000; // TODO : Remove numeric (factorize)

    private final InetAddress address;
    private final int port;
    private final ByteBuffer buffer;
    private final Game status;
    private final DatagramSocket socket;
    
    public StatusSender(Game status, DatagramSocket socket, RegisterCommand registerCommand) {
        this.status = status;
        this.address = registerCommand.getAddress();
        this.port = registerCommand.getPort();
        this.buffer = ByteBuffer.allocate(bufferSize);
        this.socket = socket;
    }
    
    public void send() throws IOException {
        buffer.clear();
        status.fill(buffer);
        DatagramPacket packet = new DatagramPacket(buffer.array(), buffer.position(), address, port);
        socket.send(packet);
    }

}
