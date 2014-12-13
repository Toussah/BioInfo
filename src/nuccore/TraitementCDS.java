package nuccore;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import erreur.Erreur;
import sequence.ListSequence;

public class TraitementCDS {
	
	String id = "";
	String name = "";
	String path = "";
	String lecture = "";
	
	ListSequence listseq;
	List<Erreur> lister = new ArrayList<Erreur>();
	List<String> listcds;
	ListSequence listfinal = new ListSequence();

	public TraitementCDS(String pid, String pn, String pth, List<String> lcds, ListSequence ls, List<Erreur> le) {
		
		id = pid;
		name = pn;
		path = pth;
		listseq = ls;
		listcds = lcds;
		lister = le;
		
		runTraitement();
	}
	
	public void runTraitement() {
		
		for(int k = 0; k < listcds.size(); k++) {
			
			lecture = listcds.get(k);
			startExecuterCDS(listcds.get(k));
		}	
	}//runTraitement
	
	public void startExecuterCDS(String cds) {
		
		StringBuffer sb = executerCDS(new StringBuffer(cds));
		
		//on v�rifie la sequence final
		if(((sb.length() % 3) == 0) && (sb.length() > 0)) {

			// relacher la contrainte sur le codon d'initiation :
			ArrayList<String> codonInit = new ArrayList<>();
			codonInit.add("ATG");
			codonInit.add("CTG");
			codonInit.add("TTG");
			codonInit.add("GTG");
			codonInit.add("ATA");
			codonInit.add("ATC");
			codonInit.add("ATT");
			codonInit.add("TTA");

			//if(sb.substring(0, 3).equals("ATG") || sb.substring(0, 3).equals("GTG") || sb.substring(0, 3).equals("TTG")) {
			if(codonInit.contains(sb.substring(0, 3))) {

				if(sb.substring(sb.length()-3, sb.length()).equals("TAA") || sb.substring(sb.length()-3, sb.length()).equals("TAG") || sb.substring(sb.length()-3, sb.length()).equals("TGA")) {
					
					//si on arrive ici, la sequence est correct
					listfinal.addSequence(cds, sb);
				}
				else {
					
					addErreur("codon stop invalide");
				}
			}
			else {
				
				addErreur("codon initiation invalide");
			}
		}
		else {
			
			addErreur("chaine non modulo 3");
		}
	}
	
	public StringBuffer executerCDS(StringBuffer cds) {
		
		String compl = "complement";
		String join = "join";
		StringBuffer tmp = new StringBuffer();
		
		if((cds.length() > 10) &&(cds.substring(0,10).toString().equals(compl))) {
			
			tmp.append(cds.substring(11, cds.length()-1));
			return complement(executerCDS(tmp));
		}
		else if((cds.length() > 4) &&(cds.substring(0,4).equals(join))) {		

			tmp.append(cds.substring(5, cds.length()-1));
			return join(tmp);
		}//pas de complement et pas de join
		else if(estEntier(cds.substring(0,1))) {
			
			return code(cds);
		}//erreur
		else {
			//ici il faut creer une erreur

			return new StringBuffer();
		}
	}
	
	public StringBuffer code(StringBuffer seq) {
		
		StringBuffer sb = new StringBuffer();
		
		for(int i = 0; i < listseq.size(); i++) {
			
			if(listseq.get(i).getSource().equals(seq.toString())) {
				
				sb = listseq.get(i).getSequence();
				break;
			}
		}
		return sb;
	}
	
	public StringBuffer join(StringBuffer seq) {
		
		String tmp;
		StringBuffer resultat = new StringBuffer();
		StringBuffer sub;
		StringBuffer tmpsub;
		
		if(estEntier(seq.substring(0, 1))) {
			
			tmp = seq.toString();
			StringTokenizer token = new StringTokenizer(tmp, ",");
			
			while(token.hasMoreTokens()) {
				
				sub = new StringBuffer(token.nextToken());
				tmpsub = code(sub);
				
				//si la s�quence � prendre est null
				if(tmpsub.length() == 0) {
					
					resultat = new StringBuffer();
					break;
				}
				else {
				
					resultat.append(tmpsub);
				}
			}
		}
		else {
			
			addErreur("join contient un token invalide");
		}
		
		return resultat;
	}
	
	public StringBuffer complement(StringBuffer seq) {
		
		StringBuffer sb = new StringBuffer();
		
		for(int i = seq.length()-1 ; i >= 0; i--) {
			
			sb.append(complementLettre(seq.subSequence(i, i+1).toString()));
		}
		return sb;
	}//complement
	
	public String complementLettre(String lettre) {
		
		if(lettre.equals("A")) {
			
			return "T";
		}
		else if(lettre.equals("T")) {
			
			return "A";
		}
		else if(lettre.equals("C")) {
			
			return "G";
		}
		else if(lettre.equals("G")) {
			
			return "C";
		}
		else {
			
			return "0";
		}
	}//complementLettre
	
	public boolean estEntier(String test) {
		
		try {
			
			Integer.parseInt(test);
		} catch (NumberFormatException e){return false;}
		
 		return true;
	}//estEntier
	
	private void addErreur(String ptype) {
		
		Erreur e = new Erreur(id, name, path, lecture, ptype);
		lister.add(e);
	}//addErreur
	
	public List<Erreur> getListErreur() {
		
		return lister;
	}
	
	public ListSequence getListFinal() {
		
		return listfinal;
	}
}
