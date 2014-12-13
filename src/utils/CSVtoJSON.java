package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.StringTokenizer;

import org.json.JSONArray;

public class CSVtoJSON {
public static ArrayList<HashMap<String,String>> conversion(File source, String kingdom) {
		
        ArrayList<HashMap<String,String>> hashList = new ArrayList<HashMap<String,String>>();
        
        Scanner scanner = null;
        
		try {
			scanner = new Scanner(source);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Error opening the File.");
		}
		
		if(scanner!=null) { 
	        
	        //Grab the fields names
			if(scanner.hasNextLine())
	        {
				String fields[] = scanner.nextLine().split("\t");
		        renameFields(fields);
    		
	    		while (scanner.hasNextLine())
	            {
	    			HashMap<String,String> map = new HashMap<String, String>();
	        		
	    			StringTokenizer st = new StringTokenizer(scanner.nextLine(), "\t");
	    			
	    			int counter = 0;
	    			while(st.hasMoreTokens()) {
	    				String value = st.nextToken();
	    				map.put(fields[counter], value);
	    				counter++;
	    			}
	    			map.put("kingdom", kingdom);
	
	    			hashList.add(map);
	            }
	        }
			else
			{
				System.err.println("We're out.");
			}
		}
		
        //Do not forget to close the scanner 
        scanner.close();

        return hashList;
	}
	
	public static void writeResultToFile( ArrayList<HashMap<String,String>> hl, String filename) {
		
		JSONArray ja = new JSONArray(hl);
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(filename);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		writer.print(ja.toString(4));
		writer.close();
	}
	
	
	public static void renameFields(String[] fields) {
		
		for (int i = 0; i < fields.length; i++) {
			fields[i] = fields[i].replace("#Organism/Name", "name");
			fields[i] = fields[i].replace("BioProject Accession", "project_acc");
			fields[i] = fields[i].replace("Assembly Accession", "assembly_id");
			fields[i] = fields[i].replace("Chromosomes", "chromosome_count");
			fields[i] = fields[i].replace("Organelles", "organelle_count");
			fields[i] = fields[i].replace("Plasmids", "plasmid_count");
			fields[i] = fields[i].replace("Modify Date", "modify_date");
			fields[i] = fields[i].toLowerCase();
		}
		
	}

}
