package pokerstats.game.store;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import pokerstats.server.game.model.Player;

public class MatchStore implements Serializable, Cloneable {
	
	private static final long serialVersionUID = 1L;

	private List<Player> players = new ArrayList<Player>();
	
	private Integer blindLevel;
	
	private int actualSeat = 1;
	
	private Integer startChipstack = 1500;
	
	public MatchStore(){
		players = new ArrayList<Player>();
	}
	
	public MatchStore deepClone() {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(this);

			ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
			ObjectInputStream ois = new ObjectInputStream(bais);
			return (MatchStore) ois.readObject();
		} catch (IOException e) {
			return null;
		} catch (ClassNotFoundException e) {
			return null;
		}
	}

	
	public List<Player> getPlayers(){
		return this.players;
	}
	
	public void setPlayers(List<Player> players){
		this.players = players;
	}

	public Integer getBlindLevel() {
		return blindLevel;
	}

	public void setBlindLevel(Integer blindLevel) {
		this.blindLevel = blindLevel;
	}

	public Integer getStartChipstack() {
		return startChipstack;
	}

	public void setStartChipstack(Integer _startChipstack) {
		startChipstack = _startChipstack;
	}

	public int getActualSeat() {
		return actualSeat;
	}

	public void setActualSeat(int actualSeat) {
		this.actualSeat = actualSeat;
	}
}
