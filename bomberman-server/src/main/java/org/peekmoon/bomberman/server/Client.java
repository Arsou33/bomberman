package org.peekmoon.bomberman.server;

import java.io.IOException;
import java.net.InetAddress;
import java.time.Duration;
import java.time.Instant;

import org.peekmoon.bomberman.model.Game;
import org.peekmoon.bomberman.model.Lobby;
import org.peekmoon.bomberman.model.Player;
import org.peekmoon.bomberman.model.WaitingRoom;
import org.peekmoon.bomberman.network.Message;
import org.peekmoon.bomberman.network.command.CommandListener;
import org.peekmoon.bomberman.network.command.CreateGameCommand;
import org.peekmoon.bomberman.network.command.CreateWaitingRoomCommand;
import org.peekmoon.bomberman.network.command.EnteringLobbyCommand;
import org.peekmoon.bomberman.network.command.PingCommand;
import org.peekmoon.bomberman.network.command.PlayerDropBombCommand;
import org.peekmoon.bomberman.network.command.PlayerStartMoveCommand;
import org.peekmoon.bomberman.network.command.PlayerStopMoveCommand;
import org.peekmoon.bomberman.network.command.RegisterCommand;
import org.peekmoon.bomberman.server.network.StatusSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Client implements CommandListener {
    
    private final static Logger log = LoggerFactory.getLogger(Client.class);

    private final Lobby lobby;
    private final GameRepository gameRepository;
    private final StatusSender statusSender;
    private final InetAddress address;
    private final int port;
    
    private Instant lastSeen;
    private Message currentSubscription;
    
    private WaitingRoom waitingRoom;
    private Game game;
    private Player player;

    public Client(Lobby lobby, GameRepository gameRepository, StatusSender statusSender, RegisterCommand registerCommand) {
        this.gameRepository = gameRepository;
        this.statusSender = statusSender;
        this.address = registerCommand.getAddress();
        this.port = registerCommand.getPort();
        this.lastSeen = Instant.now();
        this.lobby = lobby;
        
        this.currentSubscription = null;
    }
    
    ////////////////////////////
    // Helpers Methods
    ////////////////////////////

    public void sendStatus()  {
        try {
            if (currentSubscription != null) {
                statusSender.send(currentSubscription, this);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Error on send status for client " + this, e);
        }
    }

    public boolean hasSent(Message command) {
        return command.getAddress().equals(address) && command.getPort() == port;
    }
    
    public boolean notSeen(Duration ofSeconds) {
        return Instant.now().isAfter(lastSeen.plus(ofSeconds));
    }
    
    ////////////////////////////
    // Command Listener Methods
    ////////////////////////////
    
    @Override
    public void register(RegisterCommand command) {
        throw new IllegalStateException("Client already registered " + this);
    }

    @Override
    public void ping(PingCommand ping) {
        lastSeen = Instant.now();
    }
    
    @Override
    public void createWaitingRoom(CreateWaitingRoomCommand command) {
        if (game!=null) throw new IllegalStateException("Client " + this + " try to create waiting room when already in game " + game);
        if (waitingRoom!=null) throw new IllegalStateException("Client " + this + " try to create waiting room when already in waiting room " + waitingRoom);
        
        currentSubscription = waitingRoom = lobby.createWaitingRoom();
        log.info("Client {} created waiting room {}", this, waitingRoom);
    }
    
    @Override
    public void enteringLobby(EnteringLobbyCommand command) {
        if (game!=null) throw new IllegalStateException("Client " + this + " try to entering lobby when already in game " + game);
        if (waitingRoom!=null) throw new IllegalStateException("Client " + this + " try to entering lobby when already in waiting room " + waitingRoom);
        
        log.info("Client {} entering lobby", this);
        currentSubscription = lobby;
    }
    
    @Override
    public void createGame(CreateGameCommand createGameCommand) {
        if (game!=null) throw new IllegalStateException("Client " + this + " try to creating game when already in game " + game);
        
        log.info("Client {} creating game", this);
        currentSubscription = game = gameRepository.createGame();
        player = game.getPlayers().get(0);
    }

    // In game methods

    @Override
    public void playerDropBomb(PlayerDropBombCommand command) {
        player.dropBomb();
    }

    @Override
    public void playerStartMove(PlayerStartMoveCommand command) {
        player.startMove(command.getDirection());
    }

    @Override
    public void playerStopMove(PlayerStopMoveCommand command) {
        player.stopMove(command.getDirection());
    }
    
    ////////////////////////////
    // Getter and Setter
    ////////////////////////////

    public InetAddress getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    ////////////////////////////
    // toString Methods
    ////////////////////////////

    @Override
    public String toString() {
        return "Client [address=" + address + ", port=" + port + "]";
    }


}
