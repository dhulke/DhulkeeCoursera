package gui;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {
	
	public static void createAndShowGui() {
//		JFrame.setDefaultLookAndFeelDecorated(true);
		JFrame mainFrame = new JFrame("Dhulkee Coursera Downloader");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setContentPane(new MainPanel(mainFrame));
		mainFrame.setSize(700, 500);
		mainFrame.setVisible(true);
	}

	public static void main(String[] args) {
		
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {

				try {
					for (UIManager.LookAndFeelInfo info : UIManager
							.getInstalledLookAndFeels()) {
						if ("Nimbus".equals(info.getName())) {
							UIManager.setLookAndFeel(info.getClassName());
							break;
						}
					}
				} catch (Exception e) {
					// If Nimbus is not available, you can set the GUI to
					// another look and feel.
				}
				createAndShowGui();
			}
		});
	}

}
