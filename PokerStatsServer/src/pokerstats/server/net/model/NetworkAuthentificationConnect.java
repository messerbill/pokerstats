package pokerstats.server.net.model;

import java.io.Serializable;

public class NetworkAuthentificationConnect implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String username;
	
	public NetworkAuthentificationConnect(){
	}
	
	public NetworkAuthentificationConnect(String username){
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}



