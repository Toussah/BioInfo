package ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.EventObject;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.event.CellEditorListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreeCellRenderer;


/**
 * The panel which contains a tree of checkboxes, that represent the taxonomy.
 */
public class CheckBoxTreePanel extends JPanel implements ActionListener {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel mainPanel;
    private JLabel nbOrganisms;
    private JScrollPane jsp;
    private JTree tree;
    // Array containing all checkboxes of the tree
    @SuppressWarnings("unused")
	private ArrayList<MyCheckBox> listBoxes;
    
    /**
     * Creates the panel containing the tree of checkboxes
     */
    public CheckBoxTreePanel() {
        super(new BorderLayout());
        
        // Init the tree
        this.initTree();
        
        // Init the selected organisms counter
        this.nbOrganisms = new JLabel("Nombre d'organismes selectionnes : 0");
        this.nbOrganisms.setHorizontalAlignment(SwingConstants.CENTER); 
        
        // Init the main panel
        this.mainPanel = new JPanel(new BorderLayout());
        this.jsp.setPreferredSize(new Dimension(800, 300));
        this.mainPanel.add(this.jsp, BorderLayout.WEST);
        this.mainPanel.add(this.nbOrganisms, BorderLayout.EAST);
        
        // Add the panel
        this.add(this.mainPanel, BorderLayout.WEST);
    }
    
    /**
     * Initialize the tree of checkboxes
     */
    private void initTree() {

        this.tree = new JTree();
        this.tree.expandRow(0);
        this.tree.setRootVisible(false);
        this.tree.setShowsRootHandles(true);

        RenduComposant rc = new RenduComposant();
        EditComposant ec = new EditComposant();
        this.tree.setCellRenderer(rc);
        this.tree.setCellEditor(ec);
        this.tree.setEditable(true);

        this.jsp = new JScrollPane(this.tree);
    }
    
    /**
     * Create all nodes recursively of the tree according to the taxonomy given
     * @param tree
     * @param tt 
     */
    /*
    private void createNodes(DefaultMutableTreeNode tree, TaxonomyTerm tt) {
        if(tt.getChildren().size() > 0) {
            for(int i = 0; i < tt.getChildren().size(); i++) {
                if(tt.getChildren().get(i).getNbOrganismsRec() > 0) {
                    JCheckBox jcb = new JCheckBox(tt.getChildren().get(i).toString());
                    jcb.setBackground(Color.white);
                    DefaultMutableTreeNode taxoElement = new DefaultMutableTreeNode(jcb, true);

                    jcb.addActionListener(this);
                    this.taxoBoxes.add(new MyCheckBox(jcb, taxoElement, tt.getChildren().get(i)));

                    tree.add(taxoElement);
                    this.createNodes(taxoElement, tt.getChildren().get(i));
                }
            }
        } else {
            if(tt.getNbOrganismsRec() > 0) {
                for(int i = 0; i < tt.getOrganisms().size(); i++) {
                    // It's the leaf : an organism. So we only add a String to display it. No checkbox.
                    DefaultMutableTreeNode taxoElement = new DefaultMutableTreeNode("<html>" + tt.getOrganisms().get(i).toStringExtendedHTML() + "</html>", true);
                    tree.add(taxoElement);
                }
            }
        }
    }
    */

    /**
     * Function called when a checkbox is selected, or unselected
     * We have to do some actions on other checkboxes (parents, children...)
     * @param e 
     */
    
    @Override
    public void actionPerformed(ActionEvent e) {
        /*
        for(int i = 0; i < this.taxoBoxes.size(); i++) {
            // We search for the checkbox that was just clicked
            if(source == this.taxoBoxes.get(i).jcb) {                
                // Is it selected ?
                if (source.isSelected()) {
                    // We set selected all his children           
                    this.setSelectedChildren(this.taxoBoxes.get(i).dmtn, true);                    
                    
                    // We set selected all his parents
                    for (Enumeration<DefaultMutableTreeNode> enume = this.taxoBoxes.get(i).dmtn.pathFromAncestorEnumeration(this.taxoBoxes.get(i).dmtn.getRoot()); enume.hasMoreElements();) {
                        DefaultMutableTreeNode node = enume.nextElement();
                        try {
                            JCheckBox cb = (JCheckBox) node.getUserObject();
                            cb.setSelected(true);
                        } catch (Exception ex) { }                        
                    }
                    break;
                } else {
                    // We deselect all his children
                    this.setSelectedChildren(this.taxoBoxes.get(i).dmtn, false);
                    break;
                }
            }
        }
        
        this.updateOrganismsCounter();
        */
        this.tree.repaint();
    }
    
    
    /**
     * Checks all of this node's children and recursively their children too
     * @param node 
     */
    @SuppressWarnings("unused")
	private void setSelectedChildren(DefaultMutableTreeNode node, boolean selected) {
        @SuppressWarnings("unchecked")
		Enumeration<DefaultMutableTreeNode> enume = node.children();
        while(enume.hasMoreElements()) {
            DefaultMutableTreeNode child = enume.nextElement();
            try {
                // Try to get the checkbox linked to this node
                JCheckBox cb = (JCheckBox) child.getUserObject();
                cb.setSelected(selected);
                this.setSelectedChildren(child, selected);
            } catch(Exception ex) {
                // There is no checkbox linked to this node : for exemple the root
                // or the leafs which contains only string for the organisms
            }
        }
    }
    
