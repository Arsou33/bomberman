package org.peekmoon.bomberman.model;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class Game {
	
	private final Board board;
	private final List<Player> players;
	
	public Game() {
		board = new Board();
		players = new ArrayList<>();
		players.add(new Player(board, 1,1));
		players.add(new Player(board, 19,13));  //TODO : Better construction
	}
	
	public Game(ByteBuffer byteBuffer) {
		board = new Board(byteBuffer);
		byte nbPlayer = byteBuffer.get();
		players = new ArrayList<>(nbPlayer);
		while (nbPlayer>0) {
			players.add(new Player(board, byteBuffer));
			nbPlayer--;
		}
	}

	public void fill(ByteBuffer buffer) {
		board.fill(buffer);
		buffer.put((byte) players.size());
		players.forEach(player->player.fill(buffer));
	}
	
	public Board getBoardStatus() {
		return board;
	}
	
	public List<Player> getPlayers() {
		return players;
	}

	public void update() {
		players.forEach(player->player.update());
		board.update();
	}

	public void dropBomb(int noPlayer) {
		players.get(noPlayer).dropBomb();
	}

	public void startMove(int noPlayer, Direction direction) {
		players.get(noPlayer).startMove(direction);		
	}

	public void stopMove(int noPlayer, Direction direction) {
		players.get(noPlayer).stopMove(direction);
	}
	

}
