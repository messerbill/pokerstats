package pokerstats.server.net.model;

import java.io.Serializable;

public class PlayerFolds implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String userName;
	
	public PlayerFolds(String userName){
		this.userName = userName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
