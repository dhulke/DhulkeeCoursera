package gui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;

public class ToolBar extends JToolBar implements ActionListener {

	public static final String LOGIN = "login";
	public static final String REFRESH = "refresh";
	public static final String DOWNLOAD = "download";
	public static final String STOP = "stop";
	
	public enum ConnectedState {CONNECTED, DISCONNECTED, DOWNLOADABLE, LOADING}
	
	public JButton login;
	public JButton refresh;
	public JButton download;
	public JButton support;
	
	private ActionListener toolBarListener;
	
	private static ToolBar toolBar = new ToolBar();
	
	private ToolBar() {
		
		setFloatable(false);
		setLayout(new FlowLayout(FlowLayout.CENTER));
		
		login = addButton("Login", "login cartoon.png", "Login to Coursera", LOGIN, this);
		refresh = addButton("Load Courses", "load courses cartoon.png", "Refresh the list of courses", REFRESH, this);
		download = addButton("Download", "download cartoon.png", "Download selected courses", DOWNLOAD, this);
		support = addButton("Hey!!", "support cartoon.png", "Stop download", STOP, this);
		
		setConnectedState(ConnectedState.DISCONNECTED);
		
		add(login);
		add(refresh);
		add(download);
		add(support);
	}
	
	private JButton addButton(String title, String icon, String toolTip, String actionCommand, ActionListener al) {
		JButton button = new JButton(title);
		button.setVerticalTextPosition(SwingConstants.BOTTOM);
	    button.setHorizontalTextPosition(SwingConstants.CENTER);
		button.setIcon(Utils.icon(icon));
		button.setToolTipText(toolTip);
		button.setActionCommand(actionCommand);
		button.addActionListener(al);
		return button;
	}
	
	public void setConnectedState(ConnectedState connectedState) {
		switch(connectedState) {
		case CONNECTED :
			login.setEnabled(true);       
			refresh.setEnabled(true);   
			download.setEnabled(false);   
			support.setEnabled(true);
			break;
		case DISCONNECTED :
			login.setEnabled(true);       
			refresh.setEnabled(false);   
			download.setEnabled(false);   
			support.setEnabled(true);
			break;
		case DOWNLOADABLE :
			login.setEnabled(true);       
			refresh.setEnabled(true);   
			download.setEnabled(true);   
			support.setEnabled(true);
			break;
		case LOADING :
			login.setEnabled(false);       
			refresh.setEnabled(false);   
			download.setEnabled(false);   
			support.setEnabled(true);
		}
	}


	public void setToolBarListener(ActionListener toolBarListener) {
		this.toolBarListener = toolBarListener;
	}
	
	private void fireToolBarButtonEvent(ActionEvent e) {
		if(toolBarListener != null)
			toolBarListener.actionPerformed(e);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		fireToolBarButtonEvent(e);
	}
	
	public JButton getLogin() {
		return login;
	}

	public JButton getRefresh() {
		return refresh;
	}

	public JButton getDownload() {
		return download;
	}

	public JButton getSupport() {
		return support;
	}

	public static ToolBar getInstace() {
		return toolBar;
	}
}
