package gui;

import gui.ToolBar.ConnectedState;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public class DefaultLoginDialogListener implements LoginDialogListener, ActionListener {

	private LoginDialog loginDialog;
	private StatusBar statusBar;
	private ToolBar toolBar;
	private ProgressDialog progressDialog;
	private int count;
	private Timer timer;
	
	
	public DefaultLoginDialogListener(LoginDialog loginDialog) {
		this.loginDialog = loginDialog;
		statusBar = StatusBar.getInstace();
		toolBar = ToolBar.getInstace();
		
	}
	
	@Override
	public void loggingIn(String email, String password) {
		
		statusBar.setConnected(StatusBar.ConnectionStatus.CONNECTING);
		
		loginDialog.setVisible(false);
		
		
		
		statusBar.setConnected(StatusBar.ConnectionStatus.CONNECTED);
		statusBar.setMessage("All Right!");
		toolBar.setConnectedState(ConnectedState.CONNECTED);
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
