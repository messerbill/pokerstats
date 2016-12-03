package pokerstats.server.net.model;

import java.io.Serializable;
import java.util.List;

import pokerstats.server.game.model.Card;

public class BoardCards implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private List<Card> cards;
	
	public BoardCards(List<Card> cards){
		this.setCards(cards);
	}

	public List<Card> getCards() {
		return cards;
	}

	public void setCards(List<Card> cards) {
		this.cards = cards;
	}

}
