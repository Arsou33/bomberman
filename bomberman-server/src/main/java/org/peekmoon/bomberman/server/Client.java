package org.peekmoon.bomberman.server;

import java.io.IOException;
import java.net.InetAddress;

import org.peekmoon.bomberman.network.command.Command;
import org.peekmoon.bomberman.network.command.RegisterCommand;
import org.peekmoon.bomberman.server.network.StatusSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Client {
    
    private final static Logger log = LoggerFactory.getLogger(Client.class);

    private final StatusSender statusSender;
    private final InetAddress address;
    private final int port;
    
    private boolean living = true;

    public Client(RegisterCommand registerCommand, StatusSender statusSender) {
        this.address = registerCommand.getAddress();
        this.port = registerCommand.getPort();
        this.statusSender = statusSender;
    }

    public void sendStatus()  {
        if (!living) return;
        try {
            statusSender.send(this);
        } catch (IOException e) {
            log.warn("error on send status for client " + this);
            living = false;
        }
    }

    public boolean hasSent(Command command) {
        return command.getAddress().equals(address) && command.getPort() == port;
    }

    public InetAddress getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    @Override
    public String toString() {
        return "Client [address=" + address + ", port=" + port + "]";
    }

}
