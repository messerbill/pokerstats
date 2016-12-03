package pokerstats.server.game.model;

import java.io.Serializable;

public class TablePot implements Serializable {

	private static final long serialVersionUID = 1L;

	private String owner;
	
	private Integer amount;
	
	public TablePot(String owner, Integer amount){
		setOwner(owner);
		setAmount(amount);
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}
}
