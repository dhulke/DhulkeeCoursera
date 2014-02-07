package gui;

import java.awt.Point;
import java.awt.Window;

import javax.swing.JDialog;
import javax.swing.SwingUtilities;

public abstract class FixedJDialog extends JDialog {
	
	private Window window;
	
	public FixedJDialog(Window window, String title) {
		super(window, title);
		this.window = window;
	}
	
	@Override
	public void setVisible(final boolean visible) {
		if (visible) {
			Point newLocation = Utils.getMiddlePoint(window, FixedJDialog.this);
			setLocation(newLocation.x, newLocation.y);
		}
		FixedJDialog.super.setVisible(visible);
	}
	
	public void setVisibleEDT(final boolean visible) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override public void run() {
				FixedJDialog.this.setVisible(visible);
			}
		});
	}
}
