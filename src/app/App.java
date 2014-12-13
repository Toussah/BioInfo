package app;

import java.util.ArrayList;

import javax.swing.tree.TreePath;

import nuccore.Nuccore;

import org.json.JSONArray;
import org.json.JSONObject;

import ui.AppLabels;
import ui.MainWindow;
import utils.GenomeData;
import utils.GenomeTreeNode;
import utils.Interpreter;
import utils.Linker;
import excel.DirectoryExcel;

public class App {
	//public static final transient String FILE_SEP  = System.getProperty("file.separator");
	public static final transient String FILE_SEP  = "/";
	public static boolean fineStatistics = false;
	
	private static Thread reload_thread;
	private static Thread analysis_thread;
	
	private static MainWindow w = MainWindow.getInstance();
	/**
	 * @throws Exception 
	 */
	public App() throws Exception {


	      new MainApp(w);
	     
	}
	
	public static void reload_metadata(TreePath[] pselectedPaths){
		//final TreePath[] selectedPaths = pselectedPaths;
		Runnable fun = new Runnable(){
			public void run(){
				w.set_progressBar_value(0);
				w.start_progressBar();
				w.clear_log();
				int ids[] = new int[0];
				//boolean all = false;
				JSONArray js = Interpreter.open_json_file();
				JSONArray result = new JSONArray();
				ArrayList<JSONArray> genomes_list = new ArrayList<JSONArray>();
				w.add_to_log("Updating selected genomes");
//				for(int i=0; i < selectedPaths.length; i++){
//		        	TreePath tp = selectedPaths[i];
//		        	GenomeTreeNode tn = (GenomeTreeNode) tp.getLastPathComponent();
//		        	switch(tn.type){
//		        		case GenomeTreeNode.ALL_TYPE:
//		        			all = true;
//		        			w.add_to_log("Start fetching list of genomes...");
//		    				js = Interpreter.start_fetch_listing();
//		    				w.append_to_log(AppLabels.APP_DONE);
//		        			break;
//		        		case GenomeTreeNode.KINGDOM_TYPE:
//		        			w.add_to_log("Start retrieving ids of kingdom " + tn.name + "...");
//		        			break;
//		        		case GenomeTreeNode.GROUP_TYPE:
//		        			w.add_to_log("Start retrieving ids of group " + tn.name + "...");
//		        			break;
//		        		case GenomeTreeNode.SUBGROUP_TYPE:
//		        			w.add_to_log("Start retrieving ids of subgroup " + tn.name + "...");
//		        			break;
//		        		case GenomeTreeNode.GENOME_TYPE:
//		        			w.add_to_log("Start retrieving id of genome " + tn.name + "...");
//		        			break;
//		        	}
		        	
		        	w.add_to_log("Start retrieving ids...");
    				ids = Interpreter.refresh();
    				w.append_to_log(AppLabels.APP_DONE);
				
					w.add_to_log("Start retrieving metadata...");
					genomes_list.add(Interpreter.fetch_nuccore(ids));   
					w.append_to_log(AppLabels.APP_DONE);
//				}
				
			    for (JSONArray arr : genomes_list) {
			    	System.out.println(arr.toString(4));
			        for (int i = 0; i < arr.length(); i++) {
			            result.put(arr.get(i));
			        }
			    }
			    
			    Linker.updateOrganizedJson(js, result);
			    w.add_to_log(AppLabels.APP_COMPLETE);
				w.set_progressBar_value(100);
				w.stop_progressBar();
			    
//				if(all){
//					w.update_tree(Linker.JTreeFromJsonArray(Interpreter.open_json_file()));
//				}
			}
		};
		reload_thread = new Thread(fun);
		reload_thread.start();
		
	}
	
