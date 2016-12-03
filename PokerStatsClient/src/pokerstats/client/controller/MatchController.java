package pokerstats.client.controller;

import java.util.List;
import java.util.logging.Logger;

import pokerstats.client.main.PokerStatsMain;
import pokerstats.game.store.GameStore;
import pokerstats.game.store.MatchStore;
import pokerstats.server.game.model.Player;

public class MatchController {
	
	private static GameStore gameStore;
	
	private static final Logger Log =  Logger.getLogger("PokerStatsClient");
	
	public static Integer getStartChipstack(){
		return gameStore.getMatchStore().getStartChipstack();
	}
	
	public static void newGameStore(){
		gameStore = new GameStore();
	}
	
	public static GameStore getGameStore(){
		return gameStore;
	}
	
	public static Integer getBigBlind(){
		return gameStore.getMatchStore().getBlindLevel();
	}
	
	public static void setStartChipstack(Integer startChipstack){
		gameStore.getMatchStore().setStartChipstack(startChipstack);
	}
	
	public static List<Player> getPlayers(){
		if (gameStore.getMatchStore() != null && gameStore.getMatchStore().getPlayers() != null) return gameStore.getMatchStore().getPlayers();
		return null;
	}
	
	public static Player getPlayerByName(String name){
		for (Player p : getPlayers()) {
			if (p.getName().equals(name)) return p;
		}
		return null;
	}
	
	public static void increaseChipstackFromPlayer(String playerName, Integer amount){
		Player p = getPlayerByName(playerName);
		p.setChipstack(p.getChipstack() - amount);
	}
	
	public static void setChipstack(Integer amount){
		getPlayerByName(PokerStatsMain.username).setChipstack(amount);
	}
	
	
	public static void setPlayers(List<Player> players){
		gameStore.getMatchStore().setPlayers(players);
	}
	
	public static void addPlayer(Player player){
		gameStore.getMatchStore().getPlayers().add(player);
	}

	public static void setMatchStore(MatchStore _matchStore) {
		gameStore.setMatchStore(_matchStore);
		Log.info("new matchstore set");
	}

	
}
