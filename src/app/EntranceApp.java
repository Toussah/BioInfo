/**
 * 
 */
package app;
import java.io.File;
import java.util.Scanner;

import org.json.JSONObject;
import utils.FetchURLs;
import utils.RetrieveFTP;

import javax.swing.*;


/**
 * @author billy
 *
 */
public class EntranceApp {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {

		//MetalLookAndFeel.setCurrentTheme(currentTheme);

		com.jtattoo.plaf.hifi.HiFiLookAndFeel.setTheme("Large-Font");

		try {
			UIManager.setLookAndFeel("com.jtattoo.plaf.hifi.HiFiLookAndFeel");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		new App();
//		FetchURLs fu = new FetchURLs("http://www.ncbi.nlm.nih.gov", "ftp.ncbi.nlm.nih.gov");
//		fu.init("/genomes/GENOME_REPORTS/");
////		fu.setOrganizedJson("BioInfo.json");
////		JSONObject viruses = fu.exists(fu.getOrganizedJson(), "kingdom", "Viruses");
////		fu.setGenomeJson("test2.json");
//		fu.sortJson("BioInfo3.json");
//		fu.completeUrls();
	}


}
