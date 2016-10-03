package org.peekmoon.bomberman.server;

import org.peekmoon.bomberman.network.command.Command;
import org.peekmoon.bomberman.network.command.PlayerStartMoveCommand;
import org.peekmoon.bomberman.network.command.PlayerStopMoveCommand;
import org.peekmoon.bomberman.network.status.GameStatus;

public class GameEngine {
	
	private final GameStatus status;
	
	private final BoardEngine boardEngine;
	private final PlayerEngine playerEngine;
	
	public GameEngine() {
		this.boardEngine = new BoardEngine();
		this.playerEngine = new PlayerEngine();
		this.status = new GameStatus(boardEngine.getStatus(), playerEngine.getStatus());
	}
	
	
	public void apply(Command command) {
        // TODO : Use polymorphism to apply instead of instanceof
        switch (command.getType()) {
        case PLAYER_DROP_BOMB:
            playerEngine.dropBomb();
            break;
        case PLAYER_START_MOVE:
            PlayerStartMoveCommand playerStartMoveCommand = (PlayerStartMoveCommand)command;
            playerEngine.startMove(playerStartMoveCommand.getDirection());
            break;
        case PLAYER_STOP_MOVE:
        	PlayerStopMoveCommand playerStopMoveCommand = (PlayerStopMoveCommand)command;
            playerEngine.stopMove(playerStopMoveCommand.getDirection());
            break;
        default:
            throw new IllegalStateException("Unknown type " + command.getType());
        
        }
    }


	public void update() {
		playerEngine.update();
	}


	public GameStatus getStatus() {
		return status;
	}
		

}
