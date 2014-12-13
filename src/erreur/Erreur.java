package erreur;

public class Erreur {
	
	private String id;
	private String nom;
	private String path;
	private String lecture;
	private String type;

	public Erreur(String pid, String pname, String ppath, String plec, String ptype) {
		
		id = pid;
		nom = pname;
		path = ppath;
		lecture = plec;
		type = ptype;
	}
	
	public String lireErreur() {
		
		return path + ";" + nom + ";" + id + ";" + lecture + ";" + type;
	}
}
