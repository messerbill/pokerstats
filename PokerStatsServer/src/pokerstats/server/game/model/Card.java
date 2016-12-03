package pokerstats.server.game.model;

import java.io.Serializable;

public class Card implements Serializable {

	private static final long serialVersionUID = 1L;

	public CardColor color;
        
    public String imageFileName = "";
	
	public Integer value;
        
    public Card(Integer val, CardColor color, String imageFileName){
		this.color = color;
		this.value = val;
        this.imageFileName = imageFileName;
	}
	
    public void test(){
    	
    }
    
	public String toString(){
		String result = "(";
		result += this.color+" - "+this.value+")";
		return result;
	}

}