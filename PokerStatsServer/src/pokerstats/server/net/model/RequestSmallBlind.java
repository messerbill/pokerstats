package pokerstats.server.net.model;

import java.io.Serializable;

public class RequestSmallBlind implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String userName;
	
	public RequestSmallBlind(){
		
	}
	
	public RequestSmallBlind(String userName){
		setUserName(userName);
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
