package gui;

import gui.ConfigurationDialog.ButtonPressed;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginDialog extends FixedJDialog implements ActionListener {

	private final String LOGINCOMMAND = "login";
	private final String CANCELCOMMAND = "cancel";
	
	private JPanel fieldsPanel;
	
	private JLabel emailLabel;
	private JLabel passwordLabel;
	
	private JTextField emailField;
	private JPasswordField passwordField;
	
	private JButton loginButton;
	private JButton cancelButton;
	
	private String email;
	private String password;
	
	private LoginDialogListener loginListener;
	
	public enum ButtonPressed {
		OK, CANCEL
	}
	
	private ButtonPressed buttonPressed;
	

	public LoginDialog(Window window) {
		super(window, "Login");
		setSize(300, 300);
		setLayout(new BorderLayout());
		setResizable(false);
		
		
		JPanel dialog = (JPanel)getContentPane();
		dialog.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		fieldsPanel = new JPanel(new GridBagLayout());
		
		emailLabel = new JLabel("E-Mail");
		passwordLabel = new JLabel("Password");
		
		emailField = new JTextField(15);
		passwordField = new JPasswordField(15);
		
		constructFieldsPanel();
		
		JPanel buttonsPanel = new JPanel();
		
		loginButton = new JButton("Login");
		loginButton.setActionCommand(LOGINCOMMAND);
		loginButton.addActionListener(this);
		
		cancelButton = new JButton("Cancel");
		cancelButton.setActionCommand(CANCELCOMMAND);
		cancelButton.addActionListener(this);
		
		buttonsPanel.add(loginButton);
		buttonsPanel.add(cancelButton);

		add(fieldsPanel, BorderLayout.CENTER);
		add(buttonsPanel, BorderLayout.PAGE_END);
		pack();
	}
	
	private void constructFieldsPanel() {
		GridBagConstraints c = new GridBagConstraints();
		c.gridy = 0;
		
		c.gridx = 0;
		c.insets = new Insets(0, 0, 5, 10);
		c.anchor = GridBagConstraints.LINE_END;
		fieldsPanel.add(emailLabel, c);
		
		c.gridx = 1;
		c.insets = new Insets(0, 0, 5, 0);
		c.anchor = GridBagConstraints.LINE_START;
		fieldsPanel.add(emailField, c);
		
		c.gridy = 1;
		c.gridx = 0;
		c.insets = new Insets(0, 0, 10, 10);
		c.anchor = GridBagConstraints.LINE_END;
		fieldsPanel.add(passwordLabel, c);
		
		c.gridx = 1;
		c.insets = new Insets(0, 0, 10, 0);
		c.anchor = GridBagConstraints.LINE_START;
		fieldsPanel.add(passwordField, c);
	}
	
	public String getEmail() {
		return emailField.getText();
	}
	
	public String getPassword() {
		return new String(passwordField.getPassword());
	}
	
	public ButtonPressed getButtonPressed() {
		return buttonPressed;
	}
	
	
	private void fireLoggedIn(String email, String password) {
		if(loginListener != null)
			loginListener.loggingIn(email, password);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		switch(e.getActionCommand()) {
		case LOGINCOMMAND :
			email = this.emailField.getText();
			password = new String(this.passwordField.getPassword());
			buttonPressed = ButtonPressed.OK;
			fireLoggedIn(email, password);
			setVisible(false);
			break;
			
		case CANCELCOMMAND :
			setVisible(false);
			emailField.setText(email);
			passwordField.setText(password);
			buttonPressed = ButtonPressed.CANCEL;
			setVisible(false);
			break;
		}
		
	}

	public void setLoginListener(
			LoginDialogListener loginListener) {
		this.loginListener = loginListener;
	}

}
