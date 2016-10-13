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
	
	public void apply(int idPlayer, Command command) {
        // TODO : Use polymorphism to apply instead of instanceof
        switch (command.getType()) {
        case PLAYER_DROP_BOMB:
            game.dropBomb(idPlayer);
            break;
        case PLAYER_START_MOVE:
            PlayerStartMoveCommand playerStartMoveCommand = (PlayerStartMoveCommand)command;
            game.startMove(idPlayer, playerStartMoveCommand.getDirection());
            break;
        case PLAYER_STOP_MOVE:
        	PlayerStopMoveCommand playerStopMoveCommand = (PlayerStopMoveCommand)command;
        	game.stopMove(idPlayer, playerStopMoveCommand.getDirection());
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
