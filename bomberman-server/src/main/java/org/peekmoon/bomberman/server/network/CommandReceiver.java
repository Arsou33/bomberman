package org.peekmoon.bomberman.server.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.peekmoon.bomberman.network.Command;
import org.peekmoon.bomberman.network.RegisterCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//TODO : Check if we can mutualize xxxReceiver class
//TODO : Check if we can mutualize xxxSender class
public class CommandReceiver implements Runnable {
    
    private final static Logger log = LoggerFactory.getLogger(CommandReceiver.class);
    private final static int port = 8232;
    private final static int bufferSize = 1000;

    private final ByteBuffer byteBuffer = ByteBuffer.allocate(bufferSize);
    private final DatagramPacket data = new DatagramPacket(byteBuffer.array(), bufferSize);
    
    private final List<Command> commands = new ArrayList<>();
    
    public List<Command> next() {
        synchronized (commands) {
            List<Command> result = new ArrayList<>(commands);
            commands.clear();
            return result;
        }
    }
    
    @Override
    public void run() {
        try (DatagramSocket socket = new DatagramSocket(port)) {
            while (true) {
                socket.receive(data);
                log.debug("Received data of length {} : {} ", data.getLength(), Arrays.copyOfRange(data.getData(), 0, data.getLength()));
                byteBuffer.position(0);
                byteBuffer.limit(data.getLength());
                Command command = Command.extractFrom(byteBuffer);
                if (command instanceof RegisterCommand) {
                    RegisterCommand registerCommand = (RegisterCommand)command;
                    registerCommand.setPort(data.getPort());
                    registerCommand.setAddress(data.getAddress());
                }
                synchronized (commands) {
                    commands.add(command);
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException(e); 
        }
        
    }



}
