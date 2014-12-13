package utils;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nuccore.NuccoreThread;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ui.AppLabels;
import ui.MainWindow;
import app.App;
import utils.RetrieveFTP;
import utils.CSVtoJSON;
/**
 * @author toussah
 *	Public class fetching the URLs and info of every genomes. 
 */
public class FetchURLs 
{
	
	/**
	 * 
	 */
	private String url; //The url of the database.
	private String FTPHostName; //The url of the ftp hostname.
	private JSONObject genomeJson; //The original JSON from the database.
	private JSONArray organizedJson; //A sorted representation of the original JSON, with the chromosomes' urls and with a tree with (kingdom/group/subgroup).
	
	private MainWindow w = MainWindow.getInstance();
	
	private int nb_proc;
	
	private static float val = 0;
	
	public void setURL(String url)
	{
		this.url = url;
	}
	
	public String getURL()
	{
		return url;
	}
	
	public JSONArray getOrganizedJson() {
		return organizedJson;
	}
	
	public void setOrganizedJson(JSONArray org) {
		this.organizedJson = org;
	}
	
	public void setOrganizedJson(String filepath) throws FileNotFoundException
	{
		File genomes = new File(filepath);
        Scanner s = new Scanner(genomes);
        String content = s.useDelimiter("\\Z").next();
        s.close();
        this.organizedJson = new JSONArray(content);
	}

	public String FTPHostName() {
		return FTPHostName;
	}

	public void setFTPHostName(String url) {
		this.FTPHostName = url;
	}
	
	public JSONObject getGenomeJson() {
		return genomeJson;
	}

	public void setGenomeJson(JSONObject genomeJson) {
		this.genomeJson = genomeJson;
	}
	
	public void setGenomeJson(String input) throws IOException 
	{
		File genomes = new File(input);
        Scanner s = new Scanner(genomes);
        String content = s.useDelimiter("\\Z").next();
        s.close();
        String genomeVar = content;
        this.genomeJson = new JSONObject(genomeVar);
	}

	public FetchURLs(String url, String FTPHostName) throws IOException
	{
		this.url = url;
		this.FTPHostName = FTPHostName;
		this.organizedJson = new JSONArray();
		this.genomeJson = new JSONObject();
	}
	
	public void init(String FTPComplete) throws Exception 
	{
		System.out.println("Connecting to the database...");
        RetrieveFTP ftp = new RetrieveFTP(FTPHostName, FTPComplete);
        System.out.println("Connected!");
        System.out.println("Fetching Prokaryotes...");
        File prokaryotes = ftp.getProkariotes();
        System.out.println("Fetched!");
        System.out.println("Fetching Eukaryotes...");
        File eukaryotes = ftp.getEukariotes();
        System.out.println("Fetched!");
        System.out.println("Fetching Viruses...");
        File viruses = ftp.getViruses();
        System.out.println("Fetched!");
       
        List<HashMap<String, String>> lsp = CSVtoJSON.conversion(prokaryotes, "Prokaryotes");
        List<HashMap<String, String>> lse = CSVtoJSON.conversion(eukaryotes, "Eukaryotes");
        List<HashMap<String, String>> lsv = CSVtoJSON.conversion(viruses, "Viruses");
   
        JSONArray genomes = new JSONArray(lsp);
        for(int i = 0; i < lse.size(); i++)
        {
        	genomes.put(lse.get(i));
        }
        for(int i = 0; i < lsv.size(); i++)
        {
        	genomes.put(lsv.get(i));
        }
        genomeJson = new JSONObject();
        genomeJson.put("genomes", genomes);
	}

	
	
	//Checks if a value exists in the different JSONObject (dictionnary) of a JSONArray, given its supposed key.
	//Returns the object if it exists, null otherwise.
	public JSONObject exists(JSONArray arr, String key, String value)
	{
		JSONObject res = null;
		int i;
		int len = arr.length();
		for(i = 0; i < len; i++)
		{
			JSONObject genome = arr.getJSONObject(i);
			if(value.equals((genome.get(key).toString())))
			{
				res = genome;
				break;
			}
		}
		return res;
	}
	
