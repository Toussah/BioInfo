package utils;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JCheckBox;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import ui.MainWindow;

public class CheckTreeManager extends MouseAdapter implements TreeSelectionListener{ 
    private CheckTreeSelectionModel selectionModel; 
    private JTree tree = new JTree(); 
    int hotspot = new JCheckBox().getPreferredSize().width; 
    public int selected = 0;
 
    public CheckTreeManager(JTree tree){ 
        this.tree = tree; 
        selectionModel = new CheckTreeSelectionModel(tree.getModel()); 
        tree.setCellRenderer(new CheckTreeCellRenderer(tree.getCellRenderer(), selectionModel)); 
        tree.addMouseListener(this); 
        selectionModel.addTreeSelectionListener(this); 
    } 
 
    public void mouseClicked(MouseEvent me){ 
        TreePath path = tree.getPathForLocation(me.getX(), me.getY()); 
        if(path==null) 
            return; 
        if(me.getX()>tree.getPathBounds(path).x+hotspot) 
            return; 
 
        boolean selected = selectionModel.isPathSelected(path, true); 
        selectionModel.removeTreeSelectionListener(this); 
 
        try{ 
            if(selected) 
                selectionModel.removeSelectionPath(path); 
            else 
                selectionModel.addSelectionPath(path); 
        } finally{ 
            selectionModel.addTreeSelectionListener(this); 
            tree.treeDidChange(); 
        } 
        update_selected_item(selectionModel.getSelectionPaths());
       

    }
    
    private void update_selected_item(TreePath[] paths){
    	 int s = 0;
         for(int i=0; i < paths.length; i++){
         	TreePath tp = paths[i];
         	TreeNode tn = (TreeNode) tp.getLastPathComponent();
         	s += count_childs_node(tn);
         	
         }
         MainWindow.getInstance().setSelectedItems(s);
    }
    
    private int count_childs_node(TreeNode tn){
    	int i,s;
    	i = 0;
    	s = 0;
    	if(tn.isLeaf()){
    		s = 1;
    	}else{
	    	while(i < tn.getChildCount()){
	    		s += count_childs_node(tn.getChildAt(i));
	    		i++;
	    	}
    	}
    	return s;
    }
 
    public CheckTreeSelectionModel getSelectionModel(){ 
        return selectionModel; 
    } 
 
    public void valueChanged(TreeSelectionEvent e){ 
        tree.treeDidChange(); 
    }

}
