package pokerstats.server.gui;

import java.awt.ComponentOrientation;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import pokerstats.server.game.controller.MatchController;
import pokerstats.server.game.model.Player;
import pokerstats.server.net.controller.NetworkController;
import pokerstats.server.net.model.Connection;
import pokerstats.server.net.service.NetworkService;

public class Lobby extends JFrame {

	private static final long serialVersionUID = -2428691482138717367L;
	
	private static Logger Log =  Logger.getLogger("PokerStats");
	
	public static JTextArea textArea;
	
	private Lobby lobby;
	
	private static Boolean uiSetup = false;
	
	public static JScrollPane scroll; 
	
	private static JList<String> userList;

	public Lobby() {
        initUI();
    }

    private void initUI() {
    	Log.info("setting up UI...");
    	this.setVisible(true);
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	//setSize(700, 500);
    	
    	//GUI Panels
    	JPanel mainPanel = new JPanel();
    	mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
    	JPanel controlButtonPanel = new JPanel(new FlowLayout());
    	JPanel logPanel = new JPanel();
    	JPanel matchControlPanel = new JPanel(new FlowLayout());
    	
    	//set layouts
//    	controlButtonPanel.setLayout(new GridLayout(0,2));
//    	logPanel.setLayout(new GridLayout(0,2));
//    	matchControlButtonPanel.setLayout(new GridLayout(0,2));
        
        //add panels to main panel
    	mainPanel.add(controlButtonPanel);
    	mainPanel.add(logPanel);
    	mainPanel.add(matchControlPanel);
    	
        //GUI components
        final JButton startButton = new JButton("start server");
        final JButton stopButton = new JButton("stop server");
        final JButton startMatchButton = new JButton("start match");
        
        textArea = new JTextArea(8, 50);
        userList = new JList<String>();
        scroll = new JScrollPane (textArea,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        
        userList.addMouseListener(new MouseAdapter(){
        	public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                		
                }
            }
        });
        
        controlButtonPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        
        //add components onto controlPanel
        controlButtonPanel.add(startButton);
        controlButtonPanel.add(stopButton);
        logPanel.add(scroll);
        matchControlPanel.add(startMatchButton);
        matchControlPanel.add(userList);
        
        //set component properties
        setTitle("PokerStats Server Lobby");
        setLocationRelativeTo(null); 
        textArea.setEditable(false);
        
        //button listeners
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
            	NetworkController.startServer(lobby);
            	startButton.setEnabled(false);
            	stopButton.setEnabled(true);
            	startMatchButton.setEnabled(true);
            }
        });
        
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
            	NetworkController.stopServer(lobby);
            	startButton.setEnabled(true);
            	stopButton.setEnabled(false);
            	startMatchButton.setEnabled(false);
            }
        });
        
        startMatchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
            	MatchController.startNewMatch(1500, 10);
            }
        });
        
        stopButton.setEnabled(false);
        startMatchButton.setEnabled(false);
        
        add(mainPanel);
        setUiSetup(true);
        this.pack();
        Log.info("UI setup successful");
    }

    public static void addLineToLog(String str){
    	textArea.append(str+"\n");
    }
    
    
    public static void updateList(){
    	DefaultListModel<String> listModel = new DefaultListModel<String>();
    	List<Connection> cons = NetworkService.getActiveConnections();
    	if (cons != null && cons.size() > 0) {
    		for (Connection c : cons){
    			if (MatchController.getPlayerByName(c.getUserName()) != null) {
    				Player p = MatchController.getPlayerByName(c.getUserName());
    				listModel.addElement("["+p.getSeat()+"] - "+p.getName()+" ("+p.getChipstack()+")");
    			} else {
    				listModel.addElement(c.getUserName());
    			}
    		}	
		}
    	userList.setModel(listModel);
    }

	public static Boolean getUiSetup() {
		return uiSetup;
	}

	public static void setUiSetup(Boolean _uiSetup) {
		uiSetup = _uiSetup;
	}
}