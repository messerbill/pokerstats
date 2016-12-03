package pokerstats.server.game.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pokerstats.server.game.model.Card;
import pokerstats.server.game.model.CardColor;

public class CardValuesController {
	
	public static Card checkHighCard(List<Card> cards){
		int lastCardValue = 0;
		Card result = null;
		for (Card c : cards){
			if (c.value > lastCardValue) {
				lastCardValue =  c.value;
				result = c;
			}
		}
		return result;
	}
	
	public static Map<Integer, List<Card>> checkForSameCardValues(List<Card> cards){
		Map<Integer, List<Card>> resultCards = new HashMap<Integer, List<Card>>();
		List<Card> cardList1 = new ArrayList<Card>();
		List<Card> cardList2 = new ArrayList<Card>();
		List<Card> cardList3 = new ArrayList<Card>();
		List<Card> cardList4 = new ArrayList<Card>();
		List<Card> cardList5 = new ArrayList<Card>();
		List<Card> cardList6 = new ArrayList<Card>();
		List<Card> cardList7 = new ArrayList<Card>();
		List<Card> cardList8 = new ArrayList<Card>();
		List<Card> cardList9 = new ArrayList<Card>();
		List<Card> cardList10 = new ArrayList<Card>();
		List<Card> cardList11 = new ArrayList<Card>();
		List<Card> cardList12 = new ArrayList<Card>();
		List<Card> cardList13 = new ArrayList<Card>();
		for (Card c : cards){
			switch(c.value){
				case 1:
					cardList1.add(c);
					break;
				case 2:
					cardList2.add(c);
					break;
				case 3:
					cardList3.add(c);
					break;
				case 4:
					cardList4.add(c);
					break;
				case 5:
					cardList5.add(c);
					break;
				case 6:
					cardList6.add(c);
					break;
				case 7:
					cardList7.add(c);
					break;
				case 8:
					cardList8.add(c);
					break;
				case 9:
					cardList9.add(c);
					break;
				case 10:
					cardList10.add(c);
					break;
				case 11:
					cardList11.add(c);
					break;
				case 12:
					cardList12.add(c);
					break;
				case 13:
					cardList13.add(c);
					break;
			}
			
		}
		if (cardList1.size() > 0) resultCards.put(1, cardList1);
		if (cardList2.size() > 0) resultCards.put(2, cardList2);
		if (cardList3.size() > 0) resultCards.put(3, cardList3);
		if (cardList4.size() > 0) resultCards.put(4, cardList4);
		if (cardList5.size() > 0) resultCards.put(5, cardList5);
		if (cardList6.size() > 0) resultCards.put(6, cardList6);
		if (cardList7.size() > 0) resultCards.put(7, cardList7);
		if (cardList8.size() > 0) resultCards.put(8, cardList8);
		if (cardList9.size() > 0) resultCards.put(9, cardList9);
		if (cardList10.size() > 0) resultCards.put(10, cardList10);
		if (cardList11.size() > 0) resultCards.put(11, cardList11);
		if (cardList12.size() > 0) resultCards.put(12, cardList12);
		if (cardList13.size() > 0) resultCards.put(13, cardList13);
		return resultCards;
	}
	
	public static Map<CardColor, List<Card>> checkForSameCardColors(List<Card> cards){
		Map<CardColor, List<Card>> resultMap = new HashMap<CardColor, List<Card>>();
		List<Card> cardListHeart = new ArrayList<Card>();
		List<Card> cardListCross = new ArrayList<Card>();
		List<Card> cardListKaro = new ArrayList<Card>();
		List<Card> cardListSpades = new ArrayList<Card>();
		for (Card c : cards){
			switch(c.color) {
				case HEART:
					cardListHeart.add(c);
					break;
				case CROSS:
					cardListCross.add(c);
					break;
				case KARO:
					cardListKaro.add(c);
					break;
				case SPADES:
					cardListSpades.add(c);
					break;
			}
		}
		if (cardListHeart.size() > 0) resultMap.put(CardColor.HEART, cardListHeart);
		if (cardListCross.size() > 0) resultMap.put(CardColor.CROSS, cardListCross);
		if (cardListKaro.size() > 0) resultMap.put(CardColor.KARO, cardListKaro);
		if (cardListSpades.size() > 0) resultMap.put(CardColor.SPADES, cardListSpades);
		return resultMap;
	}
	
	public static List<Card> checkForStraight(List<Card> cards){
		List<Card> resultCards = null;
		//Collections.sort(cards);
		return resultCards;
	}
        
        public static List<Card> checkForRoyalFlush(List<Card> cards){
		List<Card> resultCards = null;
		//Collections.sort(cards);
		return resultCards;
	}
        
        //neue spiellogikchecks hier
}
