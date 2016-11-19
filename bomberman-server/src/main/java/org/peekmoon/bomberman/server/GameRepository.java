package org.peekmoon.bomberman.server;

import java.util.ArrayList;
import java.util.List;

import org.peekmoon.bomberman.model.Game;

public class GameRepository {
	
	private final List<Game> games;
	
	public GameRepository() {
	    games = new ArrayList<>();
	}
	
	public void update() {
	    games.forEach(Game::update);
	}

	// TODO : Clean old game
    public Game createGame() {
        Game game = new Game();
        games.add(game);
        return game;
    }
	
}
