package org.peekmoon.bomberman.server;

import java.io.IOException;
import java.net.DatagramSocket;

import org.peekmoon.bomberman.model.Lobby;
import org.peekmoon.bomberman.network.command.Command;
import org.peekmoon.bomberman.server.network.CommandReceiver;
import org.peekmoon.bomberman.server.network.StatusSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainServer {

    private final static Logger log = LoggerFactory.getLogger(MainServer.class);
    private final static long period = 20;

    public static void main(String[] args) {
        log.info("Sarting bomberman server...");
        new MainServer().start();
    }

    public void start() {

        CommandReceiver receiver = new CommandReceiver();
        new Thread(receiver, "Command receiver").start();

        try (DatagramSocket socket = new DatagramSocket()) {
            GameRepository gameRepository = new GameRepository();
            
            Lobby lobby = new Lobby();
            StatusSender statusSender = new StatusSender(socket);
            Clients clients = new Clients(lobby, gameRepository, statusSender);

            long lastTick = System.currentTimeMillis();
            while (true) {
                /// Wait next tick
                lastTick = waitTick(lastTick);
                
                // Apply modifications received from clients
                for (Command command : receiver.next()) {
                    clients.apply(command);
                }
                
                // Update games state
                gameRepository.update();
                
                // Send clients subscribe information
                clients.sendStatus();

            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private long waitTick(long lastTick) {
        long currentTick = System.currentTimeMillis();
        long delayFromLast = lastTick - currentTick;
        if (delayFromLast > period) {
            log.warn("Too late " + lastTick + " -> " + currentTick);
        } else if (delayFromLast < period) {
            try {
                Thread.sleep(period - delayFromLast);
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
        }
        return System.currentTimeMillis();

    }

}
