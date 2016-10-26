package org.peekmoon.bomberman.server;

import java.time.Duration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.peekmoon.bomberman.network.command.Command;
import org.peekmoon.bomberman.network.command.PingCommand;
import org.peekmoon.bomberman.network.command.RegisterCommand;
import org.peekmoon.bomberman.server.network.StatusSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Clients {

    private final static Logger log = LoggerFactory.getLogger(Clients.class);

    private final StatusSender statusSender;
    private final List<Client> clients = new LinkedList<>();
    
    private int noNextPlayer;
    

    public Clients(StatusSender statusSender) {
        this.statusSender = statusSender;
        this.noNextPlayer = 0;
    }

    public void register(RegisterCommand registerCommand) {
        Client client = new Client(registerCommand, statusSender, noNextPlayer);
        if (noNextPlayer!=-1) {
            if (++noNextPlayer>1) noNextPlayer = -1;
        } 
        clients.add(client);
        log.info("New client is registering : " + client);
    }
    
    public void apply(PingCommand command) {
        getClient(command).refresh();
    }

    public void sendStatus() {
        Iterator<Client> it = clients.iterator();
        while (it.hasNext()) {
            Client client = it.next();
            if (client.notSeen(Duration.ofSeconds(10))) {
                log.info("Client {} is removed becaused not seen", client );
                it.remove();
            } else {
                client.sendStatus();
            }
        }
    }

    public Client getClient(Command command) {
        for (Client client : clients) {
            if (client.hasSent(command)) return client;
        };
        throw new IllegalStateException("A command is received for an unknown client " + command);
    }



}
