package sequence;

public class Sequence {
	
	private String source;
	private int debut;
	private int fin;
	private StringBuffer seq;
	
	public Sequence(String ps, int pd, int pf, StringBuffer pseq) {
		
		source = ps;
		debut = pd;
		fin = pf;
		seq = pseq;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public int getDebut() {
		return debut;
	}

	public void setDebut(int debut) {
		this.debut = debut;
	}

	public int getFin() {
		return fin;
	}

	public void setFin(int fin) {
		this.fin = fin;
	}

	public StringBuffer getSequence() {
		return seq;
	}
	
	public void setSequence(StringBuffer pseq) {
		this.seq = pseq;
	}
	
	public void addSubSequence(String s) {
		this.seq.append(s);
	}
	
	public boolean estDans(int pdeb, int pfin) {
		
		//le début de la séquenc est dedans
		if((debut >= pdeb) && (debut <= pfin) && (fin > pfin)) {
			
			return true;
		}//la fin de la sequence est dedans	
		else if((fin >= pdeb) && (fin <= pfin) && (debut < pdeb)) {
		
				return true;
		}//toute la séquence est dedans
		else if((debut >= pdeb) && (debut <= pfin) && (fin >= pdeb) && (fin <= pfin)) {
			
			return true;
		}//toute la partie est dans la sequence
		else if ((debut <= pdeb) && (debut <= pfin) && (fin >= pdeb) && (fin >= pfin)) {
			
			return true;
		}
		else {
			
			return false;
		}
	}
}//class Sequence
