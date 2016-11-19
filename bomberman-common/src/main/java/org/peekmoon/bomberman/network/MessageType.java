package org.peekmoon.bomberman.network;

import org.peekmoon.bomberman.model.Game;
import org.peekmoon.bomberman.model.Lobby;
import org.peekmoon.bomberman.model.WaitingRoom;
import org.peekmoon.bomberman.network.command.CreateGameCommand;
import org.peekmoon.bomberman.network.command.CreateWaitingRoomCommand;
import org.peekmoon.bomberman.network.command.EnteringLobbyCommand;
import org.peekmoon.bomberman.network.command.PingCommand;
import org.peekmoon.bomberman.network.command.PlayerDropBombCommand;
import org.peekmoon.bomberman.network.command.PlayerStartMoveCommand;
import org.peekmoon.bomberman.network.command.PlayerStopMoveCommand;
import org.peekmoon.bomberman.network.command.RegisterCommand;

public enum MessageType {
    // Client->Server Command
    REGISTER(RegisterCommand.class),
    PING(PingCommand.class),
    CREATE_WAITING_ROOM(CreateWaitingRoomCommand.class),
    ENTERING_LOBBY(EnteringLobbyCommand.class),
    CREATE_GAME(CreateGameCommand.class),
    PLAYER_START_MOVE(PlayerStartMoveCommand.class),
    PLAYER_STOP_MOVE(PlayerStopMoveCommand.class),
    PLAYER_DROP_BOMB(PlayerDropBombCommand.class),
    
    // Server->Client status
    LOBBY(Lobby.class),
    WAITING_ROOM(WaitingRoom.class),
    GAME(Game.class)
    ;

    private Class<? extends Message> clazz;

    private MessageType(Class<? extends Message> clazz) {
        this.clazz = clazz;
    }
    
    public static MessageType get(Message command) {
        Class<? extends Message> commandClass = command.getClass();
        for (MessageType type : MessageType.values()) {
            if (type.clazz.equals(commandClass)) return type;
        }
        throw new IllegalStateException("Command class " + commandClass.getName() + " is unknown");
    }
    
    public Class<? extends Message> getMessageClass() {
        return clazz;
    }

}
