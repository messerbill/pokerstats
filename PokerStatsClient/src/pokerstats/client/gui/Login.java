package pokerstats.client.gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import pokerstats.client.net.controller.ConnectionController;

public class Login extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private JPanel controlPanel;
	
	public JTextArea usernameTextArea = new JTextArea("");
	
	public Login(){
		initUI();
		this.setVisible(true);
	}
	
	private void initUI() {
        controlPanel = new JPanel();
        setTitle("PokerStats Login");
        setSize(300, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        //GUI components
        JButton connectButton = new JButton("connect");
        JButton cancelButton = new JButton("cancel");
        JLabel usernameLabel = new JLabel("Username:");
        
        //add onto controlPanel
        controlPanel.add(usernameLabel);
        controlPanel.add(usernameTextArea);
        controlPanel.add(connectButton);
        controlPanel.add(cancelButton);
        
        usernameTextArea.getDocument().putProperty("filterNewlines", Boolean.TRUE);
        
        //listeners
        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
            	if (usernameTextArea.getText().length() > 0) {
            		GuiContainer.login.setVisible(false);
            		ConnectionController.logIn(usernameTextArea.getText());
            	}
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
            	//ConnectionController.disconnect("Ole");
            //	connectionController.getChipStackFromPlayer("Ben");
            }
        });
       
        controlPanel.setLayout(new GridLayout(0,2));
        this.add(controlPanel);
        this.pack();
    }
}
