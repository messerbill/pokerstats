package pokerstats.game.store;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import pokerstats.server.game.model.Card;
import pokerstats.server.game.model.TablePot;
import pokerstats.server.game.model.SubRound;

public class RoundStore implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<Card> boardCards;
	
	private List<TablePot> tablePots = new ArrayList<TablePot>();
	
	private Integer pot = 0;
	
	private SubRound subRoundState = SubRound.PRE_FLOP;
	
	//private int activePlayers;
	
	private int lastBet = 0;
	
	private List<Card> cards;
	
	public RoundStore(){
		
	}

	public List<Card> getBoardCards() {
		return boardCards;
	}

	public void setBoardCards(List<Card> boardCards) {
		this.boardCards = boardCards;
	}

	public List<TablePot> getTablePots() {
		return tablePots;
	}

	public void setTablePots(List<TablePot> tablePots) {
		this.tablePots = tablePots;
	}

	public int getLastBet() {
		return lastBet;
	}

	public void setLastBet(int lastBet) {
		this.lastBet = lastBet;
	}

	public List<Card> getCards() {
		return cards;
	}

	public void setCards(List<Card> cards) {
		this.cards = cards;
	}

	public Integer getPot() {
		return pot;
	}

	public void setPot(Integer pot) {
		this.pot = pot;
	}

	public SubRound getSubRoundState() {
		return subRoundState;
	}

	public void setSubRoundState(SubRound subRoundState) {
		this.subRoundState = subRoundState;
	}
	
}
