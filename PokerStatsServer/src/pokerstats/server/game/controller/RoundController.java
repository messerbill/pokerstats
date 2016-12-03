package pokerstats.server.game.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import pokerstats.game.store.RoundStore;
import pokerstats.server.game.model.Card;
import pokerstats.server.game.model.CardColor;
import pokerstats.server.game.model.Hand;
import pokerstats.server.game.model.Player;
import pokerstats.server.game.model.TablePot;
import pokerstats.server.game.model.SubRound;
import pokerstats.server.main.PokerStatsServer;
import pokerstats.server.net.controller.NetworkController;

public class RoundController {
	
	private static Logger Log =  Logger.getLogger("PokerStats");
	
	public RoundController(){
		startNewRound();
	}
	
	/*
	 * round / subround methods
	 */
	
	public static void startNewRound(){
		setRoundStore(new RoundStore());
		for(Player p : MatchController.getPlayers()){
			p.setInGame(true);
			p.setMoved(false);
		}
		newDeck();
		//MatchController.increaseActualSeat();
		forceBlindPosts();
		dealCardsToPlayers();
		RoundController.setSubRoundState(SubRound.PRE_FLOP);
		NetworkController.syncWithAllPlayers();
		PlayersController.pokeStartPlayer();
	}
	
	@SuppressWarnings("incomplete-switch")
	public static void nextSubRound(){
		switch (RoundController.getSubRoundState()){
			case PRE_FLOP:
				RoundController.setSubRoundState(SubRound.POST_FLOP);
				dealFlopCards();
				PlayersController.setPlayersMovedFalse();
				if (MatchController.getPlayers().size() > 2) MatchController.setActualSeat(1);
				if (MatchController.getPlayers().size() == 2) MatchController.setActualSeat(2);
				NetworkController.syncWithAllPlayers();
				PlayersController.pokeNextPlayer();
				break;
			case POST_FLOP:
				RoundController.setSubRoundState(SubRound.POST_TURN);
				dealTurnCard();
				PlayersController.setPlayersMovedFalse();
				if (MatchController.getPlayers().size() > 2) MatchController.setActualSeat(1);
				if (MatchController.getPlayers().size() == 2) MatchController.setActualSeat(2);
				NetworkController.syncWithAllPlayers();
				PlayersController.pokeNextPlayer();
				break;
			case POST_TURN:
				RoundController.setSubRoundState(SubRound.POST_RIVER);
				dealRiverCard();
				PlayersController.setPlayersMovedFalse();
				if (MatchController.getPlayers().size() > 2) MatchController.setActualSeat(1);
				if (MatchController.getPlayers().size() == 2) MatchController.setActualSeat(2);
				NetworkController.syncWithAllPlayers();
				PlayersController.pokeNextPlayer();
				break;
			case POST_RIVER:
				RoundController.setSubRoundState(SubRound.SHOWDOWN);
				MatchController.setActualSeat(1);
				NetworkController.syncWithAllPlayers();
				evaluateWinnerAtShowdown();
				break;
		}
	}
	
	public static Player evaluateWinnerAtShowdown(){
		Map<Integer, List<Card>> tmp = new HashMap<Integer, List<Card>>();
                //loope über alle spieler und schaue wer die besten karten hat
                for (Player p : MatchController.getMatchStore().getPlayers()) {
                    //System.out.println(p.getName());
                    //merge die karten auf dem tisch mit der spieler hand
                    //überprüfe das blatt nach straßen etc
                    //speicher das ergebnis ab, um mit ddem nächsten spieler vergleichen zu können
                }
		tmp = CardValuesController.checkForSameCardValues(mergeBoardCardsWithPlayerHand(MatchController.getMatchStore().getPlayers().get(0)));
		System.out.println(tmp.toString());
		Log.info("now the best cards have won");
		return null;
	}
	
	public static List<Card> mergeBoardCardsWithPlayerHand(Player p){
		List<Card> merged = new ArrayList<Card>();
		merged.addAll(getBoardCards());
		merged.add(p.getHand().getCard1());
		merged.add(p.getHand().getCard2());
		return merged;
	}
	
