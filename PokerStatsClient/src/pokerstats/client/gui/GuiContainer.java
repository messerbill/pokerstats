package pokerstats.client.gui;

public class GuiContainer {
	
	public static Login login;
	
	public static Match match;
	
	public GuiContainer(){
		login = new Login();
		match = new Match();
	}

}
