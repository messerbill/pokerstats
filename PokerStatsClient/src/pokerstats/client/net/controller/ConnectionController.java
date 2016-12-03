package pokerstats.client.net.controller;

import pokerstats.client.main.PokerStatsMain;
import pokerstats.client.net.service.ConnectionService;
import pokerstats.server.net.model.NetworkAuthentificationDisconnect;

public class ConnectionController {
	
	public static void logIn(String username){
		PokerStatsMain.username = username;
		ConnectionService.connect(username);
	}
	
	public static void disconnect(){
		ConnectionService.send(new NetworkAuthentificationDisconnect());
		ConnectionService.disconnect();
	}
}
