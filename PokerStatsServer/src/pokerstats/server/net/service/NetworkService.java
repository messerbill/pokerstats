package pokerstats.server.net.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import pokerstats.server.net.model.Connection;
import pokerstats.server.net.store.ConnectionStore;

public class NetworkService {
	
	private static Logger Log =  Logger.getLogger("PokerStats");
	
	private static ConnectionStore connectionStore;
	
	private static Boolean serverState = false;
	
	public static void setConnectionStore(ConnectionStore _connectionStore){
		connectionStore = _connectionStore;
	}
	
	public static void addConnection(Connection c){
		NetworkService.getActiveConnections().add(c);
	}
	
	public static synchronized void send(Connection c, Object o){
		try {
			c.getObjectOutputStream().writeObject(o);
			c.getObjectOutputStream().reset();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static List<Connection> getActiveConnections() {
		return connectionStore.getActiveConnections();
	}
	
	public static void setActiveConnections(List<Connection> l) {
		connectionStore.setActiveConnections(l);
	}
	
	public static Connection getConnectionByUsername(String username){
		for (Connection c : getConnectionStore().getActiveConnections()) {
			if (c.getUserName().equals(username)) return c;
		}
		return null;
	}
	
	public static void removeConnection(Connection connection){
		
		List<Connection> connectionList = getConnectionStore().getActiveConnections();
		
		List<Connection> clone = new ArrayList<Connection>(connectionList.size());
	    for(Connection item: connectionList) clone.add(item);
	    Iterator<Connection> i = clone.iterator();
	    
		while (i.hasNext()) {
				Connection c = i.next();
				if (c.equals(connection)){
					i.remove();
					Log.info(c.getUserName()+" was removed from the connection store");
				}
		}
		getConnectionStore().setActiveConnections(clone);
	}
	
	public static void removeConnectionByUsername(String username){
		
		List<Connection> connectionList = getConnectionStore().getActiveConnections();
		
		List<Connection> clone = new ArrayList<Connection>(connectionList.size());
	    for(Connection item: connectionList) clone.add(item);
	    Iterator<Connection> i = clone.iterator();
	    
		while (i.hasNext()) {
				Connection c = i.next();
				if (c.getUserName().equals(username)){
					i.remove();
					Log.info(c.getUserName()+" was removed from the connection store");
				}
		}
		getConnectionStore().setActiveConnections(clone);
	}
	
	public static Boolean usernameExists(String username){
		return getConnectionByUsername(username) != null ? true : false;
	}
	
	private static ConnectionStore getConnectionStore(){
		return connectionStore;
	}

	public static Boolean getServerState() {
		return serverState;
	}

	public static void setServerState(Boolean _serverState) {
		serverState = _serverState;
	}
}
