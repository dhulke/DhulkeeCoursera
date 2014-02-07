package gui;

import gui.StatusBar.ConnectionStatus;
import gui.ToolBar.ConnectedState;

import java.awt.BorderLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

import coursera.Connector;

public class MainPanel extends JPanel {

	private ToolBar toolBar;
	private CoursesPanel coursesPanel;
	private StatusBar statusBar;
	
	private String CAUTHCookie;
	
	
	public MainPanel(JFrame parent) {
		
		setLayout(new BorderLayout());
		
		toolBar = ToolBar.getInstace();
		toolBar.setToolBarListener(new DefaultToolBarListener((Window) parent));
		
		coursesPanel = CoursesPanel.getInstace();
		statusBar = StatusBar.getInstace();
		
		
		add(toolBar, BorderLayout.PAGE_START);
		add(coursesPanel, BorderLayout.CENTER);
		add(statusBar, BorderLayout.PAGE_END);
		
	}
	
	
	public class DefaultToolBarListener implements ActionListener {
		
		private LoginDialog loginDialog;
		private ConfigurationDialog configurationDialog;
		
		private Window parent;
		

		public DefaultToolBarListener(Window parent) {
			
			this.parent = parent;
			
			loginDialog = new LoginDialog((Window) parent);
			loginDialog.setLoginListener(new DefaultLoginListener());
			
			configurationDialog = new ConfigurationDialog((Window) parent);
		}

		private void loginButtonPressed() {
			
			loginDialog.setModal(true);
			loginDialog.setVisible(true);
		}
		
		private void refreshButtonPressed() {
			new Refresher((Window) parent, CAUTHCookie);

		}

		private void downloadButtonPressed() {
			new Synchronizer((Window) parent, CAUTHCookie, configurationDialog);
		}

		private void stopButtonPressed() {
			JOptionPane.showMessageDialog(parent,
	                "Hey man, I need to eat... Would you be kind enough to help me?\n" +
	                		"If you have a paypal account, and can help me, my email is: greendhulke@gmail.com\n" +
	                		"If not, just spread the news and let people know of this software.\n" +
	                		"The website is: dhulkeecoursera.blogspot.com.\n" +
	                		"I'll try to frequently add new features to it.\n" +
	                		"Thanks!\n\n" +
	                		"Peace out!",
	                "Support",
	                JOptionPane.PLAIN_MESSAGE,
	                Utils.icon("cat-eyes.png"));
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			switch(e.getActionCommand()) {
			case ToolBar.LOGIN :
				loginButtonPressed();
				break;
			case ToolBar.REFRESH :
				refreshButtonPressed();
				break;
			case ToolBar.DOWNLOAD :
				downloadButtonPressed();
				break;
			case ToolBar.STOP :
				stopButtonPressed();
				break;
			}
			
		}
		
		
		class DefaultLoginListener implements LoginDialogListener {

			@Override
			public void loggingIn(final String email, final String password) {
				
				statusBar.setConnected(ConnectionStatus.CONNECTING);
				toolBar.setConnectedState(ConnectedState.LOADING);
				
				new SwingWorker<Void, Void>() {

					@Override
					protected Void doInBackground() throws Exception {

						Connector connector = new Connector(email, password);
						CAUTHCookie = connector.connect();
						
						return null;
					}

					@Override
					protected void done() {
						
						if(CAUTHCookie != null) {
							statusBar.setConnected(ConnectionStatus.CONNECTED);
							statusBar.setMessage("All Right! Let's get it crackin'! =D");
							toolBar.setConnectedState(ConnectedState.CONNECTED);
						} else {
							statusBar.setConnected(ConnectionStatus.DISCONNECTED);
							statusBar.setMessage("Email or password didn't match! =(");
							toolBar.setConnectedState(ConnectedState.DISCONNECTED);
						}
					}
					
					
					
				}.execute();
				
			}
			
		}

	}
	
	
}
