package org.peekmoon.bomberman.network.command;

import java.net.InetAddress;
import java.nio.ByteBuffer;

public class RegisterCommand extends Command {
    
    // Port and address are not serialized and set by command receiver from datagrampacket metainformation
    private int port;
    private InetAddress address;
    
    public RegisterCommand() {
        // Nothing to do
    }
    
    public RegisterCommand(ByteBuffer buffer) {
        // Nothing to do
    }


    @Override
    protected void fillData(ByteBuffer buffer) {
    }

    public void setPort(int port) {
        this.port = port;
    }
    
    public int getPort() {
        return port;
    }

    public void setAddress(InetAddress address) {
        this.address = address;
    }
    
    public InetAddress getAddress() {
        return address;
    }

}
