package utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

import javax.swing.JTree;
import javax.swing.tree.TreePath;

import org.json.JSONArray;
import org.json.JSONObject;

import app.App;


public class Linker {

	public static JTree JTreeFromJsonArray(JSONArray organizedJson){
		JTree tree = null;
		
		int len = organizedJson.length();
		int i,j,k,l;
		GenomeTreeNode root_node = new GenomeTreeNode("Genomes", GenomeTreeNode.ALL_TYPE);
		
		for(i = 0; i < len; i++)
		{
			String kingdom_name = organizedJson.getJSONObject(i).getString("kingdom");
			GenomeTreeNode kingdom_node = new GenomeTreeNode(kingdom_name, GenomeTreeNode.KINGDOM_TYPE);
			JSONArray groups = organizedJson.getJSONObject(i).getJSONArray("groups");
			int leng = groups.length();
			for(j = 0; j < leng; j++)
			{
				String group_name = groups.getJSONObject(j).getString("group");
				GenomeTreeNode group_node = new GenomeTreeNode(group_name, GenomeTreeNode.GROUP_TYPE);
				JSONArray subgroups = groups.getJSONObject(j).getJSONArray("subgroups");
				int lens = subgroups.length();
				for(k = 0; k < lens; k++)
				{
					String subgroup_name = subgroups.getJSONObject(k).getString("subgroup");
					GenomeTreeNode subgroup_node = new GenomeTreeNode(subgroup_name, GenomeTreeNode.SUBGROUP_TYPE);
					JSONArray genomes = subgroups.getJSONObject(k).getJSONArray("genomes");
					int lenc = genomes.length();
					for(l = 0; l < lenc; l++)
					{						
						String genome_name = genomes.getJSONObject(l).getString("name");
						GenomeTreeNode genome_node = new GenomeTreeNode(genome_name, GenomeTreeNode.GENOME_TYPE);
						subgroup_node.add(genome_node);
					}
					group_node.add(subgroup_node);
				}
				kingdom_node.add(group_node);
			}
			root_node.add(kingdom_node);
		}
	
		tree = new JTree(root_node);
		
		return tree;
	}
	
	public static int[] genomesIdListFromJsonArray(JSONArray organizedJson){

		int len = organizedJson.length();
		int i,j,k,l;
		ArrayList<Integer> id_list = new ArrayList<Integer>();
		for(i = 0; i < len; i++)
		{
			JSONArray groups = organizedJson.getJSONObject(i).getJSONArray("groups");
			int leng = groups.length();
			for(j = 0; j < leng; j++)
			{
				JSONArray subgroups = groups.getJSONObject(j).getJSONArray("subgroups");
				int lens = subgroups.length();
				for(k = 0; k < lens; k++)
				{
					JSONArray genomes = subgroups.getJSONObject(k).getJSONArray("genomes");
					int lenc = genomes.length();
					for(l = 0; l < lenc; l++)
					{						
						JSONObject genome = genomes.getJSONObject(l);
						id_list.add(new Integer(genome.getInt("taxid")));
					}
				}
			}
		}
		
		int i_tab[] = new int[id_list.size()];
		
		for(i = 0; i < id_list.size(); i++){
			i_tab[i] = id_list.get(i).intValue();
		}
		
		return i_tab;
	}
	
	public static int[] genomesIdListOfKingdomFromJsonArray(JSONArray organizedJson, String key){

		int len = organizedJson.length();
		int i,j,k,l;
		ArrayList<Integer> id_list = new ArrayList<Integer>();
		boolean ok = false;
		for(i = 0; i < len && !ok; i++)
		{
			JSONObject kingdom = organizedJson.getJSONObject(i);
			
			if((kingdom.getString("kingdom")).equals(key)){
				JSONArray groups = kingdom.getJSONArray("groups");
				int leng = groups.length();
				for(j = 0; j < leng; j++)
				{
					JSONArray subgroups = groups.getJSONObject(j).getJSONArray("subgroups");
					int lens = subgroups.length();
					for(k = 0; k < lens; k++)
					{
						JSONArray genomes = subgroups.getJSONObject(k).getJSONArray("genomes");
						int lenc = genomes.length();
						for(l = 0; l < lenc; l++)
						{						
							JSONObject genome = genomes.getJSONObject(l);
							id_list.add(new Integer(genome.getInt("taxid")));
						}
					}
				}
				ok = true;
			}
		}
		
		int i_tab[] = new int[id_list.size()];
		
		for(i = 0; i < id_list.size(); i++){
			i_tab[i] = id_list.get(i).intValue();
		}
		
		return i_tab;
	}
	