	public static boolean subRoundFinished(){
		int ingamePlayers = 0;
		for(Player p : MatchController.getPlayers()){
			if (p.getInGame()) {
				ingamePlayers++;
			}
		}
		if (ingamePlayers == 1) return true;
		for(Player p : MatchController.getPlayers()){
			if (!p.getMoved()) {
				return false;
			}
		}
		Integer singlePot = getTablePots().size() > 0 ? getTablePots().get(0).getAmount() : 0;
		for (int i = 1; i < getTablePots().size(); i++){
			if (singlePot.compareTo(getTablePots().get(i).getAmount()) != 0) {
				return false;
			}
		}
		return true;
	}
	
	public static boolean roundFinished(){
		Integer activePlayers = 0;
		for(Player p : MatchController.getPlayers()){
			if (p.getInGame()) activePlayers++;
		}
		return (activePlayers > 1) ? false : true;
	}
	
	/*
	 * seat methods
	 */
	
	public static void increaseActualSeat(){
		if (MatchController.getActualSeat() < MatchController.getPlayers().size()){
			MatchController.setActualSeat(MatchController.getActualSeat()+1);
		} else {
			MatchController.setActualSeat(1);
		}
	}
	
	/*
	 * pot / chipstack methods
	 */
	
	public static Integer getHighestTablePotAmount(){
		Integer amount = 0;
		for(TablePot p : getTablePots()){
			if (p.getAmount() > amount) amount = p.getAmount();
		}
		return amount;
	}
	
	private static void forceBlindPosts(){
		postSmallBlind();
		postBigBlind();
	}
	
	public static void postSmallBlind(){
		Player p = MatchController.getPlayerBySeat(1);
		p.decreaseChipstack(MatchController.getBlindLevel() / 2);
		addTablePot(new TablePot(p.getName(), MatchController.getBlindLevel() / 2));
		Log.info(p.getName()+" posts Small Blind");
	}
	
	public static void postBigBlind(){
		Player p = MatchController.getPlayerBySeat(2);
		p.decreaseChipstack(MatchController.getBlindLevel());
		addTablePot(new TablePot(p.getName(), MatchController.getBlindLevel()));
		Log.info(p.getName()+" posts Big Blind");
	}
	
	public static synchronized void addTablePot(TablePot pot){
		Log.info("added table pot: "+pot.getAmount()+"("+pot.getOwner()+")");
		List<TablePot> tablePots = getTablePots();
		boolean potOwnerExists = false;
		for (TablePot p : tablePots){
			if (p.getOwner().equals(pot.getOwner())) {
				potOwnerExists = true;
				p.setAmount(p.getAmount() + pot.getAmount());
				RoundController.increasePot(pot.getAmount());
			}
		}
		if(!potOwnerExists) {
			getTablePots().add(pot);
			RoundController.increasePot(pot.getAmount());
		}
	}
	
	public static void mainPotToWinner(){
		PlayersController.getWinner().increaseChipstack(getPot());
	}
	
	public static boolean alreadyRaised(){
		for (TablePot p : getTablePots()){
			if (p.getAmount() > MatchController.getBlindLevel()) return true;
		}
		return false;
	}
	
	public static boolean decreaseChipstack(int amount, Player p){
		if (p.getChipstack() >= amount) {
			p.setChipstack(p.getChipstack() - amount);
			return true;
		}
		return false;
	}
	
	public static void increasePot(Integer amount){
		setPot(getPot() + amount);
	}
	
	public static void mergeTablePots(){
		Integer mainPot = 0;
		for (TablePot p : getTablePots()){
			mainPot += p.getAmount();
		}
		Log.info("merged table pots");
		setTablePots(new ArrayList<TablePot>());
		setPot(mainPot);
	}
	
	/*
	 * card-dealing methods
	 */
	
	public static void dealFlopCards(){
		Log.info("Dealing Flop Cards...");
		setBoardCards(new ArrayList<Card>());
		pullCard();
		for (int i = 0; i < 3; i++){
			getBoardCards().add(pullCard());
		}
	}
	
