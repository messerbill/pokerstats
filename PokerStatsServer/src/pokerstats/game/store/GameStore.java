package pokerstats.game.store;

public class GameStore {
	
	private MatchStore matchStore = new MatchStore();
	
	private RoundStore roundStore = new RoundStore();
	
	public GameStore(){
		
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

}