	public static int[] genomesIdListOfGroupFromJsonArray(JSONArray organizedJson, String key){

		int len = organizedJson.length();
		int i,j,k,l;
		ArrayList<Integer> id_list = new ArrayList<Integer>();
		boolean ok = false;
		for(i = 0; i < len && !ok; i++)
		{
			JSONArray groups = organizedJson.getJSONObject(i).getJSONArray("groups");
			int leng = groups.length();
			for(j = 0; j < leng && !ok; j++)
			{
				JSONObject group = groups.getJSONObject(j);
				
				if((group.getString("group")).equals(key)){
					JSONArray subgroups = groups.getJSONObject(j).getJSONArray("subgroups");
					int lens = subgroups.length();
					for(k = 0; k < lens; k++)
					{
						JSONArray genomes = subgroups.getJSONObject(k).getJSONArray("genomes");
						int lenc = genomes.length();
						for(l = 0; l < lenc; l++)
						{						
							JSONObject genome = genomes.getJSONObject(l);
							id_list.add(new Integer(genome.getInt("taxid")));
						}
					}
					ok = true;
				}
			}
		}
		
		int i_tab[] = new int[id_list.size()];
		
		for(i = 0; i < id_list.size(); i++){
			i_tab[i] = id_list.get(i).intValue();
		}
		
		return i_tab;
	}
	
	public static int[] genomesIdListOfSubGroupFromJsonArray(JSONArray organizedJson, String key){
		int len = organizedJson.length();
		int i,j,k,l;
		ArrayList<Integer> id_list = new ArrayList<Integer>();
		boolean ok = false;
		for(i = 0; i < len && !ok; i++)
		{
			JSONArray groups = organizedJson.getJSONObject(i).getJSONArray("groups");
			int leng = groups.length();
			for(j = 0; j < leng && !ok; j++)
			{
				JSONArray subgroups = groups.getJSONObject(j).getJSONArray("subgroups");
				int lens = subgroups.length();
				for(k = 0; k < lens && !ok; k++)
				{
					JSONObject subgroup = subgroups.getJSONObject(k);
					if((subgroup.getString("subgroup")).equals(key)){
						JSONArray genomes = subgroups.getJSONObject(k).getJSONArray("genomes");
						int lenc = genomes.length();
						for(l = 0; l < lenc; l++)
						{						
							JSONObject genome = genomes.getJSONObject(l);
							id_list.add(new Integer(genome.getInt("taxid")));
						}
						ok = true;
					}
				}
			}
		}
		
		int i_tab[] = new int[id_list.size()];
		
		for(i = 0; i < id_list.size(); i++){
			i_tab[i] = id_list.get(i).intValue();
		}
		
		return i_tab;
	}
	
	public static int genomeIdFromJsonArray(JSONArray organizedJson, String name){

		int len = organizedJson.length();
		int i,j,k,l;
		int id = -1;
		boolean ok = false;
		for(i = 0; i < len && !ok; i++)
		{
			JSONArray groups = organizedJson.getJSONObject(i).getJSONArray("groups");
			int leng = groups.length();
			for(j = 0; j < leng && !ok; j++)
			{
				JSONArray subgroups = groups.getJSONObject(j).getJSONArray("subgroups");
				int lens = subgroups.length();
				for(k = 0; k < lens && !ok; k++)
				{
					JSONArray genomes = subgroups.getJSONObject(k).getJSONArray("genomes");
					int lenc = genomes.length();
					for(l = 0; l < lenc && !ok; l++)
					{						
						JSONObject genome = genomes.getJSONObject(l);
						if((genome.getString("name")).equals(name)){
							id = genome.getInt("taxid");
							ok = true;
						}
					}
				}
			}
		}
		
		return id;
	}
	
