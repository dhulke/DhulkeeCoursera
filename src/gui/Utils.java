package gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.net.URL;

import javax.swing.ImageIcon;

public class Utils {

	public static ImageIcon icon(String name) {
		URL url = Utils.class.getResource("/images/" + name);
		return new ImageIcon(url);
	}

	public static Point getMiddlePoint(Component parent, Component child) {
		Point p = parent.getLocationOnScreen();
		Dimension parendSize = parent.getSize();
		Dimension childSize = child.getSize();

		/*
		 * For some reason setLocationRelativeTo doesn't work so I set the
		 * positioning manually to be in the middle of the parent window
		 */
		int pointX = p.x + ((parendSize.width - childSize.width) / 2);
		int pointY = p.y + ((parendSize.height - childSize.height) / 2);

		return new Point(pointX, pointY);
	}
}
