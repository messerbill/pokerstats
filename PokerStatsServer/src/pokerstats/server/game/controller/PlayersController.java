package pokerstats.server.game.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import pokerstats.server.game.model.Player;
import pokerstats.server.net.model.PokePlayer;
import pokerstats.server.net.service.NetworkService;

public class PlayersController {
	
	private static Logger Log =  Logger.getLogger("PokerStats");
	
	public static void setPlayersMovedFalse(){
		for(Player p : MatchController.getPlayers()) p.setMoved(false);
	}
	
	public static boolean checkFoldStatus(){
		int i = 0;
		for (Player p : MatchController.getPlayers()){
			if (!p.getInGame()) i++;
		}
		return ((MatchController.getPlayers().size() - i) > 1) ? false : true;
	}
	
	public static void pokeStartPlayer(){
		Player actualPlayer;
		if (MatchController.getPlayers().size() > 2) {
			actualPlayer = MatchController.getPlayerBySeat(3);
			MatchController.setActualSeat(3);
		} else {
			actualPlayer = MatchController.getPlayerBySeat(1);
			MatchController.setActualSeat(1);
		}
		
		Log.info("poking player "+actualPlayer.getName()+" to request start move");
		NetworkService.send(
				NetworkService.getConnectionByUsername(actualPlayer.getName()), new PokePlayer());
	}
	
	public static void pokeNextPlayer(){
		Player actualPlayer = whoMovesNext();
		Log.info("poking player "+actualPlayer.getName()+" to request mext move");
		NetworkService.send(
				NetworkService.getConnectionByUsername(actualPlayer.getName()), new PokePlayer());
	}
	
	public static Player whoMovesNext(){		
		List<Player> ingamePlayers = new ArrayList<Player>();
		List<Player> players = MatchController.getPlayers();
		for(Player p : players){
			if (p.getInGame()) ingamePlayers.add(p);
		}
		for(Player p : ingamePlayers){
			if (p.getSeat() == MatchController.getActualSeat()) return p;
		}
		
		if (MatchController.getPlayerBySeat(MatchController.getActualSeat()) != null) return MatchController.getPlayerBySeat(MatchController.getActualSeat());
		return MatchController.getPlayerBySeat(1);
	}
	
	public static Player getWinner(){
		for(Player p : MatchController.getPlayers()){
			if (p.getInGame()) return p;
		}
		return null;
	}
}
