package org.peekmoon.bomberman.server;

import java.io.IOException;
import java.net.InetAddress;
import java.time.Duration;
import java.time.Instant;

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
    private final int noPlayer;
    
    private boolean living = true;
    private Instant lastSeen;

    public Client(RegisterCommand registerCommand, StatusSender statusSender, int noPlayer) {
        this.address = registerCommand.getAddress();
        this.port = registerCommand.getPort();
        this.statusSender = statusSender;
        this.noPlayer = noPlayer;
        this.lastSeen = Instant.now();
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
        return "Client [noPlayer=" + noPlayer + ", address=" + address + ", port=" + port + "]";
    }

    public boolean isPlayer() {
        return noPlayer != -1;
    }

    public int getNoPlayer() {
        return noPlayer;
    }

    public void refresh() {
        lastSeen = Instant.now();
    }

    public boolean notSeen(Duration ofSeconds) {
        return Instant.now().isAfter(lastSeen.plus(ofSeconds));
    }

}
