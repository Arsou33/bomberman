package org.peekmoon.bomberman.server;

import org.peekmoon.bomberman.network.command.Command;
import org.peekmoon.bomberman.network.command.PlayerStartMoveCommand;
import org.peekmoon.bomberman.network.command.PlayerStopMoveCommand;
import org.peekmoon.bomberman.network.status.GameStatus;

public class GameEngine {
	
	private final GameStatus game;
	
	public GameEngine() {
		this.game = new GameStatus();
	}
	
	public void apply(Command command) {
        // TODO : Use polymorphism to apply instead of instanceof
        switch (command.getType()) {
        case PLAYER_DROP_BOMB:
            game.dropBomb();
            break;
        case PLAYER_START_MOVE:
            PlayerStartMoveCommand playerStartMoveCommand = (PlayerStartMoveCommand)command;
            game.startMove(playerStartMoveCommand.getDirection());
            break;
        case PLAYER_STOP_MOVE:
        	PlayerStopMoveCommand playerStopMoveCommand = (PlayerStopMoveCommand)command;
        	game.stopMove(playerStopMoveCommand.getDirection());
            break;
        default:
            throw new IllegalStateException("Unknown type " + command.getType());
        
        }
    }


	public void update() {
		game.update();
	}


	public GameStatus getStatus() {
		return game;
	}
		

}