    /**
     * Return all taxonomy terms selected by the user.
     * @return 
     */
    /*
    public ArrayList<TaxonomyTerm> getSelectedTerms() {
        ArrayList<TaxonomyTerm> res = new ArrayList<TaxonomyTerm>();
        for(int i = 0; i < this.taxoBoxes.size(); i++) {
            if(this.taxoBoxes.get(i).jcb.isSelected()) {
                res.add(this.taxoBoxes.get(i).tt);
            }
        }
        return res;
    }*/
    
    /**
     * Return all organisms selected by the user.
     * @return 
     */
    /*
    public ArrayList<Organism> getSelectedOrganisms() {
        ArrayList<Organism> res = new ArrayList<Organism>();
        for(int i = 0; i < this.taxoBoxes.size(); i++) {
            // We search for terminal nodes, just before leafs
            if(this.taxoBoxes.get(i).jcb.isSelected() && this.taxoBoxes.get(i).tt.getChildren().size() == 0) {
                ArrayList<Organism> organisms = this.taxoBoxes.get(i).tt.getOrganisms();
                for(int j = 0; j < organisms.size(); j++) {
                    res.add(organisms.get(j));
                }
            }
        }
        return res;
    }*/
    
    /**
     * Updates the organisms counter
     */
    /*
    private void updateOrganismsCounter() {
        int nb = this.getSelectedOrganisms().size();
        this.nbOrganisms.setText("Nombre d'organismes selectionnes : " + nb);
    }*/
}

class RenduComposant implements TreeCellRenderer {
    /**
     * Tells to render a checkbox if wanted, or a label
     */
    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object obj, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus){
        DefaultMutableTreeNode dmtcr = (DefaultMutableTreeNode) obj;
        if(dmtcr.getUserObject() instanceof JCheckBox){
            JCheckBox cb = (JCheckBox) dmtcr.getUserObject();
            return cb;
        } else {
            JLabel jl = new JLabel(dmtcr.getUserObject().toString());
            return jl;
        }
    }
}


class EditComposant implements TreeCellEditor{
    @Override
    public void addCellEditorListener(CellEditorListener l) {}
    
    @Override
    public void cancelCellEditing() {}
    
    @Override
    public Object getCellEditorValue() { return this; }
    
    @Override
    public boolean isCellEditable(EventObject evt) {
        if(evt instanceof MouseEvent){
            MouseEvent mevt = (MouseEvent) evt;
            if (mevt.getClickCount() == 1){
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void removeCellEditorListener(CellEditorListener l) {}
    
    @Override
    public boolean shouldSelectCell(EventObject anEvent) { return true; }
    
    @Override
    public boolean stopCellEditing() { return false; }
    
    @Override
    public Component getTreeCellEditorComponent(JTree tree, Object obj, boolean isSelected, boolean expanded, boolean leaf, int row) {
        DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode) obj;
        if(dmtn.getUserObject() instanceof JCheckBox) {
            JCheckBox jcb = (JCheckBox) dmtn.getUserObject();
            jcb.setEnabled(true);
            return jcb;
        } else {
            return new JLabel(dmtn.getUserObject().toString());
        }       
    }
}

/**
 * Small class to link a checkbox with a tree node
 */

class MyCheckBox {
    public JCheckBox jcb;
    public DefaultMutableTreeNode dmtn;
    //public TaxonomyTerm tt;
    
    public MyCheckBox(JCheckBox jcb, DefaultMutableTreeNode dmtn/*, TaxonomyTerm tt*/) {
        this.jcb = jcb;
        this.dmtn = dmtn;
        //this.tt = tt;
    }
}
