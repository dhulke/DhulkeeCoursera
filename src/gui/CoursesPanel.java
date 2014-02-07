package gui;

import it.cnr.imaa.essi.lablib.gui.checkboxtree.CheckboxTree;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

public class CoursesPanel extends JPanel implements ActionListener {

	
	private static CoursesPanel coursesPanel = new CoursesPanel();
	
	private JPanel miniToolBar; 
	CheckboxTree tree;
	
	public static CoursesPanel getInstace() {
		return coursesPanel;
	}
	
	private CoursesPanel() {
		setLayout(new BorderLayout());
		setBackground(Color.WHITE);
		
//		miniToolBar = new JPanel(new FlowLayout(FlowLayout.CENTER));
//		Dimension d = miniToolBar.getPreferredSize();
//		d.height = 15;
//		miniToolBar.setPreferredSize(d);
//		
//		JButton expand = new JButton("Expand All");
//		expand.setActionCommand("expand");
//		
//		JButton colapse = new JButton("Colapse All");
//		colapse.setActionCommand("colapse");
//		
//		Dimension b = expand.getPreferredSize();
//		b.height = 15;
//		expand.setPreferredSize(b);
//		colapse.setPreferredSize(b);
//		
//		miniToolBar.add(expand);
//		miniToolBar.add(colapse);
//		
//		add(miniToolBar, BorderLayout.PAGE_START);
	}
	
	public void setTreeModel(TreeModel treeModel) {
		
		this.removeAll();
		
        tree = new CheckboxTree(treeModel);
        tree.setShowsRootHandles(true);
        tree.setRootVisible(false);
        
        JScrollPane scrollPane = new JScrollPane(tree);
        scrollPane.setBorder(null);
        
//        add(miniToolBar, BorderLayout.PAGE_START);
		add(scrollPane, BorderLayout.CENTER);
		validate();
	}
	
	public CheckboxTree getTree() {
		return tree;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		switch(e.getActionCommand()) {
		case "expand" :
			tree.expandAll();
			System.out.println("lula");
			break;
		case "colapse" :
//			expandAll(tree, , false);
		}
		
	}
	
    static private void expandAll(CheckboxTree tree, TreePath parent, boolean expand) {
    	TreeNode node = (TreeNode) parent.getLastPathComponent();
	    if (node.getChildCount() >= 0) {
	        for (@SuppressWarnings("unchecked")
	        Enumeration<TreeNode> e = node.children(); e.hasMoreElements();) {
	        TreeNode treeNode = (TreeNode) e.nextElement();
	        TreePath path = parent.pathByAddingChild(treeNode);
	        expandAll(tree, path, expand);
	        }
	    }
	    // Expansion or collapse must be done bottom-up
	    if (expand) {
	        tree.expandPath(parent);
	    } else {
	        tree.collapsePath(parent);
	    }
    }
	
}