	public static int[] genomeIdsFromJSONArray(JSONArray organizedJson, TreePath path){

		int len = organizedJson.length();

		GenomePath gp = new GenomePath(path);
		
		int i,j,k,l;
		ArrayList<Integer> id_list = new ArrayList<Integer>();
		String key = "";
		boolean ok = false;
		for(i = 0; i < len && !ok; i++)
		{
			key = "";
			if(gp.hasKingdom()){
				key = gp.kingdom_name;
			}
			JSONObject kingdom = organizedJson.getJSONObject(i);
			
			if(key.equals("") || (kingdom.getString("kingdom")).equals(key)){
				JSONArray groups = organizedJson.getJSONObject(i).getJSONArray("groups");
				int leng = groups.length();
				for(j = 0; j < leng && !ok; j++)
				{
					key = "";
					if(gp.hasGroup()){
						key = gp.group_name;
					}
					JSONObject group = groups.getJSONObject(j);
					
					if(key.equals("") || (group.getString("group")).equals(key)){
						JSONArray subgroups = group.getJSONArray("subgroups");
						int lens = subgroups.length();
						for(k = 0; k < lens && !ok; k++)
						{
							key = "";
							if(gp.hasSubgroup()){
								key = gp.subgroup_name;
							}
							JSONObject subgroup = subgroups.getJSONObject(k);
							
							if(key.equals("") || (subgroup.getString("subgroup")).equals(key)){
								JSONArray genomes = subgroup.getJSONArray("genomes");
								int lenc = genomes.length();
								for(l = 0; l < lenc && !ok; l++)
								{						
									key = "";
									if(gp.hasGenome()){
										key = gp.genome_name;
									}
									JSONObject genome = genomes.getJSONObject(l);
									
									if(key.equals("") || (genome.getString("name")).equals(key)){
										id_list.add(new Integer(genome.getInt("taxid")));
									}
								}
							}
						}
					}
				}
			}
		}
		
		int i_tab[] = new int[id_list.size()];
		
		for(i = 0; i < id_list.size(); i++){
			i_tab[i] = id_list.get(i).intValue();
		}
		
		return i_tab;
	}
	
	
	
	public static JSONArray JSONArrayOfAllFromJsonArray(JSONArray organizedJson){

		int len = organizedJson.length();
		int i,j,k,l;
		JSONArray json = new JSONArray();
		for(i = 0; i < len; i++)
		{
			JSONObject kingdom = organizedJson.getJSONObject(i);
			JSONArray groups = kingdom.getJSONArray("groups");
			int leng = groups.length();
			for(j = 0; j < leng; j++)
			{
				JSONArray subgroups = groups.getJSONObject(j).getJSONArray("subgroups");
				int lens = subgroups.length();
				for(k = 0; k < lens; k++)
				{
					JSONArray genomes = subgroups.getJSONObject(k).getJSONArray("genomes");
					int lenc = genomes.length();
					for(l = 0; l < lenc; l++)
					{						
						JSONObject genome = genomes.getJSONObject(l);
						json.put(genome);
					}
				}
			}
		}
		
		return json;
	}
	
	public static JSONArray JSONArrayOfKingdomFromJsonArray(JSONArray organizedJson, String key){

		int len = organizedJson.length();
		int i,j,k,l;
		JSONArray json = new JSONArray();
		String kingdom_name;
		boolean ok = false;
		for(i = 0; i < len && !ok; i++)
		{
			JSONObject kingdom = organizedJson.getJSONObject(i);
			kingdom_name = kingdom.getString("kingdom");
			if(kingdom_name.equals(key)){
				JSONArray groups = kingdom.getJSONArray("groups");
				int leng = groups.length();
				for(j = 0; j < leng && !ok; j++)
				{
					JSONArray subgroups = groups.getJSONObject(j).getJSONArray("subgroups");
					int lens = subgroups.length();
					for(k = 0; k < lens && !ok; k++)
					{
						JSONArray genomes = subgroups.getJSONObject(k).getJSONArray("genomes");
						int lenc = genomes.length();
						for(l = 0; l < lenc && !ok; l++)
						{						
							JSONObject genome = genomes.getJSONObject(l);
							json.put(genome);
						}
					}
				}
				ok = true;
			}
		}
		
		return json;
	}
	
