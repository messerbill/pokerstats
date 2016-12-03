package pokerstats.server.net.model;

import java.io.Serializable;

public class BetAmountToPlayer implements Serializable {

	private static final long serialVersionUID = 1L;

	private String playerName;
	
	private Integer amount;
	
	public BetAmountToPlayer(String playerName, Integer amount){
		this.playerName = playerName;
		this.amount = amount;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

}
