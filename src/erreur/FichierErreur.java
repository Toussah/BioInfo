package erreur;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class FichierErreur {
	
	private List<Erreur> lister = new ArrayList<Erreur>();

	public FichierErreur(List<Erreur> list) {
		
		lister = list;
	}
	
	public void createFile() {
		
		String url = "erreur/";
		
		url = url + "listerreur.txt";
		
		try {
			FileWriter fichier = new FileWriter(url);
			
			for(int i = 0; i < lister.size(); i++) {
				
				fichier.write (lister.get(i).lireErreur() + "\n");

			}
		    
		    fichier.close();
		}
		catch(Exception e) {
			
		}
	}
}
