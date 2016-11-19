package org.peekmoon.bomberman.network.command;

public interface CommandListener {

    void register(RegisterCommand command);
    void ping(PingCommand command);
    void enteringLobby(EnteringLobbyCommand command);
    void createWaitingRoom(CreateWaitingRoomCommand createLobbyCommand);
    void playerDropBomb(PlayerDropBombCommand command);
    void playerStartMove(PlayerStartMoveCommand command);
    void playerStopMove(PlayerStopMoveCommand command);
    void createGame(CreateGameCommand createGameCommand);

}