	//Sorts the JSON original object of the database, nesting the data to give it a correct tree view.
	public void sortJson(String output) throws IOException
	{
		int i;
		JSONArray genomes = genomeJson.getJSONArray("genomes");
		int len = genomes.length();
		JSONObject genome;
		String kingdom;
		String group;
		String subgroup;
		String bioproj;
		String name;
		String status;
		String modify_date;
		Boolean old = true;
		int taxid;
		String assemblyID;
		List<String> chromosomes;
		List<String> plasmids;
		List<String> chloroplasts;
		List<String> mitochondrions;
		
		//w.add_to_log("Number of genomes fetched : " + len);
		for (i = 0; i < len; i++)
		{
			genome = genomes.getJSONObject(i);
			kingdom = genome.getString("kingdom");
			group = genome.getString("group");
			subgroup = genome.getString("subgroup");
			bioproj = genome.getString("project_acc");
			taxid = Integer.parseInt(genome.getString("taxid"));
			modify_date = genome.getString("modify_date");
			assemblyID = "";
			name = genome.getString("name");
			status = genome.getString("status");
			
			assemblyID = genome.optString("assembly_id");
			chromosomes = new ArrayList<String>();
			plasmids = new ArrayList<String>();
			chloroplasts = new ArrayList<String>();
			mitochondrions = new ArrayList<String>();
			
			String kingdomPath = "Genomes" + App.FILE_SEP + kingdom.replaceAll("[\\/\\<\\>\\?|\\*\"\\:]", " ");
			String groupPath = kingdomPath + App.FILE_SEP + group.replaceAll("[\\/\\<\\>\\?|\\*\"\\:]", " ");
			String subgroupPath = groupPath + App.FILE_SEP + subgroup.replaceAll("[\\/\\<\\>\\?|\\*\"\\:]", " ");
			String genomePath = subgroupPath + App.FILE_SEP +  name.replaceAll("[\\/\\<\\>\\?|\\*\"\\:]", " ");
			
			if((!bioproj.equals("") || !assemblyID.equals("")) && !status.equalsIgnoreCase("scaffold") && !status.equalsIgnoreCase("contig"))
			{
				JSONObject king_obj = exists(organizedJson, "kingdom", kingdom);
				if(king_obj == null)
				{
					king_obj = new JSONObject();
					king_obj.put("kingdom", kingdom);
					king_obj.put("groups", new JSONArray());
					organizedJson.put(king_obj);
				}
				
				JSONObject group_obj = exists(king_obj.getJSONArray("groups"), "group", group);
				if(group_obj == null)
				{
					group_obj = new JSONObject();
					group_obj.put("group", group);
					group_obj.put("subgroups", new JSONArray());
					king_obj.accumulate("groups", group_obj);
				}
				
				JSONObject subgroup_obj = exists(group_obj.getJSONArray("subgroups"), "subgroup", subgroup);
				if(subgroup_obj == null)
				{
					subgroup_obj = new JSONObject();
					subgroup_obj.put("subgroup", subgroup);
					subgroup_obj.put("genomes", new JSONArray());
					group_obj.accumulate("subgroups", subgroup_obj);
				}
				
				JSONObject genome_obj = exists(subgroup_obj.getJSONArray("genomes"), "name", name);
				if(genome_obj == null || !App.fineStatistics)
				{
					genome_obj = new JSONObject();
					genome_obj.put("name", name);
					genome_obj.put("bioproj", bioproj);
					genome_obj.put("taxid", taxid);
					genome_obj.put("assembly_id", assemblyID);
					genome_obj.put("status", status);
					genome_obj.put("modify_date", modify_date);
					genome_obj.put("old", old);
					genome_obj.put("chromosomes", chromosomes);
					genome_obj.put("plasmids", plasmids);
					genome_obj.put("mitochondrions", mitochondrions);
					genome_obj.put("chloroplasts", chloroplasts);
					genome_obj.put("path", genomePath.replace("\\", "/"));
					subgroup_obj.accumulate("genomes", genome_obj);
				}
			}
		}
//		w.add_to_log(AppLabels.APP_DONE);
		Writer writer = null;
		try 
		{
		    writer = new BufferedWriter(new OutputStreamWriter(
		          new FileOutputStream(output), "utf-8"));
		    writer.write(organizedJson.toString(4));
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
	
	public int[] updateJSON()
	{
		List<Integer> idList = new ArrayList<Integer>();
		
		int i;
		JSONArray genomes = this.genomeJson.getJSONArray("genomes");
		for(i = 0; i < genomes.length(); i++)
		{
			JSONObject genome = genomes.getJSONObject(i);
			int id = updateGenome(genome);
			if(id != -1)
			{
				idList.add(id);
			}
		}
		
		int[] ids = new int[idList.size()];
	    for (i = 0; i < ids.length; i++)
	    {
	        ids[i] = idList.get(i).intValue();
	    }
	    return ids;
	}
	
	public int updateGenome(JSONObject genome)
	{
		int id = -1;
		String kingdom = genome.getString("kingdom");
		String group = genome.getString("group");
		String subgroup = genome.getString("subgroup");
		String name = genome.getString("name");
		String status = genome.getString("status");
		String bioproj = genome.getString("project_acc");
		String assemblyID = genome.optString("assembly_id");
		String new_date = genome.getString("modify_date");
		String taxid_string = genome.getString("taxid");
		
		if((!bioproj.equals("") || !assemblyID.equals("")) && !status.equalsIgnoreCase("scaffold") && !status.equalsIgnoreCase("contig"))
		{
			JSONObject k = exists(organizedJson, "kingdom", kingdom);
			if(k == null)
			{
				System.err.println("Kingdom doesn't exist.");
				System.exit(0);
			}
			
			JSONObject g = exists(k.getJSONArray("groups"), "group", group);
			if(g == null)
			{
				System.err.println("Group doesn't exist.");
				System.exit(0);
			}
			
			JSONObject s = exists(g.getJSONArray("subgroups"), "subgroup", subgroup);
			if(s == null)
			{
				System.err.println("SubGroup doesn't exist.");
				System.exit(0);
			}
			JSONObject gen = exists(s.getJSONArray("genomes"), "taxid", taxid_string);
			if(gen == null)
			{
				//New genome.
				Boolean old = true;
				
				List<String> chromosomes;
				List<String> plasmids;
				List<String> chloroplasts;
				List<String> mitochondrions;
				
				int taxid = Integer.parseInt(taxid_string);
				chromosomes = new ArrayList<String>();
				plasmids = new ArrayList<String>();
				chloroplasts = new ArrayList<String>();
				mitochondrions = new ArrayList<String>();
				
				String kingdomPath = "Genomes" + App.FILE_SEP + kingdom.replaceAll("[\\/\\<\\>\\?|\\*\"\\:]", " ");
				String groupPath = kingdomPath + App.FILE_SEP + group.replaceAll("[\\/\\<\\>\\?|\\*\"\\:]", " ");
				String subgroupPath = groupPath + App.FILE_SEP + subgroup.replaceAll("[\\/\\<\\>\\?|\\*\"\\:]", " ");
				String genomePath = subgroupPath + App.FILE_SEP +  name.replaceAll("[\\/\\<\\>\\?|\\*\"\\:]", " ");
				
				gen = new JSONObject();
				gen.put("name", name);
				gen.put("bioproj", bioproj);
				gen.put("taxid", taxid);
				gen.put("assembly_id", assemblyID);
				gen.put("status", status);
				gen.put("modify_date", new_date);
				gen.put("old", old);
				gen.put("chromosomes", chromosomes);
				gen.put("plasmids", plasmids);
				gen.put("mitochondrions", mitochondrions);
				gen.put("chloroplasts", chloroplasts);
				gen.put("path", genomePath.replace("\\", "/"));
				
				s.accumulate("genomes", gen);
				
				id = taxid;
			}
			else
			{
				//Génome existant
				String old_date = gen.getString("modify_date");
				if(old_date.equals(new_date))
				{
					//Le génome n'a pas été mis à jour depuis la dernière récupération.
					gen.put("old", false);
				}
				else
				{
					//Le génome a été modifié depuis la dernière fois.
					gen.put("old", true);
					gen.put("modify_date", genome.getString("modify_date"));
					id = genome.getInt("taxid");
				}
			}
		}
		return id;
	}
	
	//Works.
	public int length()
	{
		int i,j,k;
		int len = organizedJson.length();
		int size = 0;
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
					int lenG = genomes.length();
					size += lenG;
				}
			}
		}
		return size;
	}
	
	public void completeUrls() throws JSONException, IOException, InterruptedException
	{
		int i,j,k,l;
		int len = organizedJson.length();
		for(i = 0; i < len; i++)
		{
			String kingName = organizedJson.getJSONObject(i).getString("kingdom");
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
						System.out.println(genome.get("name"));
						int clen = genome.getJSONArray("chromosomes").length();
						int plen = genome.getJSONArray("plasmids").length();
						int olen = genome.getJSONArray("mitochondrions").length() + genome.getJSONArray("chloroplasts").length();
						if(clen == 0 && plen == 0 && olen == 0)
						{
							if(kingName.equals("Viruses"))
							{
								String bioproj = genome.optString("bioproj");
								System.out.println(bioproj);
								List<String> urlList = getVirusURL(bioproj);
								genome.put("chromosomes", urlList);
							}
							else
							{
								String assemblyID = genome.optString("assembly_id");
								if(assemblyID != null && !assemblyID.startsWith("-") && assemblyID != "")
								{
									System.out.println(assemblyID);
									Map<String, List<String>> urlMap = getURLs(assemblyID);
									genome.put("chromosomes", urlMap.get("chromosomes"));
									genome.put("plasmids", urlMap.get("plasmids"));
									genome.put("chloroplasts", urlMap.get("chloroplasts"));
									genome.put("mitochondrions", urlMap.get("mitochondrions"));
								}
							}
							
							Writer writer = null;
							try 
							{
							    writer = new BufferedWriter(new OutputStreamWriter(
							          new FileOutputStream("massive.json"), "utf-8"));
							    writer.write(organizedJson.toString(4));
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
				}
			}
		}
	}
	
	public void initThread(String output) throws JSONException, IOException, InterruptedException
	{
		int i;
		int len = organizedJson.length();
		for(i = 0; i < len; i++)
		{
			completeUrls(organizedJson.getJSONObject(i), output);
		}
	}
	
	public void completeUrls(JSONObject kingdom, String output) throws JSONException, IOException, InterruptedException
	{
		int i;
		int j;
		int k;
		JSONArray groups = kingdom.getJSONArray("groups");
		String kingName = kingdom.getString("kingdom");
		int leng = groups.length();
		for(i = 0; i < leng; i++)
		{
			JSONArray subgroups = groups.getJSONObject(i).getJSONArray("subgroups");
			int lens = subgroups.length();
			for(j = 0; j < lens; j++)
			{
				JSONArray genomes = subgroups.getJSONObject(j).getJSONArray("genomes");
				int lenc = genomes.length();
				for(k = 0; k < lenc; k++)
				{
					JSONObject genome = genomes.getJSONObject(k);
					int clen = genome.getJSONArray("chromosomes").length();
					int plen = genome.getJSONArray("plasmids").length();
					int olen = genome.getJSONArray("mitochondrions").length() + genome.getJSONArray("chloroplasts").length();
					System.out.println(genome.getString("name"));
					if(clen == 0 && plen == 0 && olen == 0)
					{
						if(kingName.equals("Viruses"))
						{
							System.out.println("bioproj : " + genome.optString("bioproj"));
							List<String> urlList = getVirusURL(genome.getString("bioproj"));
							genome.put("chromosomes", urlList);
						}
						else
						{
							String assemblyID = genome.optString("assembly_id");
							if(assemblyID != null && !assemblyID.equals("") && assemblyID.startsWith("-"))
							{
								Map<String, List<String>> urlMap = getURLs(assemblyID);
								genome.put("chromosomes", urlMap.get("chromosomes"));
								genome.put("plasmids", urlMap.get("plasmids"));
								genome.put("mitochondrions", urlMap.get("mitochondrions"));
								genome.put("chloroplasts", urlMap.get("chloroplasts"));
							}
						}
						Writer writer = null;
						try 
						{
						    writer = new BufferedWriter(new OutputStreamWriter(
						          new FileOutputStream(output), "utf-8"));
						    writer.write(organizedJson.toString(4));
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
			}
		}
	}
	
	public void completeUrl(String kingdom, String group, String subgroup, String name) throws JSONException, IOException
	{
		JSONObject genome = getGenome(kingdom, group, subgroup, name);
		List<String> urlList = getVirusURL(genome.getString("bioproj"));
		genome.put("chromosomes", urlList);
	}
	
	//Fetches the url list of the chromosomes of a genome on the database, given its taxid.
	public List<String> getVirusURL(String bioproj) throws IOException
	{
		boolean caught = false;
		
		/*****/
//		Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("global.proxy.alcatel-lucent.com", 8000));
//		HttpURLConnection connection =(HttpURLConnection)new URL(url + "/bioproject/" + bioproj).openConnection(proxy);
		/*****/
		
		/****/
		String bioprojURL = new String(url + "/bioproject/" + bioproj);
		URL site = new URL(bioprojURL);
		URLConnection connection = site.openConnection();
		/****/
		
		BufferedReader in = null; 
		do
		{
			try
			{
				in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				caught = false;
			}
			catch(UnknownHostException | FileNotFoundException | ConnectException ce)
			{
				caught = true;
			}
		}while(caught);
        Pattern p = Pattern.compile("<a href=\"/nuccore/(NC_.*?)\"");
        String inputLine;
        List<String> urlRes = new ArrayList<String>();
        while ((inputLine = in.readLine()) != null)
        {	
        	Matcher m = p.matcher(inputLine);
        	while(m.find())
        	{
        		urlRes.add(m.group(1));
        	}
        }
        in.close();
		return urlRes;
	}
	
	public Map<String, List<String>> getURLs(String assemblyID) throws IOException, InterruptedException
	{
		int timeout = 3;
		boolean caught = false;
		
		/****/
//		Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("global.proxy.alcatel-lucent.com", 8000));
//		HttpURLConnection connection =(HttpURLConnection)new URL(url + "/assembly/" + assemblyID).openConnection(proxy);
//		connection.setRequestMethod("GET");
		/****/
		
		/****/
		String assemblyURL = url + "/assembly/" + assemblyID;
		URL site = new URL(assemblyURL);
		URLConnection connection = site.openConnection();
		/****/
		
		BufferedReader in = null;
		int i = 0;
		do
		{
			try
			{
				in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				caught = false;
			}
			catch(UnknownHostException | ConnectException ce)
			{
				System.out.println(ce);
				caught = true;
				Thread.sleep(1000);
				i++;
			}
		}while(caught && i < timeout);
		if(i == timeout)
		{
			List<String> urlC = new ArrayList<String>();
		    List<String> urlP = new ArrayList<String>();
		    List<String> urlM = new ArrayList<String>();
		    List<String> urlCl = new ArrayList<String>();
		    
		    Map<String, List<String>> res = new HashMap<String, List<String>>();
		    res.put("chromosomes", urlC);
		    res.put("plasmids", urlP);
		    res.put("mitochondrions", urlM);
		    res.put("chloroplasts", urlCl);
		    System.out.println("Timeout : " + assemblyURL);
			return res;
		}
		Pattern patC = Pattern.compile("<td>Chromosome.*?(NC_[0-9\\.]*?)</a>");
		Pattern patP = Pattern.compile("<td>Plasmid.*?(NC_[0-9\\.]*?)</a>");
		Pattern patM = Pattern.compile("<td>Mitochondrion.*?(NC_[0-9\\.]*?)</a>");
		Pattern patCl = Pattern.compile("<td>Chloroplast.*?(NC_[0-9\\.]*?)</a>");
		String inputLine;
	    List<String> urlC = new ArrayList<String>();
	    List<String> urlP = new ArrayList<String>();
	    List<String> urlM = new ArrayList<String>();
	    List<String> urlCl = new ArrayList<String>();
	    while ((inputLine = in.readLine()) != null)
	    {
		    Matcher matC = patC.matcher(inputLine);
		    Matcher matP = patP.matcher(inputLine);
		    Matcher matM = patM.matcher(inputLine);
		    Matcher matCl = patCl.matcher(inputLine);
	    	while(matC.find())
	    	{
	    		urlC.add(matC.group(1));
	    	}
	    	while(matP.find())
	    	{
	    		urlP.add(matP.group(1));
	    	}
	    	while(matM.find())
	    	{
	    		urlM.add(matM.group(1));
	    	}
	    	while(matCl.find())
	    	{
	    		urlCl.add(matCl.group(1));
	    	}
	    }
	    in.close();
	    Map<String, List<String>> res = new HashMap<String, List<String>>();
	    res.put("chromosomes", urlC);
	    res.put("plasmids", urlP);
	    res.put("mitochondrions", urlM);
	    res.put("chloroplasts", urlCl);
		return res;
	}
	
	//Get the JSON representation of a genome, safe (sends an error if doesn't exist).
	public JSONObject getGenome(String kingdom, String group, String subgroup, String genome)
	{
		JSONObject k = exists(organizedJson, "kingdom", kingdom);
		if(k == null)
		{
			System.err.println("Kingdom doesn't exist.");
			System.exit(0);
		}
		
		JSONObject g = exists(k.getJSONArray("groups"), "group", group);
		if(g == null)
		{
			System.err.println("Group doesn't exist.");
			System.exit(0);
		}
		
		JSONObject s = exists(g.getJSONArray("subgroups"), "subgroup", subgroup);
		if(s == null)
		{
			System.err.println("SubGroup doesn't exist.");
			System.exit(0);
		}
		JSONObject gen = exists(s.getJSONArray("genomes"), "name", genome);
		if(gen == null)
		{
			System.err.println("Genome doesn't exist.");
			System.exit(0);
		}
		return gen;
	}
	
	

	public JSONObject getGenome(int id)
	{
		int i;
		int j;
		int k;
		int l;
		
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
						if (genome.getInt("taxid") == id)
						{
							String kingdomName = organizedJson.getJSONObject(i).getString("kingdom");
							String kingdomPath = "Genomes" + App.FILE_SEP + kingdomName.replaceAll("[\\/\\<\\>\\?|\\*\"\\:]", " ");
							
							String groupName = groups.getJSONObject(j).getString("group");
							String groupPath = kingdomPath + App.FILE_SEP + groupName.replaceAll("[\\/\\<\\>\\?|\\*\"\\:]", " ");
							
							String subGroupName = subgroups.getJSONObject(k).getString("subgroup");
							String subGroupPath = groupPath + App.FILE_SEP + subGroupName.replaceAll("[\\/\\<\\>\\?|\\*\"\\:]", " ");
							
							String name = genome.getString("name");
							String genomePath = subGroupPath + App.FILE_SEP +  name.replaceAll("[\\/\\<\\>\\?|\\*\"\\:]", " ");
							
							//Initial directory creation :
							File theDir = new File(genomePath);
							theDir.mkdirs();
							
							return genome;
						}
					}
				}
			}
		}
		return null;
	}
	
	//Get a genome attribute according to its key, safe (returns null if doesn't exist or writes error message).
	public Object optObject(String kingdom, String group, String subgroup, String genome, String key)
	{
		JSONObject gen = getGenome(kingdom, group, subgroup, genome);
		Object o = gen.opt(key);
		return o;
	}
	
	//Get genome id of a genome, not safe.
	public int getTaxId(String kingdom, String group, String subgroup, String genome)
	{
		JSONObject gen = getGenome(kingdom, group, subgroup, genome);
		int taxid = (int)gen.getLong("taxid");
		return taxid;
	}
	
	//Get the url list of the chromoses of a genome, safe (returns null if doesn't exist or writes error message).
	public Map<String, List<String>> getUrlByGenome(String kingdom, String group, String subgroup, String genome)
	{
		JSONObject gen = getGenome(kingdom, group, subgroup, genome);
		JSONArray chr = gen.optJSONArray("chromosomes");
		JSONArray pl = gen.optJSONArray("plasmids");
		JSONArray mit = gen.optJSONArray("mitochondrions");
		JSONArray chl = gen.optJSONArray("chloroplasts");
		
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		List<String> listChr = new ArrayList<String>();
		List<String> listPl = new ArrayList<String>();
		List<String> listMit = new ArrayList<String>();
		List<String> listChl = new ArrayList<String>();
		for (int  i = 0; i < chr.length(); i++)
		{ 
		    listChr.add(chr.getString(i));
		}
		for (int  i = 0; i < pl.length(); i++)
		{ 
		    listPl.add(pl.getString(i));
		}
		for (int  i = 0; i < mit.length(); i++)
		{ 
		    listMit.add(mit.getString(i));
		}
		for (int  i = 0; i < chl.length(); i++)
		{ 
		    listChl.add(chl.getString(i));
		}
		map.put("chromosomes", listChr);
		map.put("plasmids", listPl);
		map.put("mitochondrions", listMit);
		map.put("chloroplasts", listChl);
		return map;
	}
	
	public JSONArray getUrlFromList(int[] ids)
	{
		JSONArray res = new JSONArray();
		for(int id : ids)
		{
			JSONObject gen = getGenome(id);
			
			JSONArray chr = gen.optJSONArray("chromosomes");
			JSONArray pl = gen.optJSONArray("plasmids");
			JSONArray mit = gen.optJSONArray("mitochondrions");
			JSONArray chl = gen.optJSONArray("chloroplasts");
			
			JSONObject jo = new JSONObject();
			
			List<String> listChr = new ArrayList<String>();
			List<String> listPl = new ArrayList<String>();
			List<String> listMit = new ArrayList<String>();
			List<String> listChl = new ArrayList<String>();
			for (int  i = 0; i < chr.length(); i++)
			{ 
			    listChr.add(chr.getString(i));
			}
			for (int  i = 0; i < pl.length(); i++)
			{ 
			    listPl.add(pl.getString(i));
			}
			for (int  i = 0; i < mit.length(); i++)
			{ 
			    listMit.add(mit.getString(i));
			}
			for (int  i = 0; i < chl.length(); i++)
			{ 
			    listChl.add(chl.getString(i));
			}
			jo.put("name", gen.getString("name"));
			jo.put("taxid", gen.getInt("taxid"));
			jo.put("chromosomes", listChr);
			jo.put("plasmids", listPl);
			jo.put("mitochondrions", listMit);
			jo.put("chloroplasts", listChl);
			
			res.put(jo);
		}
		return res;
	}
	public JSONArray getNuccoreFromList(int[] ids) throws IOException, InterruptedException
	{
		JSONArray res = new JSONArray();
		float s = (nb_proc * 100.0f) / ids.length;
		float n = 0;
		for(int id : ids)
		{
			JSONObject gen = getGenome(id);
			String assemblyID = gen.optString("assembly_id");
			JSONObject jo = new JSONObject();
			if(assemblyID != null && !assemblyID.equals("-") && !assemblyID.equals(""))
			{
				Map<String, List<String>> map = getURLs(assemblyID);
				List<String> listChr = map.get("chromosomes");
				List<String> listPl = map.get("plasmids");
				List<String> listChl = map.get("chloroplasts");
				List<String> listMit = map.get("mitochondrions");
				
				jo.put("name", gen.getString("name"));
				jo.put("taxid", gen.getInt("taxid"));
				jo.put("chromosomes", listChr);
				jo.put("plasmids", listPl);
				jo.put("mitochondrions", listMit);
				jo.put("chloroplasts", listChl);
			}
			else
			{
				List<String> l = getVirusURL(gen.getString("bioproj"));
				jo.put("chromosomes", l);
			}
			res.put(jo);
			n += s;
			w.set_progressBar_value((int)n);
		}
		return res;
	}

	public JSONArray getNuccoreFromList(int[] ids, int inf, int sup) throws IOException, InterruptedException
	{
		JSONArray res = new JSONArray();
		float s = 100.0f / ids.length;
		int i;
		for(i = inf; i < sup; i++)
		{
			int id = ids[i];
			JSONObject gen = getGenome(id);
			String assemblyID = gen.getString("assembly_id");
			JSONObject jo = new JSONObject();
			if(assemblyID != null && !assemblyID.equals("") && !assemblyID.equals("-"))
			{
				Map<String, List<String>> map = getURLs(assemblyID);
				List<String> listChr = map.get("chromosomes");
				List<String> listPl = map.get("plasmids");
				List<String> listChl = map.get("chloroplasts");
				List<String> listMit = map.get("mitochondrions");
				
				jo.put("chromosomes", listChr);
				jo.put("plasmids", listPl);
				jo.put("mitochondrions", listMit);
				jo.put("chloroplasts", listChl);
			}
			else
			{
				List<String> l = getVirusURL(gen.getString("bioproj"));
				List<String> tmp = new ArrayList<String>();
				jo.put("chromosomes", l);
				jo.put("plasmids", tmp);
				jo.put("mitochondrions", tmp);
				jo.put("chloroplasts", tmp);
			}
			jo.put("name", gen.getString("name"));
			jo.put("taxid", gen.getInt("taxid"));
			jo.put("path", gen.getString("path"));
			res.put(jo);
			val += s;
			w.set_progressBar_value((int)val);
		}
		return res;
	}
	
	public JSONArray getNuccoreFromIds(int[] ids)
	{
		int i;
		nb_proc = Runtime.getRuntime().availableProcessors();
		JSONArray nuccores = new JSONArray();
        NuccoreThread[] nts = new NuccoreThread[nb_proc];
        int len = ids.length / nb_proc;
        for(i = 0; i < nb_proc; i++)
        {
        	int inf = i * (len + 1);
        	int sup = Math.min(ids.length, (i+1) * (len + 1));
        	nts[i] = new NuccoreThread(this, ids, inf, sup);
            nts[i].start();
        }
        for(i = 0; i < nb_proc; i++)
        {
        	try
            {
            	nts[i].join();
            }catch(InterruptedException ie){}
            
            JSONArray ja = nts[i].getNuccores();
            for(int j = 0; j < ja.length(); j++)
            {
            	nuccores.put(ja.getJSONObject(j));
            }
        }
        return nuccores;
	}
}