package org.peekmoon.bomberman.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import org.peekmoon.bomberman.model.Direction;
import org.peekmoon.bomberman.network.command.Command;
import org.peekmoon.bomberman.network.command.PingCommand;
import org.peekmoon.bomberman.network.command.PlayerDropBombCommand;
import org.peekmoon.bomberman.network.command.PlayerStartMoveCommand;
import org.peekmoon.bomberman.network.command.PlayerStopMoveCommand;
import org.peekmoon.bomberman.network.command.RegisterCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandSender {
    
    private final static Logger log = LoggerFactory.getLogger(CommandSender.class);

    private final InetAddress server;
    private final int port;
    private final DatagramSocket socket; 
    private final ByteBuffer buffer;
    
    private Thread heartBeatThread;
    
    public CommandSender(DatagramSocket socket, String servername, int port) {
        try {
            this.socket = socket;
            this.buffer = ByteBuffer.allocate(1000);
            this.server = InetAddress.getByName(servername);
            this.port = port;
        } catch (UnknownHostException  e) {
            throw new IllegalArgumentException(servername, e);
        }
    }
    
    public void register() {
        log.debug("Trying to register on server {}:{}", server.getHostAddress(), port);
        send(new RegisterCommand());
        heartBeatThread = new Thread(new HeartBeat(this), "heartbeat");
        heartBeatThread.setDaemon(true);
        heartBeatThread.start();
    }
    
    void ping() {
        send(new PingCommand());
    }
    
    public void playerStartMove(Direction direction) {
        send(new PlayerStartMoveCommand(direction));
    }
    
    public void playerStopMove(Direction direction) {
        send(new PlayerStopMoveCommand(direction));
    }

    public void playerDropBomb() {
    	send(new PlayerDropBombCommand());
	}

    private synchronized void send(Command command) {
        try {
            buffer.clear();
            command.fill(buffer);
            buffer.flip();
            log.debug("Send command {} to server with size {}", command, buffer.limit());
            DatagramPacket commandPacket = new DatagramPacket(buffer.array(),buffer.limit(),server,port); 
            socket.send(commandPacket);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        } 

    }


    
}
