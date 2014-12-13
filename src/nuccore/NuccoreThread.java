package nuccore;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;

import utils.FetchURLs;

public class NuccoreThread extends Thread
{
	private JSONArray _nuccores = new JSONArray();
	FetchURLs fu;
	int ids[];
	int inf;
	int sup;
	String output;
	
	public NuccoreThread(FetchURLs fu, int ids[], int inf, int sup)
	{
		this.fu = fu;
		this.ids = ids;
		this.inf = inf;
		this.sup = sup;
	}
	
	public JSONArray getNuccores()
	{
		return _nuccores;
	}
	
	public void run()
	{
		try 
		{
			_nuccores = fu.getNuccoreFromList(ids, inf, sup );
		} 
		catch (JSONException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
