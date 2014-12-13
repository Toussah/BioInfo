package nuccore;

import java.util.ArrayList;
import java.util.List;

import ui.MainWindow;
import utils.GenomeData;
import erreur.Erreur;

public class Nuccore {

	private static int nb_proc = Runtime.getRuntime().availableProcessors() ;
	private List<Erreur> lister;
	
	private static MainWindow w = MainWindow.getInstance();
	
	public Nuccore(GenomeData gen) throws Exception {
			if(nb_proc > 4){
				nb_proc = 4;
			}
			int used_core = 0;
			NuccoreProcessThread[] npts = new NuccoreProcessThread[nb_proc];
	      	lister = new ArrayList<Erreur>();
			
			if(gen.chromosomes.size() > 0) {
				w.add_to_log("Analysis of chromosomes...");
				npts[used_core] = new NuccoreProcessThread(gen.chromosomes, gen.name, gen.path, "chromosomes", lister);
				npts[used_core].start();
				used_core ++;
			}
			
			if(used_core >= nb_proc){
				 for(int i = 0; i < used_core; i++)
		        {
		        	try
		            {
		            	npts[i].join();
		            }catch(InterruptedException ie){}
		            
		        }
				 used_core = 0;
			}
			
			if(gen.mitochondrions.size() > 0) {
				w.add_to_log("Analysis of mitochondrions...");
				npts[used_core] = new NuccoreProcessThread(gen.mitochondrions, gen.name, gen.path, "mitochondrions", lister);
				npts[used_core].start();
				used_core ++;
			}
			
			if(used_core >= nb_proc){
				 for(int i = 0; i < used_core; i++)
		        {
		        	try
		            {
		            	npts[i].join();
		            }catch(InterruptedException ie){}
		            
		        }
				 used_core = 0;
			}
			
			if(gen.plasmids.size() > 0) {
				w.add_to_log("Analysis of plasmids...");
				npts[used_core] = new NuccoreProcessThread(gen.plasmids, gen.name, gen.path, "plasmids", lister);
				npts[used_core].start();
				used_core ++;
			}
			
			if(used_core >= nb_proc){
				 for(int i = 0; i < used_core; i++)
		        {
		        	try
		            {
		            	npts[i].join();
		            }catch(InterruptedException ie){}
		            
		        }
				 used_core = 0;
			}

			if(gen.chloroplasts.size() > 0) {
				w.add_to_log("Analysis of chloroplasts...");
				npts[used_core] = new NuccoreProcessThread(gen.chloroplasts, gen.name, gen.path, "chloroplasts", lister);
				npts[used_core].start();
				used_core ++;
			}
			
	        for(int i = 0; i < used_core; i++)
	        {
	        	try
	            {
	            	npts[i].join();
	            }catch(InterruptedException ie){}
	            
	        }
		
	}//Nuccore

	
	public List<Erreur> getErreurs() {
		
		return lister;
	}
}//class Nuccore
