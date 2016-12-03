package pokerstats.server.net.controller;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Logger;

import pokerstats.server.game.controller.MatchController;
import pokerstats.server.game.controller.MovesController;
import pokerstats.server.game.controller.PlayersController;
import pokerstats.server.game.controller.RoundController;
import pokerstats.server.gui.Lobby;
import pokerstats.server.net.model.*;
import pokerstats.server.net.service.NetworkService;

class ClientThread extends Thread {
	
	private static final Logger Log =  Logger.getLogger("PokerStats");
	
	private Connection connection;
	
	Socket clientSocket;
	
	boolean running = true;

	  ClientThread(Connection c, int i) {
	    clientSocket = c.getClientSocket();
	    connection = c;
	  }

	  public void run() {
	    Log.info("Client connected with address: "+ clientSocket.getInetAddress().getHostName());
	    try {
	    	ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
	    	connection.setObjectOutputStream(out);
	    	connection.setClientAddress(clientSocket.getInetAddress());
	    	ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
	    	
		    while (running) {	        	
		      	/*
		       	 * Here are all of the Servercalls defined. The client sends a message modeled like the PokerStatsProtocol structure
		       	 * (have a look at pokerstats.server.net.model.Protocol) and the relevant action is fired
		       	 * 
		       	 * */
		    	Object object = in.readObject();
		      	if (object instanceof NetworkAuthentificationConnect){
			   		NetworkAuthentificationConnect networkAuthentificationConnect = (NetworkAuthentificationConnect)object;
			   		String username = networkAuthentificationConnect.getUsername();
			   		connection.setUserName(username);
			   		if (!NetworkService.usernameExists(username)) {
			   			NetworkService.addConnection(connection);
			    		Log.info("Client "+connection.getClientAddress()+" logged in ("+connection.getUserName()+")");
				    	Lobby.updateList();
				    	NetworkService.send(connection, new WelcomeMessage());
			   		} else {
			   			NetworkService.send(connection, new NetworkAuthentificationConnect());
			   		}
		      	}
		      	if (object instanceof NetworkAuthentificationDisconnect){
		      		System.out.println(in.readObject().toString());
			   		if (NetworkService.usernameExists(connection.getUserName())) {
			   			NetworkService.removeConnection(connection);
			    		Log.info("Client "+connection.getClientAddress()+" disconnected ("+connection.getUserName()+")");
				    	Lobby.updateList();
			   		} else {
			   			//warning
			   		}
		      	}
		      	if (object instanceof GameMove){
		      		GameMove move = (GameMove) object;
		      		//GameMove validation and interpretation
		      		if (!MovesController.interpreteMove(move, connection)) {
		      			//GameMove not valid - force new Move
		      			NetworkService.send(connection, new PokePlayer());
		      		} else {
		      			//GameMove valid - execute GameMove and reset output stream
		      			connection.getObjectOutputStream().reset();
		      			MatchController.getPlayerByName(connection.getUserName()).setMoved(true);
		      			}
		      			
		      			if (RoundController.subRoundFinished()) {
		      				if(RoundController.roundFinished()) {
		      					RoundController.mainPotToWinner();
		      					Log.info(PlayersController.getWinner().getName()+" won");
		      					MatchController.rotateSeats();
		      					NetworkController.syncWithAllPlayers();
		      					RoundController.startNewRound();
		      				} else {
		      					Log.info(RoundController.getSubRoundState()+" round finished");
			      				RoundController.mergeTablePots();
			      				RoundController.nextSubRound();
			      				NetworkController.syncWithAllPlayers();
	      					}
		      				
		      			} else {
		      				NetworkController.syncWithAllPlayers();
		      				PlayersController.pokeNextPlayer();
		      			}	
		      	}
		      	if (object instanceof SynchronizeMatch){
		      		connection.getObjectOutputStream().reset();
		      		NetworkService.send(connection, new SynchronizeMatch(connection.getUserName(), MatchController.getMatchStore(), RoundController.getRoundStore()));
		      	}
		      	Lobby.updateList();
		      	out.reset();
		      	out.flush();
		    }
	      } catch (Exception e) {
	    	  if (e instanceof SocketException){
	    		  Log.info("Client "+connection.getClientAddress()+" ("+connection.getUserName()+") disconnected unexpected");
	    		  NetworkService.removeConnection(connection);
	    		  Lobby.updateList();
	    	  } else {
	    		  e.printStackTrace();
	    	  }
	      }
	  }
}