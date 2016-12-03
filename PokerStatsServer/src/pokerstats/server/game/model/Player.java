package pokerstats.server.game.model;

import java.io.Serializable;

public class Player implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;
	
	private Integer chipStack;
	
	private Integer seat;
	
	private Hand hand;
	
	private Boolean inGame;
	
	private Boolean moved;
	
	public Player(String name){
		this.name = name;
	}
	
	public Player(String name, Integer chipStack, Integer seat){
		this.name = name;
		this.chipStack = chipStack;
		this.seat = seat;
		
	}

	public String getName() {
		return name;
	}
	
	public Boolean getInGame(){
		return inGame;
	}
	
	public void setInGame(Boolean b){
		this.inGame = b;
	}
	
	public Integer getChipstack() {
		return chipStack;
	}
	
	public void setChipstack(Integer chipStack) {
		this.chipStack = chipStack;
	}
	
	public void decreaseChipstack(Integer chipStack) {
		this.chipStack -= chipStack;
	}
	
	public void increaseChipstack(Integer chipStack) {
		this.chipStack += chipStack;
	}
	
	public void decreaseSeat() {
		this.seat--;
	}
	
	public Integer getSeat() {
		return seat;
	}
	
	public void setSeat(int s) {
		seat = s;
	}
	
	public Hand getHand() {
		return hand;
	}

	public void setHand(Hand hand) {
		this.hand = hand;
	}

	public Boolean getMoved() {
		return moved;
	}

	public void setMoved(Boolean moved) {
		this.moved = moved;
	}

}