	public static JSONArray JSONArrayOfGroupFromJsonArray(JSONArray organizedJson, String key){

		int len = organizedJson.length();
		int i,j, k,l;
		JSONArray json = new JSONArray();

		String group_name;
		boolean ok = false;
		for(i = 0; i < len && !ok; i++)
		{
			JSONArray groups = organizedJson.getJSONObject(i).getJSONArray("groups");
			int leng = groups.length();
			for(j = 0; j < leng && !ok; j++)
			{
				group_name = groups.getJSONObject(j).getString("group");
				if(group_name.equals(key)){
					JSONArray subgroups = groups.getJSONObject(j).getJSONArray("subgroups");
					int lens = subgroups.length();
					for(k = 0; k < lens && !ok; k++)
					{
						JSONArray genomes = subgroups.getJSONObject(k).getJSONArray("genomes");
						int lenc = genomes.length();
						for(l = 0; l < lenc && !ok; l++)
						{						
							JSONObject genome = genomes.getJSONObject(l);
							json.put(genome);
						}
					}
					ok = true;
				}
			}
		}
		
		return json;
	}
	
	public static JSONArray JSONArrayOfSubgroupFromJsonArray(JSONArray organizedJson, String key){

		int len = organizedJson.length();
		int i,j,k,l;
		JSONArray json = new JSONArray();
		String subgroup_name;
		boolean ok = false;
		for(i = 0; i < len && !ok; i++)
		{
			JSONArray groups = organizedJson.getJSONObject(i).getJSONArray("groups");
			int leng = groups.length();
			for(j = 0; j < leng && !ok; j++)
			{
				JSONArray subgroups = groups.getJSONObject(j).getJSONArray("subgroups");
				int lens = subgroups.length();
				for(k = 0; k < lens && !ok; k++)
				{
					subgroup_name = subgroups.getJSONObject(k).getString("subgroup");
					if(subgroup_name.equals(key)){
						JSONArray genomes = subgroups.getJSONObject(k).getJSONArray("genomes");
						int lenc = genomes.length();
						for(l = 0; l < lenc && !ok; l++)
						{						
							JSONObject genome = genomes.getJSONObject(l);
							json.put(genome);
						}
						ok = true;
					}
				}
			}
		}
		
		return json;
	}
	
	public static JSONArray JSONArrayOfGenomeFromJsonArray(JSONArray organizedJson, String name){

		int len = organizedJson.length();
		int i,j,k,l;
		JSONArray json = new JSONArray();
		boolean ok = false;
		for(i = 0; i < len && !ok; i++)
		{
			JSONArray groups = organizedJson.getJSONObject(i).getJSONArray("groups");
			int leng = groups.length();
			for(j = 0; j < leng && !ok; j++)
			{
				JSONArray subgroups = groups.getJSONObject(j).getJSONArray("subgroups");
				int lens = subgroups.length();
				for(k = 0; k < lens && !ok; k++)
				{
					JSONArray genomes = subgroups.getJSONObject(k).getJSONArray("genomes");
					int lenc = genomes.length();
					for(l = 0; l < lenc && !ok; l++)
					{						
						JSONObject genome = genomes.getJSONObject(l);
						if((genome.getString("name")).equals(name)){
							json.put(genome);
							ok = true;
						}
					}
				}
			}
		}
		
		
		return json;
	}

