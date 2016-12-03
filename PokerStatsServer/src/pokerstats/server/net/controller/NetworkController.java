package pokerstats.server.net.controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import pokerstats.server.game.model.Player;
import pokerstats.server.gui.Lobby;
import pokerstats.server.main.PokerStatsServer;
import pokerstats.server.net.model.BoardCards;
import pokerstats.server.net.model.Connection;
import pokerstats.server.net.model.ConnectionCheck;
import pokerstats.game.store.GameStore;
import pokerstats.server.game.controller.MatchController;
import pokerstats.server.game.controller.RoundController;
import pokerstats.server.game.model.Hand;
import pokerstats.server.net.model.PokePlayer;
import pokerstats.server.net.model.SynchronizeMatch;
import pokerstats.server.net.service.NetworkService;

public class NetworkController {
	
	private static Logger Log =  Logger.getLogger("PokerStats");
	
	public static void startServer(final Lobby lobby){
		NetworkService.setServerState(true);
		NetworkService.setActiveConnections(new ArrayList<Connection>());	
		PokerStatsServer.gameStore = new GameStore();
		Thread connectionListenerThread = new Thread(new Runnable() {
			ServerSocket m_ServerSocket;
			int id = 0; 
			public void run() {
				try {
					m_ServerSocket = new ServerSocket(63400);
					while (NetworkService.getServerState()) {
						Connection connection = new Connection();
					    Socket clientSocket = m_ServerSocket.accept();
					    connection.setServerSocket(m_ServerSocket);
					    connection.setClientSocket(clientSocket);
					    ClientThread cliThread = new ClientThread(connection, id++);
					    cliThread.start();
					    }
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		connectionListenerThread.start();
		Log.info("Server started");
	}
	
	public static void stopServer(Lobby lobby){
		Log.info("server shut down!");
		try {
			for (Connection connection : NetworkService.getActiveConnections()){
				connection.getServerSocket().close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static synchronized void sendToAllPlayers(Object o){
		for (Connection c : NetworkService.getActiveConnections()){
			NetworkService.send(c, o);
		}
	}
	
	public static synchronized void syncWithAllPlayers(){
		Log.info("Synchronizing match data...");
		for (Connection c : NetworkService.getActiveConnections()){
			NetworkService.send(c, new SynchronizeMatch(c.getUserName(), MatchController.getMatchStore(), RoundController.getRoundStore()));
		}
		Log.info("...done");
	}
	
	public static void pokePlayer(Connection c, Player p){
		NetworkService.send(c, new PokePlayer());
	}
	
	public static synchronized void sendHandToPlayer(Connection c, Hand hand){
		NetworkService.send(c, hand);
	}
	
	public static void sendBoardCardsToPlayers(){
		sendToAllPlayers(new BoardCards(RoundController.getBoardCards()));
	}
	
	public static Integer getChipStackFromPlayer(String username){
		return MatchController.getPlayerByName(username).getChipstack();
	}
	
	public static void playersConnectionCheck(){
		if(NetworkService.getActiveConnections() != null) {			
			List<Connection> connectionList = NetworkService.getActiveConnections();
			Iterator<Connection> i = connectionList.iterator();

			while (i.hasNext()) {
					Connection c = i.next();
					NetworkService.send(c, new ConnectionCheck(c.getUserName(), ""));
			}
		}
	}
}
