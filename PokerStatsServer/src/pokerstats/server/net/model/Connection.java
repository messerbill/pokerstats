package pokerstats.server.net.model;

import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Connection {
	
	private ServerSocket serverSocket;
	
	private Socket clientSocket;
	
	private InetAddress clientAddress;
	
	private String userName;
	
	private ObjectOutputStream objectOutputStream;
	
	public Connection(){
		
	}

	public ServerSocket getServerSocket() {
		return serverSocket;
	}

	public void setServerSocket(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}

	public Socket getClientSocket() {
		return clientSocket;
	}

	public void setClientSocket(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}

	public InetAddress getClientAddress() {
		return clientAddress;
	}

	public void setClientAddress(InetAddress clientAddress) {
		this.clientAddress = clientAddress;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public ObjectOutputStream getObjectOutputStream() {
		return objectOutputStream;
	}

	public void setObjectOutputStream(ObjectOutputStream objectOutputStream) {
		this.objectOutputStream = objectOutputStream;
	}

}
