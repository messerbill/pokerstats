package pokerstats.server.net.model;

import java.io.Serializable;

import pokerstats.server.game.model.MoveType;

public class GameMove implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private MoveType moveType;
	
	private Integer amount = 0;

	public GameMove(MoveType moveType, Integer amount) {
		this.moveType = moveType;
		this.amount = amount;
	}

	public MoveType getMoveType() {
		return moveType;
	}

	public void setMoveType(MoveType moveType) {
		this.moveType = moveType;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

}
