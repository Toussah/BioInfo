package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.tree.TreePath;

import utils.CheckTreeManager;

public class MainAppPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9044158207754090405L;
	private Image background_img;
	private JTree tree;
	private CheckTreeManager checkTreeManager;
	private JTextArea textArea;
	private StatusInfoPanel bottomPanel;
	private ActionControlPanel actionControlPanel;
	private JScrollPane treeScrollPane;
	
	/**
	 * Create the panel.
	 */
	
	public MainAppPanel() {
		setLayout(new BorderLayout());
		
	}
	
	public void buildContent(JTree tree){
		this.setBounds(800, 400, 800, 500);
		
		// + checkTreeManager.getSelectionModel().getSelectionCount());
		
		checkTreeManager = new CheckTreeManager(tree);
		
		treeScrollPane = new JScrollPane();
		
		treeScrollPane.setViewportView(tree);

		actionControlPanel = new ActionControlPanel();
		
		JPanel topPanel = new JPanel();
		
		topPanel.setLayout(new BorderLayout());
		
		topPanel.add(treeScrollPane, BorderLayout.CENTER);
		topPanel.add(actionControlPanel, BorderLayout.EAST);
		
		bottomPanel = new StatusInfoPanel(); 
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, topPanel, bottomPanel);
		
		Dimension min_size = new Dimension(800, 200);
		
		topPanel.setMinimumSize(min_size);
		bottomPanel.setMinimumSize(new Dimension(800, 100));
		splitPane.setMinimumSize(new Dimension(800, 800));
		
		splitPane.setOneTouchExpandable(true);
		this.add(splitPane, BorderLayout.CENTER);
		textArea = bottomPanel.getTextArea();
		
		ImageIcon labelimg = new ImageIcon(InitAppPanel.class.getResource(AppPaths.APP_MAIN_BACKGROUND ));
		background_img = labelimg.getImage();
	}
	
	public void setCheckBoxTree(JTree t){
		tree = t;
		checkTreeManager = new CheckTreeManager(tree); 
		treeScrollPane.setViewportView(tree);
	}
	
	public TreePath[] getTreeSelection(){
		return checkTreeManager.getSelectionModel().getSelectionPaths(); 
	}
	
	public void printSelectedPaths(){
		for( TreePath path : getTreeSelection()){
			textArea.setText(textArea.getText() + "\n" + path);
		}
	}
	
	public void changeSelectedItemsValue(int v){
		this.actionControlPanel.setSelectedItems(v);
	}
	
	public ActionControlPanel getActionControlPanel(){
		return this.actionControlPanel;
	}
	
	public StatusInfoPanel getStatusInfoPanel(){
		return this.bottomPanel;
	}
	
	public void setProgressBarValue(int i){
		
	}

	@Override
	public void paintComponent(final Graphics g) {
	    super.paintComponent(g);
	    if (background_img != null)
	        g.drawImage(background_img, 0, 0, this.getWidth(), this.getHeight(), null);
	}

}