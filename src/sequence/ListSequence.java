package sequence;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class ListSequence {
	
	List<Sequence> listseq;

	public ListSequence() {
		
		listseq = new ArrayList<Sequence>();
	}
	
	public void addSequence(String source) {
		
		StringTokenizer st = new StringTokenizer(source, "..");
		
		listseq.add(new Sequence(source, Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()), new StringBuffer()));
	}
	
	public void addSequence(String source, int debut, int fin) {
		
		listseq.add(new Sequence(source, debut, fin, new StringBuffer()));
	}

	public void addSequence(String cds, StringBuffer sb) {
		
		listseq.add(new Sequence(cds, 0, 0, sb));
	}
	
	public int size() {
		
		return listseq.size();
	}
	
	public void tri() {
		
		Sequence tmp;
		int j;
		
		if(listseq.size() > 1)
		
		for(int i = 1; i <= listseq.size(); i++) {
			
			if(listseq.get(i-1).getDebut() > listseq.get(i).getDebut()) {
				
				tmp = listseq.get(i);
				listseq.set(i, listseq.get(i-1));
				listseq.set(i-1, tmp);
				
				j = i-1;
				//on décale en avant
				while((j > 0) && (listseq.get(j).getDebut() < listseq.get(j-1).getDebut())) {
					
					tmp = listseq.get(j);
					listseq.set(j, listseq.get(j-1));
					listseq.set(j, tmp);
					
					j--;
				}
			}
		}
	}
	
	public String getSource(int x) {
		
		return listseq.get(x).getSource();
	}
	
	public Sequence get(int x) {
		
		return listseq.get(x);
	}
	
}
