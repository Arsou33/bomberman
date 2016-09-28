package org.peekmoon.bomberman.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import org.peekmoon.bomberman.board.Direction;

public class CommandSender {
    
    private static final InetAddress server;
    private final DatagramSocket socket; 
    private final ByteBuffer buffer;
    
    static {
        String servername = "localhost";
        try {
            server = InetAddress.getByName(servername);
        } catch (UnknownHostException  e) {
            throw new IllegalArgumentException(servername, e);
        }
    }
    
    public CommandSender(DatagramSocket socket) {
        this.socket = socket;
        this.buffer = ByteBuffer.allocate(1000);
    }
    
    public void register() {
        send(new RegisterCommand());
    }
    
    public void playerStartMove(Direction direction) {
        send(new PlayerMoveCommand(direction, true));
    }
    
    public void playerStopMove(Direction direction) {
        send(new PlayerMoveCommand(direction, false));
    }

    public void playerDropBomb() {
    	send(new PlayerDropBombCommand());
	}

    public void send(Command command) {
        try {
            buffer.clear();
            command.fill(buffer);
            buffer.flip();
            DatagramPacket commandPacket = new DatagramPacket(buffer.array(),buffer.limit(),server,8232); 
            socket.send(commandPacket);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        } 

    }
    
}
