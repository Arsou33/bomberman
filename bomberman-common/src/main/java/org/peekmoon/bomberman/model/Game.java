package org.peekmoon.bomberman.model;

import java.nio.ByteBuffer;

public class Game {
	
	private final Board board;
	private final Player player;
	
	public Game() {
		board = new Board();
		player = new Player(board);
	}
	
	public Game(ByteBuffer byteBuffer) {
		board = new Board(byteBuffer);
		player = new Player(board, byteBuffer);
	}

	public void fill(ByteBuffer buffer) {
		board.fill(buffer);
		player.fill(buffer);
	}
	
	public Board getBoardStatus() {
		return board;
	}
	
	public Player getPlayerStatus() {
		return player;
	}

	public void update() {
		player.update();
		board.update();
	}

	public void dropBomb() {
		player.dropBomb();
	}

	public void startMove(Direction direction) {
		player.startMove(direction);		
	}

	public void stopMove(Direction direction) {
		player.stopMove(direction);
	}
	

}
