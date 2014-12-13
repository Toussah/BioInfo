package nuccore;

import java.util.ArrayList;
import java.util.List;

import sequence.ListSequence;
import erreur.Erreur;

public class ComptageNuccore {
	
	private ListSequence lists = new ListSequence();
	@SuppressWarnings("unused")
	private List<Erreur> lister = new ArrayList<Erreur>();
	
	private double[] tabtri = new double [64];
	private double[] tabtriphase1 = new double [64];
	private double[] tabtriphase2 = new double [64];
	private double nbrBase;
	

	public ComptageNuccore(ListSequence ls, List<Erreur> le) {
		
		lists = ls;
		lister = le;
		
		nbrBase = 0;
		
	}
	
	public void comptage() {
		
		StringBuffer tmp = new StringBuffer();
		int taille;
		
		for(int k = 0; k < lists.size(); k++) {
			
			tmp = lists.get(k).getSequence();
			taille = tmp.length()-3;
			if(estACGT(tmp)){
				for(int j = 0; (j+3) <= taille; j+=3) {
					tabtri[trinuclToInt(tmp.substring(j,j+3))]++;
					if((j+4) < tmp.length())
						tabtriphase1[trinuclToInt(tmp.substring(j+1,j+4))]++;
					if((j+5) < tmp.length())
						tabtriphase2[trinuclToInt(tmp.substring(j+2,j+5))]++;
					nbrBase++;
				}
			}
		}
	}
	
	public double getnbrBase() {
		
		return nbrBase;
	}
	
	public double[] getPhase0() {
		
		return tabtri;
	}
	
	public double[] getPhase1() {
		
		return tabtriphase1;
	}

	public double[] getPhase2() {
	
	return tabtriphase2;
	}

	public int charToInt(String str) {

		if(str.equals("A")) {
			
			return 0;
		}
		else if(str.equals("C")) {
			
			return 1;
		}
		else if(str.equals("G")) {
			
			return 2;
		}
		else if(str.equals("T")) {
			
			return 3;
		}
		else {
			return 4;
		}

	}
	
	public int trinuclToInt(String s) {
		
		int result = 0;			
		
		result = (charToInt(s.substring(0, 1))*16) + (charToInt(s.substring(1, 2))*4) + charToInt(s.substring(2, 3));
		
		return result;
	}
	
	public String intToTrinucl(int nb) {
		
		String tab[] = {"AAA", "AAC", "AAG", "AAT", "ACA", "ACC", "ACG", "ACT", "AGA", "AGC", "AGG", "AGT", "ATA", "ATC", "ATG", "ATT", 
						"CAA", "CAC", "CAG", "CAT", "CCA", "CCC", "CCG", "CCT", "CGA", "CGC", "CGG", "CGT", "CTA", "CTC", "CTG", "CTT", 
						"GAA", "GAC", "GAG", "GAT", "GCA", "GCC", "GCG", "GCT", "GGA", "GGC", "GGG", "GGT", "GTA", "GTC", "GTG", "GTT",
						"TAA", "TAC", "TAG", "TAT", "TCA", "TCC", "TCG", "TCT", "TGA", "TGC", "TGG", "TGT", "TTA", "TTC", "TTG", "TTT"};

		return tab[nb];
	}
	
	@SuppressWarnings("unused")
	private double[] addTab(double[] tab, double[] tab2) {
		
		for(int i = 0; i < 64; i++) {
			
			tab[i] += tab2[i];
		}
		
		return tab;
	}
	
	public boolean estACGT(StringBuffer test) {
		
		int i = 0;
		
		for(i = 0; i < test.length()-1; i++) {
			
			if(test.substring(i, i+1).equals("A") || test.substring(i, i+1).equals("C") || test.substring(i, i+1).equals("G") || test.substring(i, i+1).equals("T")) {
				//do nothing
			}
			else {
				
				return false;
			}
		}
		
		return true;
	}
}
