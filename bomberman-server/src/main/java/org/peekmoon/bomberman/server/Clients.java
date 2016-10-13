package org.peekmoon.bomberman.server;

import java.util.ArrayList;
import java.util.List;

import org.peekmoon.bomberman.network.command.Command;
import org.peekmoon.bomberman.network.command.RegisterCommand;
import org.peekmoon.bomberman.server.network.StatusSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Clients {

    private final static Logger log = LoggerFactory.getLogger(Clients.class);

    private final StatusSender statusSender;

    private final List<Client> clients = new ArrayList<>();

    public Clients(StatusSender statusSender) {
        this.statusSender = statusSender;
    }

    public void register(RegisterCommand registerCommand) {
        Client client = new Client(registerCommand, statusSender);
        log.info("New player is registering : " + client);
        clients.add(client);
    }

    public void sendStatus() {
        clients.forEach(client->client.sendStatus());
    }

    public int getId(Command command) {
        for (Client client : clients) {
            if (client.hasSent(command)) return clients.indexOf(client);
        };
        throw new IllegalStateException("A command is received for an unknown client " + command);
    }

}
