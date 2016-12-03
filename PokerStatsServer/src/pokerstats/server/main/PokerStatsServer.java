package pokerstats.server.main;


import java.util.logging.Logger;
import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;

import pokerstats.game.store.GameStore;
import pokerstats.server.gui.Lobby;
import pokerstats.server.net.service.NetworkService;
import pokerstats.server.net.store.ConnectionStore;

public class PokerStatsServer {
	
	public static final String version = "0.0.1";
	
	private static final Logger Log =  Logger.getLogger("PokerStats");
	
	public static GameStore gameStore;
	
	public static void main(String[] args) throws IOException{
		Log.setUseParentHandlers(false);
		PokerStatsLogFormatter logFormatter = new PokerStatsLogFormatter();
		
		ConsoleHandler handler = new ConsoleHandler();
		handler.setFormatter(logFormatter);
		Log.addHandler(handler);
		
	    Log.log(Level.INFO, "PokerStats version "+version);
	    NetworkService.setConnectionStore(new ConnectionStore());
	    
	    Thread guiThread = new Thread(new Runnable() {
			public void run() {
				 Lobby lobby = new Lobby();
				 lobby.setVisible(true);
				 Lobby.updateList();
			}
		});
		guiThread.start();
	}
}
