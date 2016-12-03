package pokerstats.server.game.controller;

import java.util.logging.Logger;

import pokerstats.server.game.model.TablePot;
import pokerstats.server.net.controller.NetworkController;
import pokerstats.server.net.model.Connection;
import pokerstats.server.net.model.GameMove;
import pokerstats.server.net.model.PlayerFolds;

public class MovesController {
	
	private static final Logger Log =  Logger.getLogger("PokerStats");
	
		public static boolean interpreteMove(GameMove move, Connection c){
			switch(move.getMoveType()){
				case BET:
					if (validateMove(move, c)){
						Log.info(c.getUserName()+" bets "+move.getAmount()+" chips");
						//RoundService.setLastBet(move.getAmount());
						RoundController.decreaseChipstack(move.getAmount(), MatchController.getPlayerByName(c.getUserName()));
						RoundController.addTablePot(new TablePot(c.getUserName(), move.getAmount()));
						RoundController.increaseActualSeat();		
						NetworkController.syncWithAllPlayers();
						return true;
					}
					return false;
				case CALL:
					if (validateMove(move, c)){
						Log.info(c.getUserName()+" calls ("+move.getAmount()+" chips)");
						//RoundService.setLastBet(move.getAmount());
						RoundController.decreaseChipstack(move.getAmount(), MatchController.getPlayerByName(c.getUserName()));
						RoundController.addTablePot(new TablePot(c.getUserName(), move.getAmount()));
						RoundController.increaseActualSeat();		
						NetworkController.syncWithAllPlayers();
						return true;
					}
					return false;
				case FOLD:
					Log.info(c.getUserName()+" folds");
					MatchController.getPlayerByName(c.getUserName()).setInGame(false);
					RoundController.increaseActualSeat();			
					NetworkController.sendToAllPlayers(new PlayerFolds(c.getUserName()));
					return true;
				case RAISE:
					break;
				default:
					break;
			}
			return false;
		}
		
		private static boolean validateMove(GameMove move, Connection c){
			Integer tablePot = (RoundController.getTablePotByName(c.getUserName()) != null) ?  RoundController.getTablePotByName(c.getUserName()).getAmount() : 0;
			Integer moveAmount = move.getAmount() + tablePot;
			if (moveAmount <= MatchController.getPlayerByName(c.getUserName()).getChipstack() && moveAmount >= MatchController.getBlindLevel() && moveAmount >= RoundController.getHighestTablePotAmount()){ 
				if (!RoundController.alreadyRaised() && (moveAmount.compareTo(MatchController.getBlindLevel()) == 0)) return true; 
				if (!RoundController.alreadyRaised() && moveAmount < MatchController.getBlindLevel() * 2) return false; 
				if (RoundController.alreadyRaised() && moveAmount < RoundController.getHighestTablePotAmount()) return false; 
				if(RoundController.getTablePots().size() == 0 && moveAmount < MatchController.getBlindLevel()) return false;
				return true;
			}
			return false;
		}

}
