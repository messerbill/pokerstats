package pokerstats.client.main;

import java.util.logging.ConsoleHandler;
import java.util.logging.Logger;

import pokerstats.client.controller.MatchController;
import pokerstats.client.gui.GuiContainer;

public class PokerStatsMain {
	
	private static final Logger Log =  Logger.getLogger("PokerStatsClient");
	
	public static String username;

	public static void main(String[] args){
		Log.setUseParentHandlers(false);
		PokerStatsClientLogFormatter logFormatter = new PokerStatsClientLogFormatter();
		
		ConsoleHandler handler = new ConsoleHandler();
		handler.setFormatter(logFormatter);
		Log.addHandler(handler);
		MatchController.newGameStore();
		
		Thread guiThread = new Thread(new Runnable() {
			public void run() {
				new GuiContainer();
				//while gui loop ?
			}
		});
		guiThread.start();
	}
}