	public static void launch_analysis(TreePath[] pselectedPaths){
		final TreePath[] selectedPaths = pselectedPaths;
		Runnable fun = new Runnable(){
			public void run(){
				w.set_progressBar_value(0);
				w.start_progressBar();
				w.clear_log();
				JSONArray json_file = Interpreter.open_json_file();
				JSONArray genomes_data = new JSONArray();
				w.add_to_log("Analysing selected genomes");
				if(selectedPaths.length < 0){
					w.add_to_log("Nothing to do.");
				}else{
					Linker.createPaths(json_file);
				}
		        for(int i=0; i < selectedPaths.length; i++){
		        	TreePath tp = selectedPaths[i];
		        	GenomeTreeNode tn = (GenomeTreeNode) tp.getLastPathComponent();
		        	switch(tn.type){
		        		case GenomeTreeNode.ALL_TYPE:
		        			w.add_to_log("Retrieving genomes of all kingdoms... ");
		        			genomes_data = Linker.JSONArrayOfAllFromJsonArray(json_file);
		        			w.append_to_log(AppLabels.APP_DONE);
		        			break;
		        		case GenomeTreeNode.KINGDOM_TYPE:
		        			w.add_to_log("Retrieving genomes of kingdom " + tn.name + "...");
		        			genomes_data = Linker.JSONArrayOfKingdomFromJsonArray(json_file, tn.name);
		        			w.append_to_log(AppLabels.APP_DONE);
		        			break;
		        		case GenomeTreeNode.GROUP_TYPE:
		        			w.add_to_log("Retrieving genomes of group " + tn.name + "...");
		        			genomes_data = Linker.JSONArrayOfGroupFromJsonArray(json_file, tn.name);
		        			w.append_to_log(AppLabels.APP_DONE);
		        			break;
		        		case GenomeTreeNode.SUBGROUP_TYPE:
		        			w.add_to_log("Retrieving genomes of subgroup " + tn.name + "...");
		        			genomes_data = Linker.JSONArrayOfSubgroupFromJsonArray(json_file, tn.name);
		        			w.append_to_log(AppLabels.APP_DONE);
		        			break;
		        		case GenomeTreeNode.GENOME_TYPE:
		        			w.add_to_log("Retrieving of genome " + tn.name + "...");
		        			genomes_data = Linker.JSONArrayOfGenomeFromJsonArray(json_file, tn.name);
		        			w.append_to_log(AppLabels.APP_DONE);
		        			break;
		        	}
		  	      
		  	      if(genomes_data.length() > 0){
		  		        		 
		  		      w.set_progressBar_value(0);
		  		      
		  		      w.add_to_log("Start analysis of " + genomes_data.length() + " genomes ");
		  		      
		  		      float n = 100.0f / genomes_data.length() ;
		  		      float s = (float)w.get_progressBar_value();
		  		      ArrayList<GenomeData> gd_list = new ArrayList<GenomeData>();
		  			 
		  		      for(int j=0; j < genomes_data.length(); j++){
		  		    	  JSONObject genome = genomes_data.getJSONObject(j);
		  		    	  if(genome.getBoolean("old"))
		  		    	  {
			  		    	  GenomeData gd = Linker.genomeFromJsonObject(genome); 
			  		    	  gd_list.add(gd);
			  		    	  w.add_to_log(gd.name +  " " + gd.path);
			  		    	  w.add_to_log("Processing...");
				  		      try {
									new Nuccore(gd);
							  } catch (Exception e) {
									e.printStackTrace();
							  }
			  		    	  w.add_to_log(AppLabels.APP_DONE);
			  		    	  s += n;
			  		    	  w.set_progressBar_value((int)s);
		  		    	  }
		  		      }
		  		      w.add_to_log(AppLabels.APP_COMPLETE);
		  		      w.add_to_log("Generating summary statistics...");
			  		  ArrayList<String> path_processed = new ArrayList<String>();
				      char sep = App.FILE_SEP.charAt(0);
				      ArrayList<String> path_to_process = new ArrayList<String>();
				      
				      for(int j=0; j < gd_list.size(); j++){
				    	  GenomeData gd = gd_list.get(j);
				    	  path_to_process.add(gd.path);
				      }
				      
				      while(path_to_process.size() > 0){
				    	  path_processed = new ArrayList<String>();
				    	  for(int j = 0; j < path_to_process.size(); j++){
				    		  int last_index = path_to_process.get(j).lastIndexOf(sep);
				    		  if(last_index > 0){
					    		  String parent_path = path_to_process.get(j).substring(0, last_index);
					    		  path_to_process.remove(j);
					    		  j--;
					    		  if(!path_processed.contains(parent_path)){
					    			w.add_to_log(parent_path + "...");
						    		new DirectoryExcel(parent_path);
						    	  	path_processed.add(parent_path);
						    	  	 w.append_to_log(AppLabels.APP_DONE);
						    	  }
				    		  }
				    	  }
				    	  path_to_process = path_processed;
				      }
		  	      }
		        	
		        }
		        w.add_to_log(AppLabels.APP_COMPLETE);
		        w.set_progressBar_value(100);
				w.stop_progressBar();
			}
		};
		analysis_thread = new Thread(fun);
		analysis_thread.start();
	}
	
	public static void cancel(){

	}

}
