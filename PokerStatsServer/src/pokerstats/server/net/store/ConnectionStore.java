package pokerstats.server.net.store;

import java.util.ArrayList;
import java.util.List;

import pokerstats.server.net.model.Connection;

public class ConnectionStore {
	
	private List<Connection> activeConnections = new ArrayList<Connection>();
	
	private boolean serverState = false;

	public List<Connection> getActiveConnections() {
		return activeConnections;
	}

	public void setActiveConnections(List<Connection> activeConnections) {
		this.activeConnections = activeConnections;
	}

	public boolean isServerState() {
		return serverState;
	}

	public void setServerState(boolean serverState) {
		this.serverState = serverState;
	}
	
}