	public static void createPaths(JSONArray organizedJson)
	{
		int i,j,k,l;
		
		int len = organizedJson.length();
		for(i = 0; i < len; i++)
		{
			JSONArray groups = organizedJson.getJSONObject(i).getJSONArray("groups");
			int leng = groups.length();
			for(j = 0; j < leng; j++)
			{
				JSONArray subgroups = groups.getJSONObject(j).getJSONArray("subgroups");
				int lens = subgroups.length();
				for(k = 0; k < lens; k++)
				{
					JSONArray genomes = subgroups.getJSONObject(k).getJSONArray("genomes");
					int lenc = genomes.length();
					for(l = 0; l < lenc; l++)
					{						
						JSONObject genome = genomes.getJSONObject(l);
							String kingdomName = organizedJson.getJSONObject(i).getString("kingdom");
							String kingdomPath = "Genomes" + App.FILE_SEP + kingdomName.replaceAll("[\\/\\<\\>\\?|\\*\"\\:]", " ");
							
							String groupName = groups.getJSONObject(j).getString("group");
							String groupPath = kingdomPath + App.FILE_SEP + groupName.replaceAll("[\\/\\<\\>\\?|\\*\"\\:]", " ");
							
							String subGroupName = subgroups.getJSONObject(k).getString("subgroup");
							String subGroupPath = groupPath + App.FILE_SEP + subGroupName.replaceAll("[\\/\\<\\>\\?|\\*\"\\:]", " ");
							
							String name = genome.getString("name");
							String genomePath = subGroupPath + App.FILE_SEP +  name.replaceAll("[\\/\\<\\>\\?|\\*\"\\:]", " ");
							
							//Genome directory creation :
							File theDir = new File(genomePath);
							theDir.mkdirs();
					}
				}
			}
		}
	}

	public static GenomeData genomeFromJsonObject(JSONObject o){
		GenomeData gd = new GenomeData();
		JSONArray tmp;
		
		gd.name = o.getString("name");
		
		gd.path = o.getString("path");
		
		gd.taxid = o.getInt("taxid");
		
		tmp = o.getJSONArray("chromosomes");
		for(int i=0; i < tmp.length(); i++){
			gd.chromosomes.add(tmp.getString(i));
		}
		
		tmp = o.getJSONArray("mitochondrions");
		for(int i=0; i < tmp.length(); i++){
			gd.mitochondrions.add(tmp.getString(i));
		}
		
		tmp = o.getJSONArray("plasmids");
		for(int i=0; i < tmp.length(); i++){
			gd.plasmids.add(tmp.getString(i));
		}
		
		tmp = o.getJSONArray("chloroplasts");
		for(int i=0; i < tmp.length(); i++){
			gd.chloroplasts.add(tmp.getString(i));
		}
		
		return gd;
	}

	public static void updateOrganizedJson(JSONArray js, JSONArray result) {
		
		int i,j,k,l,m;
		boolean ok=false;
				
		int len = js.length();
		for(i = 0; i < len && !ok; i++)
		{
			JSONArray groups = js.getJSONObject(i).getJSONArray("groups");
			int leng = groups.length();
			for(j = 0; j < leng && !ok; j++)
			{
				JSONArray subgroups = groups.getJSONObject(j).getJSONArray("subgroups");
				int lens = subgroups.length();
				for(k = 0; k < lens && !ok; k++)
				{
					JSONArray genomes = subgroups.getJSONObject(k).getJSONArray("genomes");
					int lenc = genomes.length();
					for(l = 0; l < lenc && !ok; l++)
					{	
						JSONObject jo = genomes.getJSONObject(l);
						for(m = 0; m < result.length(); m++){
							JSONObject ro = result.getJSONObject(m);
							if(jo.getInt("taxid") == ro.getInt("taxid")){
								jo.put("chromosomes", ro.getJSONArray("chromosomes"));
								jo.put("plasmids", ro.getJSONArray("plasmids"));
								jo.put("mitochondrions", ro.getJSONArray("mitochondrions"));
								jo.put("chloroplasts", ro.getJSONArray("chloroplasts"));
								result.remove(m);
								if(result.length() == 0){
									ok = true;
								}
								break;
							}
						}
					}
				}
			}
		}
					
		Writer writer = null;
		try 
		{
			String output = null;
			if(App.fineStatistics)
			{
				output = Interpreter.FineStats;
			}
			else
			{
				output = Interpreter.MassiveStats;
			}

			writer = new BufferedWriter(new OutputStreamWriter(
		          new FileOutputStream(output), "utf-8"));
		    writer.write(js.toString(4));
		} 
		catch (IOException ex) 
		{
		  // report
		}
		finally 
		{
		   try {writer.close();} catch (Exception ex) {}
		}
	}
}

