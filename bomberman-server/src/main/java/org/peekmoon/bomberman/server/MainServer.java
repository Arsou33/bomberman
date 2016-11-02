package org.peekmoon.bomberman.server;

import java.io.IOException;
import java.net.DatagramSocket;

import org.peekmoon.bomberman.network.command.Command;
import org.peekmoon.bomberman.network.command.PingCommand;
import org.peekmoon.bomberman.network.command.RegisterCommand;
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

            GameEngine engine = new GameEngine();
            StatusSender statusSender = new StatusSender(engine.getStatus(), socket);
            Clients clients = new Clients(statusSender);

            long lastTick = System.currentTimeMillis();
            while (true) {
                waitTick(lastTick);
                lastTick = System.currentTimeMillis();
                for (Command command : receiver.next()) {
                    switch (command.getType()) {
                    case REGISTER:
                        clients.register((RegisterCommand) command);
                        break;
                    case PING:
                        clients.apply((PingCommand)command);
                        break;
                    default:
                        Client client = clients.getClient(command);
                        if (client.isPlayer()) {
                            engine.apply(client.getNoPlayer(), command);
                        }
                        break;
                    }
                }
                ;

                engine.update();
                clients.sendStatus();

            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private void waitTick(long lastTick) {
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

    }

}
