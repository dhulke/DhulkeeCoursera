package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.text.DefaultCaret;

public class ProgressDialog extends FixedJDialog {
	
	private JPanel progressPanel;
	private JProgressBar progressBar;
	private JButton cancel;
//	private JTextArea textArea;
	private JTextArea textArea;
	
	private ProgressDialogListener progressListener;
	
	
	public ProgressDialog(Window window, String title) {
		super(window, title);
		setSize(700, 500);
		setLayout(new BorderLayout());
		
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				fireProgressCancelled();
			}
		});
		
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		
		DefaultCaret caret = (DefaultCaret)textArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		JScrollPane scroll = new JScrollPane(textArea);

		progressBar = new JProgressBar(0, 100);
		
		cancel = new JButton("Cancel");
		cancel.setActionCommand("cancel");
		cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				switch(e.getActionCommand()) {
				case "cancel" :
					fireProgressCancelled();
					break;
				case "ok" :
					fireProgressOk();
					break;
				}
			}
		});
		
		progressPanel = new JPanel(new BorderLayout());
		
		progressPanel.add(progressBar, BorderLayout.CENTER);
		progressPanel.add(cancel, BorderLayout.LINE_END);
		
		progressBar.setStringPainted(true);
		
		add(scroll, BorderLayout.CENTER);
		add(progressPanel, BorderLayout.PAGE_END);
		
	}
	
	/**
	 * change the cancel button into an Ok button
	 */
	public void switchOk() {
		Dimension d = cancel.getPreferredSize();
		cancel.setText("OK");
		cancel.setActionCommand("ok");
		cancel.setPreferredSize(d);
	}
	
	public void setProgress(int progress) {
		progressBar.setValue(progress);
	}
	
	public void setMaxProgress(int maxProgress) {
		progressBar.setMaximum(maxProgress);
	}
	
	public void setMinProgress(int minProgress) {
		progressBar.setMinimum(minProgress);
	}
	
	public int getMaxProgress() {
		return progressBar.getMaximum();
	}
	
	public int getMinProgress() {
		return progressBar.getMinimum();
	}
	
	public void setIndeterminate(boolean indeterminate) {
		progressBar.setIndeterminate(indeterminate);
	}
	
	public int getProgress() {
		return progressBar.getValue();
	}
	
	public void setMessage(String message) {
		textArea.setText(message);
	}
	
	public void appendMessage(String message) {
		textArea.append(message+"\n");
	}
	
	public void appendMessageEDT(final String message) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override public void run() {
				appendMessage(message);
			}
		});
	}
	
	public String getMessage() {
		return textArea.getText();
	}
	
	private void fireProgressCancelled() {
		if(progressListener != null)
			progressListener.progressCancelled();
		setVisible(false);
	}
	
	private void fireProgressOk() {
		if(progressListener != null)
			progressListener.progressOk();
		setVisible(false);
	}
	
	public void setProgressListener(ProgressDialogListener progressListener) {
		this.progressListener = progressListener;
	}
}
