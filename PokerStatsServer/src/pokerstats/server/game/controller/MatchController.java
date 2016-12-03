package pokerstats.server.game.controller;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import pokerstats.game.store.MatchStore;
import pokerstats.server.game.model.Player;
import pokerstats.server.main.PokerStatsServer;
import pokerstats.server.net.model.Connection;
import pokerstats.server.net.service.NetworkService;

public class MatchController {
	
	private RoundController roundController;
	
	private static Logger Log =  Logger.getLogger("PokerStats");
	
	public static void startNewMatch(Integer startChipstack, Integer blindLevel){
		if (NetworkService.getActiveConnections().size() > 1){
			Log.info("starting new match");
			setMatchStore(new MatchStore());
			setStartChipstack(startChipstack);
			setBlindLevel(blindLevel);
			setActualSeat(1);
			
			Integer i = 1;
			for (Connection c : NetworkService.getActiveConnections()){
				Player newPlayer = new Player(c.getUserName(), startChipstack, i);
				newPlayer.setInGame(true);
				addPlayer(newPlayer);
				i++;
			}
			
			RoundController.startNewRound();
		} else {
			Log.info("a match needs at least two players before getting started");
		}		
	}
	
	public static void rotateSeats(){
		List<Player> players = getPlayers();
		int listSize = getPlayers().size();
		for (int i = listSize; i > 0; i--){
			if (players.get(i-1).getSeat() != 1) {
				players.get(i-1).decreaseSeat();
			} else {
				players.get(i-1).setSeat(listSize);
			}
		}
	}
	
	public static MatchStore cloneMatchStore(){
		return (MatchStore) PokerStatsServer.gameStore.getMatchStore().deepClone();
	}
	
	public static void increaseActualSeat(){
		if(PokerStatsServer.gameStore.getMatchStore().getPlayers().size() < PokerStatsServer.gameStore.getMatchStore().getActualSeat()){
			PokerStatsServer.gameStore.getMatchStore().setActualSeat(PokerStatsServer.gameStore.getMatchStore().getActualSeat() + 1);
		} else {
			PokerStatsServer.gameStore.getMatchStore().setActualSeat(1);
		}
	}
	
	public static void addPlayer(Player player){
		Log.log(Level.INFO, "Added Player " + player.getName() + " into MatchStore");
		getPlayers().add(player);
	}
	
	/*
	 * Getters and Setters
	 */
	
	public static Integer getStartChipstack(){
		return PokerStatsServer.gameStore.getMatchStore().getStartChipstack();}
	public static void setStartChipstack(Integer startChipstack){
		PokerStatsServer.gameStore.getMatchStore().setStartChipstack(startChipstack);
		Log.info("start chipstack was set to "+getStartChipstack());}
	public RoundController getRoundController() {
		return roundController;}
	public void setRoundController(RoundController roundController) {
		this.roundController = roundController;}
	public static void setMatchStore(MatchStore _matchStore){
		PokerStatsServer.gameStore.setMatchStore(_matchStore);}
	public static MatchStore getMatchStore(){
		return PokerStatsServer.gameStore.getMatchStore();}
	public static void setBlindLevel(Integer blindLevel){
		PokerStatsServer.gameStore.getMatchStore().setBlindLevel(blindLevel);
		Log.info("blindlevel was set to "+getBlindLevel());}
	public static Integer getBlindLevel(){
		return PokerStatsServer.gameStore.getMatchStore().getBlindLevel();}
	public static void setActualSeat(Integer i){
		PokerStatsServer.gameStore.getMatchStore().setActualSeat(i);}
	public static Integer getActualSeat(){
		return PokerStatsServer.gameStore.getMatchStore().getActualSeat();}
	public static List<Player> getPlayers(){
		return PokerStatsServer.gameStore.getMatchStore().getPlayers();}
	public static Player getPlayerByName(String name){
		for (Player p : PokerStatsServer.gameStore.getMatchStore().getPlayers()){
			if (p.getName().equals(name)) return p;
		}
		return null;}
	public static Player getPlayerBySeat(int seat){
		for (Player p : PokerStatsServer.gameStore.getMatchStore().getPlayers()){
			if (p.getSeat() == seat) return p;}
		return null;}
}
