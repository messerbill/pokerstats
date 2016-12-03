package pokerstats.client.controller;

import java.util.List;
import java.util.logging.Logger;

import pokerstats.client.main.PokerStatsMain;
import pokerstats.client.net.service.ConnectionService;
import pokerstats.game.store.RoundStore;
import pokerstats.server.game.model.Card;
import pokerstats.server.game.model.Hand;
import pokerstats.server.game.model.Player;
import pokerstats.server.game.model.SubRound;
import pokerstats.server.game.model.TablePot;
import pokerstats.server.net.model.RequestSmallBlind;
import pokerstats.server.net.model.SynchronizeMatch;

public class RoundController {
	
	private static final Logger Log =  Logger.getLogger("PokerStatsClient");
	
	public static Hand getHand(){
		for (Player p : MatchController.getGameStore().getMatchStore().getPlayers()) {
			if (p.getName().equals(PokerStatsMain.username)) return p.getHand();
		}
		return null;
	}
	
	public static Integer getPot(){
		return MatchController.getGameStore().getRoundStore().getPot();
	}
	
	public static List<TablePot> getTablePots(){
		return MatchController.getGameStore().getRoundStore().getTablePots();
	}
	
	public static void postSmallBlind(){
		ConnectionService.send(new RequestSmallBlind(PokerStatsMain.username));
	}
	
	public static Integer getCallHeight(){
		Integer amount = 0;
		Integer ownBet = 0;
		for(TablePot p : getTablePots()){
			if (p.getOwner().equals(PokerStatsMain.username)) ownBet = p.getAmount();
			if (!p.getOwner().equals(PokerStatsMain.username) && p.getAmount() > amount) amount = p.getAmount();
		}
		return amount - ownBet;
	}
	
	public static Integer getHighestTablePotAmount(){
		Integer amount = 0;
		for(TablePot p : getTablePots()){
			if (p.getAmount() > amount) amount = p.getAmount();
		}
		return amount;
	}
	
	public static void validateRoundStore(){
		SubRound subRound = MatchController.getGameStore().getRoundStore().getSubRoundState();
		//PRE_FLOP, POST_FLOP, POST_TURN, POST_RIVER, SHOWDOWN
		switch (subRound){
			case PRE_FLOP:
				if (getHand() == null){
					ConnectionService.send(new SynchronizeMatch());
				}
				
		}
	}
	
	public static void setRoundStore(RoundStore roundStore){
		MatchController.getGameStore().setRoundStore(roundStore);
		Log.info("new round store set");
	}
	
	public static void setBoardCards(List<Card> cards){
		MatchController.getGameStore().getRoundStore().setBoardCards(cards);
	}
	
	public static List<Card> getBoardCards(){
		return MatchController.getGameStore().getRoundStore().getBoardCards();
	}
	
	public static void setPot(Integer amount){
		MatchController.getGameStore().getRoundStore().setPot(amount);
		Log.info("new pot store set");
	}
	
}
