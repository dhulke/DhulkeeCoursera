package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

public class StatusBar extends JPanel {
	
	public enum ConnectionStatus {CONNECTED("Connected", "connected.png"), 
									CONNECTING("Connecting", "connecting.png"), 
									DISCONNECTED("Disconnected", "disconnected.png");
	
		private ConnectionStatus(String text, String image) {
		
		}
	}
	public ConnectionStatus connected;
	private JLabel connection;
	private JLabel message;
	
	private static StatusBar statusBar = new StatusBar();
	
	public static StatusBar getInstace() {
		return statusBar;
	}
	
	private StatusBar() {
		
		setLayout(new BorderLayout());
		Border outerBorder = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
		Border innerBorder = BorderFactory.createEmptyBorder(2, 4, 2, 1);
		setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder));
		
		message = new JLabel("Welcome!");
//		message.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		message.setHorizontalAlignment(SwingConstants.CENTER);
		
		connection = new JLabel();
		Dimension d = connection.getPreferredSize();
		d.width = 100;//So that the messagebar doesn't move when the connection state changes
		connection.setPreferredSize(d);
//		connection.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		setConnected(ConnectionStatus.DISCONNECTED);
		
		add(connection, BorderLayout.LINE_START);
		add(message, BorderLayout.CENTER);
		
	}


	public String getMessage() {
		return message.getText();
	}


	public void setMessage(String message) {
		this.message.setText(message);
	}


	public boolean isConnected() {
		return connected.equals(ConnectionStatus.CONNECTED);
	}

	public void setConnected(ConnectionStatus connected) {
		
		switch(connected) {
		case CONNECTED :
			connection.setText("Connected");
			connection.setIcon(Utils.icon("connected.png"));
			break;
		case CONNECTING :
			connection.setText("Connecting");
			connection.setIcon(Utils.icon("connecting.png"));
			break;
		case DISCONNECTED :
			connection.setText("Disconnected");
			connection.setIcon(Utils.icon("disconnected.png"));
			break;
		}
		this.connected = connected;
	}
	
	

}
