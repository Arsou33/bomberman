package org.peekmoon.bomberman.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.ByteBuffer;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StatusReceiver implements Runnable {
    
    private final static Logger log = LoggerFactory.getLogger(StatusReceiver.class);
    
    // TODO : Mutualize all bufferSize
    private final static int bufferSize = 1024;

    private final DatagramSocket socket;
    private final ByteBuffer byteBuffer = ByteBuffer.allocate(bufferSize);
    private final DatagramPacket data = new DatagramPacket(byteBuffer.array(), bufferSize);
    
    private PlayerStatus status;
    
    public StatusReceiver(DatagramSocket socket) {
        this.socket = socket;
    }
    
    @Override
    public void run() {
        log.info("Sarting status receiver...");
        // TODO : Add port to configuration
        while (true) {
             next();
        }        
        //log.info("Finish status receiver");
    }
    
    public void next() {
        try {
            socket.receive(data);
            //log.debug("Received status of length {} : {} ", data.getLength(), Arrays.copyOfRange(data.getData(), 0, data.getLength()));
            byteBuffer.position(0);
            byteBuffer.limit(data.getLength());
            status = new PlayerStatus(byteBuffer);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
    
    public PlayerStatus getStatus() {
        return status;
    }
    

}
