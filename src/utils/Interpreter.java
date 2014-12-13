package utils;

import java.io.IOException;

import app.App;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ui.MainWindow;

class UrlThread implements Runnable
{
	Thread t;
	FetchURLs fu;
	JSONObject kingdom;
	String output;
	UrlThread(FetchURLs fu, JSONObject kingdom, String output)
	{
		this.fu = fu;
		this.kingdom = kingdom;
		this.output = output;
		t = new Thread(this, "Url Thread");
		t.start();
	}
	
	public void run()
	{
		try 
		{
			fu.completeUrls(kingdom, output);
		} 
		catch (JSONException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}

public class Interpreter
{
	private static FetchURLs fu;
	public static String MassiveStats = "massive.json";
	public static String FineStats = "fine.json";
	
	public static JSONArray start_fetch_listing(){
		 String output = "BioInfo.json";
	     fu = null;
	     MainWindow w = MainWindow.getInstance();
	     try {
			fu = new FetchURLs("http://www.ncbi.nlm.nih.gov", "ftp.ncbi.nlm.nih.gov");
			w.set_progressBar_indeterminate();
			fu.init("genomes/GENOME_REPORTS/");
			w.set_progressBar_value(80);
			w.add_to_log("Saving listing...");
		    fu.sortJson(output);
		    w.set_progressBar_value(100);
			Thread.sleep(2000);
		} catch (IOException e) {
			w.add_to_log("Error while fetching");
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	     
	    return fu.getOrganizedJson();
	}
	
	public static JSONArray open_json_file() {
		String output = null;
		if(App.fineStatistics)
		{
			output = FineStats;
		}
		else
		{
			output = MassiveStats;
		}

		fu = null;
		JSONArray r = null;
		try {
			fu = new FetchURLs("http://www.ncbi.nlm.nih.gov", "ftp.ncbi.nlm.nih.gov");
			fu.setOrganizedJson(output);
			r = fu.getOrganizedJson();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return r;
	}
	
	public static int[] refresh()
	{
		try {
			fu.init("/genomes/GENOME_REPORTS/");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int[] ids = fu.updateJSON();
		return ids;
	}
	
	public static JSONArray fetch_nuccore(int[] ids){
		return fu.getNuccoreFromIds(ids);
	}
}