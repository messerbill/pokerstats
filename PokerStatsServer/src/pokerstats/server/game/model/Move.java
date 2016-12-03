package pokerstats.server.game.model;

public class Move {
	
	private MoveType moveType;
	
	private Player player;
	
	private Integer chips;
	
	public Move(MoveType moveType, Player player, Integer chips){
		this.moveType = moveType;
		this.player = player;
		this.chips = chips;
	}

	public MoveType getMoveType() {
		return moveType;
	}

	public void setMoveType(MoveType moveType) {
		this.moveType = moveType;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Integer getChips() {
		return chips;
	}

	public void setChips(Integer chips) {
		this.chips = chips;
	}
}
