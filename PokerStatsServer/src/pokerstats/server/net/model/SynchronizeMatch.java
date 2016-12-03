package pokerstats.server.net.model;

import java.io.Serializable;

import pokerstats.game.store.MatchStore;
import pokerstats.game.store.RoundStore;
import pokerstats.server.game.controller.MatchController;
import pokerstats.server.game.model.Player;

public class SynchronizeMatch implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private MatchStore matchStore;
	
	private RoundStore roundStore;
	
	private String playerName;
	
	public SynchronizeMatch(){
		
	}
	
	public SynchronizeMatch(String s, MatchStore _matchStore, RoundStore _roundStore){
		setPlayerName(s);
		setUpMatchStoreData(_matchStore, _roundStore);
	}
	
	private synchronized void setUpMatchStoreData(MatchStore _matchStore, RoundStore _roundStore){
		matchStore = MatchController.cloneMatchStore();
		roundStore = _roundStore;
		
		for (Player p : matchStore.getPlayers()){
			if (!p.getName().equals(playerName)) p.setHand(null);
		}
	}

	public MatchStore getMatchStore() {
		return matchStore;
	}

	public void setMatchStore(MatchStore matchStore) {
		this.matchStore = matchStore;
	}

	public RoundStore getRoundStore() {
		return roundStore;
	}

	public void setRoundStore(RoundStore roundStore) {
		this.roundStore = roundStore;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

}
