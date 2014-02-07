package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Path;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class ConfigurationDialog extends FixedJDialog implements ActionListener {

	private JLabel directoryLabel;
	private JButton chooser;
	private JPanel chooserPanel;
	private JFileChooser fileChooser;
	
	private JPanel optionsPanel;
	private JCheckBox overwrite;
	private JCheckBox index;
	
	private JPanel buttonsPanel;
	private JButton ok;
	private JButton cancel;
	
	private Path directory;
	
	public enum ButtonPressed {
		OK, CANCEL
	}
	
	private ButtonPressed buttonPressed;
	
	public ConfigurationDialog(Window window) {
		super(window, "Configuration");
		setLayout(new BorderLayout());
		setResizable(false);
		
		JPanel dialog = (JPanel)getContentPane();
		dialog.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));

		Border inner = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		
		chooserPanel = new JPanel(new FlowLayout());
		Border outerChooser = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Download Directory");
		chooserPanel.setBorder(BorderFactory.createCompoundBorder(outerChooser, inner));
		
		fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		directory = fileChooser.getCurrentDirectory().toPath();
		
		directoryLabel = new JLabel(directory.toString());
		Dimension d = directoryLabel.getPreferredSize();
		d.width = 400;
		directoryLabel.setPreferredSize(d);
		
		chooser = new JButton("...");
		chooser.setToolTipText("Choose a directory where the files will be downloaded to");
		chooser.setActionCommand("chooser");
		chooser.addActionListener(this);
		
		chooserPanel.add(directoryLabel);
		chooserPanel.add(chooser);
		
		
		optionsPanel = new JPanel();
		optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.PAGE_AXIS));
		Border outerOptions = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Options");
		optionsPanel.setBorder(BorderFactory.createCompoundBorder(outerOptions, inner));
		
		overwrite = new JCheckBox("Overwrite files?");
		overwrite.setSelected(true);
		overwrite.setMargin(new Insets(0, 0, 3, 0));
		
		index = new JCheckBox("Add a number to the file name to keep the order?");
		index.setSelected(true);
		
		optionsPanel.add(overwrite);
		optionsPanel.add(index);
		
		
		buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		buttonsPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
		
		ok = new JButton("OK");
		ok.setActionCommand("ok");
		ok.addActionListener(this);
		
		cancel = new JButton("Cancel");
		cancel.setActionCommand("cancel");
		cancel.addActionListener(this);
		
		Dimension dOk = ok.getPreferredSize();
		Dimension cOk = cancel.getPreferredSize();
		
		dOk.width = cOk.width;
		
		ok.setPreferredSize(dOk);
		
		
		buttonsPanel.add(ok);
		buttonsPanel.add(cancel);
		

		add(chooserPanel, BorderLayout.PAGE_START);
		add(optionsPanel, BorderLayout.CENTER);
		add(buttonsPanel, BorderLayout.PAGE_END);
		
		pack();
	}

	public Path getDirectory() {
		return directory;
	}
	
	public boolean getOverwrite() {
		return overwrite.isSelected();
	}
	
	public boolean getIndex() {
		return index.isSelected();
	}
	
	public ButtonPressed getButtonPressed() {
		return buttonPressed;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		File fileSelected;
		
		switch(e.getActionCommand()) {
		case "chooser" :
			fileChooser.showDialog(this, "Select");
			fileSelected = fileChooser.getSelectedFile();
			if(fileSelected != null) {
				Path path = fileSelected.toPath();
				directoryLabel.setText(path.toAbsolutePath().toString());
			}
			break;
		case "ok" :
			buttonPressed = ButtonPressed.OK;
			
			fileSelected = fileChooser.getSelectedFile();
			if(fileSelected != null)
				directory = fileChooser.getSelectedFile().toPath();
			
			setVisible(false);
			break;
		case "cancel" :
			buttonPressed = ButtonPressed.CANCEL;
			
			setVisible(false);
			directoryLabel.setText(directory.toString());
			break;
		}
	}
	
}
