package pokerstats.client.gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.NumberFormatter;

import pokerstats.client.controller.MatchController;
import pokerstats.client.controller.RoundController;
import pokerstats.client.main.PokerStatsMain;
import pokerstats.client.net.service.ConnectionService;
import pokerstats.server.game.model.Card;
import pokerstats.server.game.model.MoveType;
import pokerstats.server.game.model.Player;
import pokerstats.server.game.model.TablePot;
import pokerstats.server.net.model.GameMove;

public class Match extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private static final Logger Log =  Logger.getLogger("PokerStatsClient");
	
	private static Boolean uiSetup = false;
	
	public static JTextArea log;
	public static JList<String> userList;
	public static JScrollPane scroll; 
	public static JLabel usernameLabel = new JLabel("Username:");
	public static JLabel username = new JLabel(PokerStatsMain.username);
	public static JLabel chipstackLabel = new JLabel("Chips:");
	public static JLabel chipstack = new JLabel();
	public static JLabel handLabel = new JLabel("Hand: ");
	public static JPanel handCardPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
	public static JLabel hand = new JLabel();
	public static JLabel potLabel = new JLabel("Your bet(s): ");
	public static JLabel pot = new JLabel();
	public static JLabel totalPotLabel = new JLabel("Pot: ");
	public static JLabel totalPot = new JLabel();
	public static JFormattedTextField amountField;
	public static JPanel boardCards = new JPanel(new FlowLayout(FlowLayout.LEFT));

	public static JButton betButton = new JButton("bet / raise");
	public static JButton callButton = new JButton("call");
	public static JButton foldButton = new JButton("fold");
	
	public Match(){
		initUI();
		this.setVisible(false);
	}
	
	private void initUI() {
		//Match match = this;
        setTitle("PokerStats Match #'Testmatch'");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        //GUI Panels
    	JPanel mainPanel = new JPanel();
    	mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
    	JPanel container = new JPanel(new GridLayout(0,2));
    	container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));
    	//left side    	
    	JPanel left = new JPanel();
    	left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
    	JPanel usernamePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    	JPanel chipstackPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    	JPanel handPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    	JPanel potPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    	JPanel totalPotPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    	JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    	
    	JPanel logPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    	
    	JPanel userlistPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    	
    	//right side
    	JPanel right = new JPanel();
    	right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
         
        betButton.setEnabled(false);
        callButton.setEnabled(false);
        foldButton.setEnabled(false);
        
        handCardPanel.setPreferredSize(new Dimension(210,150));
         
        log = new JTextArea(5,50);
        log.setEditable(false);
        String[] test = {"hallo", "welt"};
        userList = new JList<String>(test);
        
        NumberFormat format = NumberFormat.getInstance();
        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Integer.class);
        formatter.setMinimum(0);
        formatter.setMaximum(Integer.MAX_VALUE);
        formatter.setAllowsInvalid(false);
        formatter.setCommitsOnValidEdit(true);
         
        amountField = new JFormattedTextField(formatter);
        amountField.setValue(new Integer(0));
        amountField.setColumns(10);
        
        scroll = new JScrollPane (log,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        
        //add to panels
        usernamePanel.add(usernameLabel);
        usernamePanel.add(username);
        chipstackPanel.add(chipstackLabel);
        chipstackPanel.add(chipstack);
        handPanel.add(handLabel);
        handPanel.add(handCardPanel);
        potPanel.add(potLabel);
        potPanel.add(pot);
        totalPotPanel.add(totalPotLabel);
        totalPotPanel.add(totalPot);
        logPanel.add(scroll);
        userlistPanel.add(userList);
        buttonPanel.add(amountField);
        buttonPanel.add(betButton);
        buttonPanel.add(callButton);
        buttonPanel.add(foldButton);
        
        left.add(usernamePanel);
        left.add(chipstackPanel);
        left.add(handPanel);
        left.add(potPanel);
        left.add(totalPotPanel);
        left.add(boardCards);
        left.add(buttonPanel);
        
        right.add(userlistPanel);
        
        container.add(left);
        container.add(right);
        mainPanel.add(container);
        mainPanel.add(logPanel);
        
      //listeners
        betButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
            	Integer bet = Integer.parseInt(amountField.getText());
            	if (bet > 0) {
            		ConnectionService.send(new GameMove(MoveType.BET, bet));
            		Log.info("You bet "+bet+" chips");
            		Match.toggleButtons();
            	}
            }
        });
        callButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
            	if (RoundController.getCallHeight() <= MatchController.getPlayerByName(PokerStatsMain.username).getChipstack()) {
            		ConnectionService.send(new GameMove(MoveType.CALL, RoundController.getCallHeight()));
            		Log.info("You call ("+RoundController.getCallHeight()+" chips)");
            		Match.toggleButtons();
            	}
            }
        });
        foldButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
            	ConnectionService.send(new GameMove(MoveType.FOLD, null));
            	Log.info("You fold");
            	Match.toggleButtons();
            }
        });
        
        this.add(mainPanel);
        this.pack();
        setUiSetup(true);
    }
	
	public static void updateUi(){
		List<Player> players = MatchController.getPlayers();
    	DefaultListModel<String> listModel = new DefaultListModel<String>();
    	if (players != null && players.size() > 0) {
    		for (Player p : players){
    			if (RoundController.getTablePots() != null && RoundController.getTablePots().size() > 0) {
	    			for (TablePot pot_ : RoundController.getTablePots()){
	    			    if (p.getName().equals(pot_.getOwner())) {
	    			    	listModel.addElement("["+p.getSeat()+"] - "+p.getName()+" ("+p.getChipstack()+") | "+ pot_.getAmount() ); 
	    			    }
	    			}
    			} else {
    				listModel.addElement("["+p.getSeat()+"] - "+p.getName()+" ("+p.getChipstack()+") | 0");
    			}
    		}
    		userList.setModel(listModel);
		}
    	//userlist end setup
    	username.setText(PokerStatsMain.username);
    	amountField.setText(null);
    	if (RoundController.getBoardCards() != null){
    		boardCards.removeAll();
    		handCardPanel.setPreferredSize(new Dimension(580,150));
    		for (Card c : RoundController.getBoardCards()){
        		ImagePanel imagePanel = new ImagePanel(c.imageFileName);
    	        imagePanel.setPreferredSize(new Dimension(100,140));
    	        boardCards.add(imagePanel);
        	}
    	} else {
    		boardCards.removeAll();
    		handCardPanel.setPreferredSize(new Dimension(580,150));
    	}
    	if (MatchController.getPlayerByName(PokerStatsMain.username) != null){
    		chipstack.setText(MatchController.getPlayerByName(PokerStatsMain.username).getChipstack().toString());
    		if(RoundController.getHand() != null) {
    			ImagePanel imagePanel1 = new ImagePanel(RoundController.getHand().getCard1().imageFileName);
    	        imagePanel1.setPreferredSize(new Dimension(100,140));
    	        ImagePanel imagePanel2 = new ImagePanel(RoundController.getHand().getCard2().imageFileName);
    	        imagePanel2.setPreferredSize(new Dimension(100,140));
    	        handCardPanel.removeAll();
    	        handCardPanel.setPreferredSize(new Dimension(210,150));
    	        handCardPanel.add(imagePanel1);
    	        handCardPanel.add(imagePanel2);
                }
    		if(RoundController.getPot() != null) totalPot.setText(RoundController.getPot().toString());
    	}
    }
	
	public static void toggleButtons(){
		betButton.setEnabled(!betButton.isEnabled()); 
		callButton.setEnabled(!callButton.isEnabled()); 
		foldButton.setEnabled(!foldButton.isEnabled()); 
	}
	
	public static void setButtonsActive(){
		betButton.setEnabled(true); 
		callButton.setEnabled(true); 
		foldButton.setEnabled(true); 
	}
	
	public static void addLineToLog(String str){
    	log.append(str+"\n");
    }

	public static Boolean getUiSetup() {
		return uiSetup;
	}

	public static void setUiSetup(Boolean _uiSetup) {
		uiSetup = _uiSetup;
	}

}
