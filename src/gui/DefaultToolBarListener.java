package gui;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class DefaultToolBarListener implements ActionListener {
	
	private LoginDialog loginDialog;
	private ConfigurationDialog configurationDialog;
	
	private String cookies;
	
	private JFrame parent;
	

	public DefaultToolBarListener(JFrame parent, String cookies) {
		
		loginDialog = new LoginDialog((Window) parent);
		loginDialog.setLoginListener(new DefaultLoginDialogListener(loginDialog));
		
		configurationDialog = new ConfigurationDialog((Window) parent);
		
		this.cookies = cookies;
		
		this.parent = parent;
	}

	private void loginButtonPressed() {
		loginDialog.setVisible(true);
	}
	
	private void refreshButtonPressed() {
		new Refresher((Window) parent, cookies);

	}

	private void downloadButtonPressed() {
		new Synchronizer((Window) parent, cookies, configurationDialog);
	}

	private void stopButtonPressed() {
		JOptionPane.showMessageDialog(parent,
                "Hey man, I need to eat... Would you be kind enough to help me? $1 is enough.",
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

}