	public static void dealTurnCard(){
		Log.info("Dealing Turn Card...");
		pullCard();
		getBoardCards().add(pullCard());
	}
	
	public static void dealRiverCard(){
		Log.info("Dealing River Card...");
		pullCard();
		getBoardCards().add(pullCard());
	}
	
	public static void dealCardsToPlayers(){
		for (Player p : MatchController.getPlayers()){
			Hand hand = new Hand(RoundController.pullCard(), RoundController.pullCard());
			p.setHand(hand);
			Log.log(Level.INFO, "Dealt '"+hand.getCard1()+" | "+hand.getCard2()+"' to "+p.getName());
		}
	}
	
	/*
	 * Carddeck methods
	 */
	
	public static void newDeck(){
		List<Card> cards = new ArrayList<Card>();
		
		cards.add(new Card(1, CardColor.CROSS, "2_of_clubs.jpg"));
		cards.add(new Card(2, CardColor.CROSS, "3_of_clubs.jpg"));
		cards.add(new Card(3, CardColor.CROSS, "4_of_clubs.jpg"));
		cards.add(new Card(4, CardColor.CROSS, "5_of_clubs.jpg"));
		cards.add(new Card(5, CardColor.CROSS, "6_of_clubs.jpg"));
		cards.add(new Card(6, CardColor.CROSS, "7_of_clubs.jpg"));
		cards.add(new Card(7, CardColor.CROSS, "8_of_clubs.jpg"));
		cards.add(new Card(8, CardColor.CROSS, "9_of_clubs.jpg"));
		cards.add(new Card(9, CardColor.CROSS, "10_of_clubs.jpg"));
		cards.add(new Card(10, CardColor.CROSS, "jack_of_clubs.jpg"));
		cards.add(new Card(11, CardColor.CROSS, "queen_of_clubs.jpg"));
		cards.add(new Card(12, CardColor.CROSS, "king_of_clubs.jpg"));
		cards.add(new Card(13, CardColor.CROSS, "ace_of_clubs.jpg"));
		
		cards.add(new Card(1, CardColor.HEART, "2_of_hearts.jpg"));
		cards.add(new Card(2, CardColor.HEART, "3_of_hearts.jpg"));
		cards.add(new Card(3, CardColor.HEART, "4_of_hearts.jpg"));
		cards.add(new Card(4, CardColor.HEART, "5_of_hearts.jpg"));
		cards.add(new Card(5, CardColor.HEART, "6_of_hearts.jpg"));
		cards.add(new Card(6, CardColor.HEART, "7_of_hearts.jpg"));
		cards.add(new Card(7, CardColor.HEART, "8_of_hearts.jpg"));
		cards.add(new Card(8, CardColor.HEART, "9_of_hearts.jpg"));
		cards.add(new Card(9, CardColor.HEART, "10_of_hearts.jpg"));
		cards.add(new Card(10, CardColor.HEART, "jack_of_hearts.jpg"));
		cards.add(new Card(11, CardColor.HEART, "queen_of_hearts.jpg"));
		cards.add(new Card(12, CardColor.HEART, "king_of_hearts.jpg"));
		cards.add(new Card(13, CardColor.HEART, "ace_of_hearts.jpg"));
		
		cards.add(new Card(1, CardColor.KARO, "2_of_diamonds.jpg"));
		cards.add(new Card(2, CardColor.KARO, "3_of_diamonds.jpg"));
		cards.add(new Card(3, CardColor.KARO, "4_of_diamonds.jpg"));
		cards.add(new Card(4, CardColor.KARO, "5_of_diamonds.jpg"));
		cards.add(new Card(5, CardColor.KARO, "6_of_diamonds.jpg"));
		cards.add(new Card(6, CardColor.KARO, "7_of_diamonds.jpg"));
		cards.add(new Card(7, CardColor.KARO, "8_of_diamonds.jpg"));
		cards.add(new Card(8, CardColor.KARO, "9_of_diamonds.jpg"));
		cards.add(new Card(9, CardColor.KARO, "10_of_diamonds.jpg"));
		cards.add(new Card(10, CardColor.KARO, "jack_of_diamonds.jpg"));
		cards.add(new Card(11, CardColor.KARO, "queen_of_diamonds.jpg"));
		cards.add(new Card(12, CardColor.KARO, "king_of_diamonds.jpg"));
		cards.add(new Card(13, CardColor.KARO, "ace_of_diamonds.jpg"));
		
		cards.add(new Card(1, CardColor.SPADES, "2_of_spades.jpg"));
		cards.add(new Card(2, CardColor.SPADES, "3_of_spades.jpg"));
		cards.add(new Card(3, CardColor.SPADES, "4_of_spades.jpg"));
		cards.add(new Card(4, CardColor.SPADES, "5_of_spades.jpg"));
		cards.add(new Card(5, CardColor.SPADES, "6_of_spades.jpg"));
		cards.add(new Card(6, CardColor.SPADES, "7_of_spades.jpg"));
		cards.add(new Card(7, CardColor.SPADES, "8_of_spades.jpg"));
		cards.add(new Card(8, CardColor.SPADES, "9_of_spades.jpg"));
		cards.add(new Card(9, CardColor.SPADES, "10_of_spades.jpg"));
		cards.add(new Card(10, CardColor.SPADES, "jack_of_spades.jpg"));
		cards.add(new Card(11, CardColor.SPADES, "queen_of_spades.jpg"));
		cards.add(new Card(12, CardColor.SPADES, "king_of_spades.jpg"));
		cards.add(new Card(13, CardColor.SPADES, "ace_of_spades.jpg"));
		
		setCards(cards);
		
		long seed = System.nanoTime();
		Collections.shuffle(getCards(), new Random(seed));
	}
	
