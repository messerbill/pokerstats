package pokerstats.client.net.service;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Logger;

import pokerstats.client.controller.MatchController;
import pokerstats.client.controller.RoundController;
import pokerstats.client.gui.GuiContainer;
import pokerstats.client.gui.Match;
import pokerstats.client.main.PokerStatsMain;
import pokerstats.server.net.model.*;

public class ConnectionService {
	
	private static Socket socket;
	
	private static ObjectOutputStream out;
    
    private static final Logger Log =  Logger.getLogger("PokerStatsClient");

	public ConnectionService(){
		
	}
	
	public static void connect(String username){
		Runnable task = new Runnable() {
			@Override
		    public void run()
		    {
			try {
				socket = new Socket("localhost", 63400);
				out = new ObjectOutputStream(socket.getOutputStream());
				ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
				NetworkAuthentificationConnect netAuth = new NetworkAuthentificationConnect(username);
				send(netAuth);
			    while (true){
			    	Object object = in.readObject();
			    	if (object instanceof WelcomeMessage){
			    		WelcomeMessage msg = (WelcomeMessage) object;
			    		GuiContainer.match.setVisible(true);
			    		Log.info(msg.getMessage());
			      	}
			    	else if (object instanceof PokePlayer){
			    		Log.info("receiving poke fom server");
			    		Match.setButtonsActive();
			      		Match.updateUi();
			      	}
			    	else if (object instanceof PlayerFolds){
			    		PlayerFolds playerFolds = (PlayerFolds) object;
			    		Log.info(playerFolds.getUserName()+" folds");
			      		Match.updateUi();
			      	}
			    	else if (object instanceof BetAmountToPlayer){
			    		BetAmountToPlayer betAmountToPlayer = (BetAmountToPlayer) object;
			    		if (!betAmountToPlayer.getPlayerName().equals(PokerStatsMain.username)) {
			    			Log.info(betAmountToPlayer.getPlayerName()+" bets "+betAmountToPlayer.getAmount()+" chips");
			    			MatchController.increaseChipstackFromPlayer(betAmountToPlayer.getPlayerName(), betAmountToPlayer.getAmount());
			    		}
			      		Match.updateUi();
			      	}
			    	else if (object instanceof CallAmountToPlayer){
			    		CallAmountToPlayer callAmountToPlayer = (CallAmountToPlayer) object;
			    		if (!callAmountToPlayer.getPlayerName().equals(PokerStatsMain.username)) {
			    			Log.info(callAmountToPlayer.getPlayerName()+" calls ("+callAmountToPlayer.getAmount()+" chips)");
			    			MatchController.increaseChipstackFromPlayer(callAmountToPlayer.getPlayerName(), callAmountToPlayer.getAmount());
			    		}
			      		Match.updateUi();
			      	}
			    	else if (object instanceof BoardCards){
			    		Log.info("receiving board cards fom server");
			    		BoardCards boardCards = (BoardCards) object;
			    		RoundController.setBoardCards(boardCards.getCards());
			      		Match.updateUi();
			      	}
			    	else if (object instanceof SynchronizeMatch){
			    		Log.info("synchronizing match data...");
			    		SynchronizeMatch synchronizeMatch = (SynchronizeMatch) object;
			    		MatchController.setMatchStore(synchronizeMatch.getMatchStore());
			    		RoundController.setRoundStore(synchronizeMatch.getRoundStore());
			    		//RoundController.validateRoundStore();
			    		Log.info("...done");
			      		Match.updateUi();
			      	}
			    }
		}
		catch(Exception e)
		{
		    e.printStackTrace();
		}
		
		}};
		
		Thread thread = new Thread(task);
		thread.start();
		
	}
	
	public static void disconnect(){
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void send(Object o){
		try {
			out.writeObject(o);
			out.reset();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
	

