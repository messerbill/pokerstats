package pokerstats.server.net.model;

import java.io.Serializable;

import pokerstats.server.main.PokerStatsServer;

public class WelcomeMessage implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String message = "Welcome to PokerStats game platform. Version: "+PokerStatsServer.version;

	public WelcomeMessage() {
		getMessage();
	}
	
	public String getMessage(){
		return message;
	}

}
