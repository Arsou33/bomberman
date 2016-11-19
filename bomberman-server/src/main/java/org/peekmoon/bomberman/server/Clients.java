package org.peekmoon.bomberman.server;

import java.time.Duration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.peekmoon.bomberman.model.Lobby;
import org.peekmoon.bomberman.network.Message;
import org.peekmoon.bomberman.network.MessageType;
import org.peekmoon.bomberman.network.command.Command;
import org.peekmoon.bomberman.network.command.RegisterCommand;
import org.peekmoon.bomberman.server.network.StatusSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Clients {

    private final static Logger log = LoggerFactory.getLogger(Clients.class);

    private final Lobby lobby;
    private final GameRepository gameRepository; 
    private final StatusSender statusSender;
    
    private final List<Client> clients = new LinkedList<>();
    
    public Clients(Lobby lobby, GameRepository gameRepository, StatusSender statusSender) {
        this.lobby = lobby;
        this.gameRepository = gameRepository;
        this.statusSender = statusSender;
    }

    public void apply(Command command) {
        if (command.getType() == MessageType.REGISTER) {
            register(command.getAs(RegisterCommand.class));
        } else {
            apply(command, getClient(command));
        }
    }

    private void apply(Command command, Optional<Client> client) {
        try {
            client.ifPresent(c->command.apply(c));
        } catch (Exception e) {
            log.error("Client throw exception when applying command => revoking it " + client + " " + command, e);
            if (!clients.remove(client)) {
                log.error("Unable to remove client");
            };
        }
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
    
    private void register(RegisterCommand registerCommand) {
        Client client = new Client(lobby, gameRepository, statusSender, registerCommand);
        clients.add(client);
        log.info("New client is registering : " + client);
    }

    private Optional<Client> getClient(Message message) {
        for (Client client : clients) {
            if (client.hasSent(message)) return Optional.of(client);
        };
        log.warn("A command is received for an unknown client " + message);
        return Optional.empty();
    }

    

}
