package utils;

import javax.swing.tree.DefaultMutableTreeNode;

public class GenomeTreeNode extends DefaultMutableTreeNode{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5183354879460105894L;
	public static final int ALL_TYPE = 0;
	public static final int KINGDOM_TYPE = 1;
	public static final int GROUP_TYPE = 2;
	public static final int SUBGROUP_TYPE = 3;
	public static final int GENOME_TYPE = 4;
	
	public int type;
	public String name;
	public GenomeTreeNode(String pname, int ptype){
		super(pname.replaceAll("[\\,\\/\\<\\>\\?|\\*\"\\:]", " "));
		this.name = pname;
		this.type = ptype;
	}
	
}
