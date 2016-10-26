package org.peekmoon.bomberman.server;

import org.peekmoon.bomberman.model.Game;
import org.peekmoon.bomberman.network.command.Command;
import org.peekmoon.bomberman.network.command.PlayerStartMoveCommand;
import org.peekmoon.bomberman.network.command.PlayerStopMoveCommand;

public class GameEngine {
	
	private final Game game;
	
	public GameEngine() {
		this.game = new Game();
	}
	
	public void apply(int noPlayer, Command command) {
        // TODO : Use polymorphism to apply instead of instanceof
        switch (command.getType()) {
        case PLAYER_DROP_BOMB:
            game.dropBomb(noPlayer);
            break;
        case PLAYER_START_MOVE:
            PlayerStartMoveCommand playerStartMoveCommand = (PlayerStartMoveCommand)command;
            game.startMove(noPlayer, playerStartMoveCommand.getDirection());
            break;
        case PLAYER_STOP_MOVE:
        	PlayerStopMoveCommand playerStopMoveCommand = (PlayerStopMoveCommand)command;
        	game.stopMove(noPlayer, playerStopMoveCommand.getDirection());
            break;
        default:
            throw new IllegalStateException("Unknown type " + command.getType());
        
        }
    }


	public void update() {
		game.update();
	}


	public Game getStatus() {
		return game;
	}
		

}