	public static Card pullCard(){
		int index = getCards().size();
		Card card = getCards().get(index-1);
		getCards().remove(index-1);
		return card;
	}
	
	/*
	 * Getters and Setters
	 */
	
	public static RoundStore getRoundStore() {
		return PokerStatsServer.gameStore.getRoundStore();}
	public static void setRoundStore(RoundStore _roundStore) {
		PokerStatsServer.gameStore.setRoundStore(_roundStore);}
	public static void setCards(List<Card> cards){
		PokerStatsServer.gameStore.getRoundStore().setCards(cards);}
	public static List<Card> getCards(){
		return PokerStatsServer.gameStore.getRoundStore().getCards();}
	public static List<Card> getBoardCards() {
		return PokerStatsServer.gameStore.getRoundStore().getBoardCards();}
	public static void setBoardCards(List<Card> _boardCards) {
		PokerStatsServer.gameStore.getRoundStore().setBoardCards(_boardCards);}
	public static List<TablePot> getTablePots() {
		return PokerStatsServer.gameStore.getRoundStore().getTablePots();}
	public static void setTablePots(List<TablePot> tablePots) {
		PokerStatsServer.gameStore.getRoundStore().setTablePots(tablePots);}
	public static Integer getLastBet(){
		return PokerStatsServer.gameStore.getRoundStore().getLastBet();}
	public static void setLastBet(int amount){
		PokerStatsServer.gameStore.getRoundStore().setLastBet(amount);}
	public static void setPot(Integer amount){
		PokerStatsServer.gameStore.getRoundStore().setPot(amount);}
	public static Integer getPot() {
		return PokerStatsServer.gameStore.getRoundStore().getPot();}
	public static SubRound getSubRoundState() {
		return PokerStatsServer.gameStore.getRoundStore().getSubRoundState();}
	public static void setSubRoundState(SubRound subRound) {
		PokerStatsServer.gameStore.getRoundStore().setSubRoundState(subRound);}
	public static TablePot getTablePotByName(String name) {
		int i = 0;
		for (TablePot p : PokerStatsServer.gameStore.getRoundStore().getTablePots()){
			if (p.getOwner().equals(PokerStatsServer.gameStore.getRoundStore().getTablePots().get(i).getOwner())) return p;
			i++;
		}
		return null;}
}
