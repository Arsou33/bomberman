package org.peekmoon.bomberman.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StatusReceiver implements Runnable, AutoCloseable {
    
    private final static Logger log = LoggerFactory.getLogger(StatusReceiver.class);
    
    // TODO : Mutualize all bufferSize
    private final static int bufferSize = 1024;
    
    private final DatagramSocket socket;
    private final ByteBuffer byteBuffer = ByteBuffer.allocate(bufferSize);
    private final DatagramPacket data = new DatagramPacket(byteBuffer.array(), bufferSize);
    
    private boolean running;
    private Message lastStatus;
    
    public StatusReceiver(DatagramSocket socket) {
        this.socket = socket;
        this.running = true;
    }
    
    @Override
    public void run() {
        log.info("Sarting status receiver...");
        while (running) {
             next();
        }        
        log.info("Status receiver stopped");
    }
    
    public void next() {
        try {
            socket.receive(data);
            byteBuffer.position(0);
            byteBuffer.limit(data.getLength());
            Message currentStatus = Message.extractFrom(data, byteBuffer);
            if (currentStatus.isAfter(lastStatus)) { // Ignore too old status
                lastStatus = currentStatus;
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
    
    public <T extends Message> T getStatus(Class<T> clazz) {
        try {
            int i = 30;
            while (i-- > 0) {
                if (clazz.isInstance(lastStatus)) return clazz.cast(lastStatus);
                // Wait to received correct status
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
        throw new IllegalStateException("Waiting for " + clazz + " last received is " + lastStatus.getClass());
    }

    @Override
    public void close() throws Exception {
        running = false;
    }

}
