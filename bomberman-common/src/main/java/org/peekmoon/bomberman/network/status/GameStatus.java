package org.peekmoon.bomberman.network.status;

import java.nio.ByteBuffer;

import org.peekmoon.bomberman.network.Direction;

public class GameStatus {
	
	private final BoardStatus board;
	private final PlayerStatus player;
	
	public GameStatus() {
		board = new BoardStatus();
		player = new PlayerStatus(board);
	}
	
	public GameStatus(ByteBuffer byteBuffer) {
		board = new BoardStatus(byteBuffer);
		player = new PlayerStatus(board, byteBuffer);
	}

	public void fill(ByteBuffer buffer) {
		board.fill(buffer);
		player.fill(buffer);
	}
	
	public BoardStatus getBoardStatus() {
		return board;
	}
	
	public PlayerStatus getPlayerStatus() {
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
