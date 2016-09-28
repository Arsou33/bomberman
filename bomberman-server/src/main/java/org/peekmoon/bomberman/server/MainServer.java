package org.peekmoon.bomberman.server;

import java.io.IOException;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.List;

import org.peekmoon.bomberman.network.Command;
import org.peekmoon.bomberman.network.CommandType;
import org.peekmoon.bomberman.network.RegisterCommand;
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
        PlayerEngine engine = new PlayerEngine();
        List<StatusSender> statusSenders = new ArrayList<>();
        
        CommandReceiver receiver = new CommandReceiver();
        new Thread(receiver, "Command receiver").start();
        
        try (DatagramSocket socket = new DatagramSocket()) {
            long lastTick = System.currentTimeMillis(); 
            while (true) {
                waitTick(lastTick);
                lastTick = System.currentTimeMillis();
                for (Command command : receiver.next()) {
                    if (command.getType() == CommandType.REGISTER) {
                        RegisterCommand registerCommand = (RegisterCommand)command;
                        log.info("New player is registering : " + registerCommand.getAddress() + " on " + registerCommand.getPort());
                        statusSenders.add(new StatusSender(engine.getStatus(), socket, registerCommand));
                    } else {
                        engine.apply(command);
                    }
                };
                
                engine.update();
                
                for (StatusSender statusSender : statusSenders) {
                    statusSender.send();
                }
                
            }    
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private void waitTick(long lastTick) {
        long currentTick = System.currentTimeMillis();
        long delayFromLast = lastTick - currentTick;
        if (delayFromLast > period)  {
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
