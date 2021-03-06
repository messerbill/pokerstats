package pokerstats.server.net.model;

import java.io.Serializable;

import pokerstats.server.game.model.Card;

public class Hand implements Serializable {

	private static final long serialVersionUID = 1L;

	private Card card1;
	
	private Card card2;
	
	public Hand(Card card1, Card card2){
		this.card1 = card1;
		this.card2 = card2;
	}

	public Card getCard1() {
		return card1;
	}

	public void setCard1(Card card1) {
		this.card1 = card1;
	}

	public Card getCard2() {
		return card2;
	}

	public void setCard2(Card card2) {
		this.card2 = card2;
	}

}
