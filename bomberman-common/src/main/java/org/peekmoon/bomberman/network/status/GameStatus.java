package org.peekmoon.bomberman.network.status;

import java.nio.ByteBuffer;
// TODO : studiing if xxxStatus with DTO is good idea
public class GameStatus {
	
	private final BoardStatus boardStatus;
	private final PlayerStatus playerStatus;
	
	public GameStatus(BoardStatus boardStatus, PlayerStatus playerStatus) {
		this.boardStatus = boardStatus;
		this.playerStatus = playerStatus;
	}

	public GameStatus(ByteBuffer byteBuffer) {
		boardStatus = new BoardStatus(byteBuffer);
		playerStatus = new PlayerStatus(byteBuffer);
	}

	public void fill(ByteBuffer buffer) {
		boardStatus.fill(buffer);
		playerStatus.fill(buffer);
	}
	
	public BoardStatus getBoardStatus() {
		return boardStatus;
	}
	
	public PlayerStatus getPlayerStatus() {
		return playerStatus;
	}

}
