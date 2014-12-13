package utils;

import javax.swing.tree.TreePath;
import utils.GenomeTreeNode;

public class GenomePath {

	public String kingdom_name;
	public String group_name;
	public String subgroup_name;
	public String genome_name;

	private boolean kingdom = false;
	private boolean group = false;
	private boolean subgroup = false;
	private boolean genome = false;
	
	
	public GenomePath(TreePath p){
		String ps = p.toString();
		String tmp = ps.substring(1, ps.length()-2);
		String[] t = tmp.split(", ");
		
		if(t.length > 4){
			genome = true;
			genome_name = ((GenomeTreeNode) p.getLastPathComponent()).name;
			p = p.getParentPath();
		}
		if(t.length > 3){
			subgroup = true;
			subgroup_name = ((GenomeTreeNode) p.getLastPathComponent()).name;
			p = p.getParentPath();
		}
		if(t.length > 2){
			group = true;
			group_name = ((GenomeTreeNode) p.getLastPathComponent()).name;
			p = p.getParentPath();
		}
		if(t.length > 1){
			kingdom = true;
			kingdom_name = ((GenomeTreeNode) p.getLastPathComponent()).name;
		}
		
	}

	public boolean hasKingdom(){
		return kingdom;
	}
	
	public boolean hasGroup(){
		return group;
	}
	
	public boolean hasSubgroup(){
		return subgroup;
	}
	
	public boolean hasGenome(){
		return genome;
	}
	
}
