package excel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import app.App;


//à utiliser à la fin
public class DirectoryExcel {
	
	private static int nb_proc = Runtime.getRuntime().availableProcessors() ;
	
	private String path = "";
	private static String chromosomes = "chromosomes";
	private static String plasmids = "plasmids";
	private static String mitochondrions = "mitochondrions";
	private static String chloroplasts = "chloroplasts";
	private static String f_ext = ".xls";
	private List<File> listexcelchrom = new ArrayList<File>();
	private List<File> listexcelmito = new ArrayList<File>();
	private List<File> listexcelplasm = new ArrayList<File>();
	private List<File> listexcelchlor = new ArrayList<File>();
	
	
	public void run() {
		
		readDirectory();
		
		if(nb_proc > 4){
			nb_proc = 4;
		}
		int used_core = 0;
		WriteExcelThread[] npts = new WriteExcelThread[nb_proc];
		
		File f = new File(path + App.FILE_SEP + mitochondrions + f_ext);
		if(f.exists()){
			f.delete();
		}
		
		if(this.listexcelchrom.size() > 0) {
			
			npts[used_core] = new WriteExcelThread(this.listexcelchrom, App.FILE_SEP+chromosomes+f_ext, path);
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
		
		if(this.listexcelmito.size() > 0) {
			
			npts[used_core] = new WriteExcelThread(this.listexcelmito, App.FILE_SEP+mitochondrions+f_ext, path);
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
		
		if(this.listexcelplasm.size() > 0) {
			
			npts[used_core] = new WriteExcelThread(this.listexcelplasm, App.FILE_SEP+plasmids+f_ext, path);
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

		if(this.listexcelchlor.size() > 0) {
			
			npts[used_core] = new WriteExcelThread(this.listexcelchlor, App.FILE_SEP+chloroplasts+f_ext, path);
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
		
	}

	public DirectoryExcel(String ppath) {
		
		path = ppath;
		this.run();

	}
	
	private void readDirectory() {
		
List<File> listfile = new ArrayList<File>();
		
		File folder = new File(path+App.FILE_SEP);
		File[] listOfFiles = folder.listFiles();

		//compter les sous dossiers
		for (int i = 0; i < listOfFiles.length; i++) {

			if (listOfFiles[i].isDirectory()) {
		    		
				listfile.add(listOfFiles[i]);
			}
		}
		
		for(int i = 0; i < listfile.size(); i++) {
			

			folder = new File("" + listfile.get(i));
			listOfFiles = folder.listFiles();
			
			for(int j = 0; j < listOfFiles.length; j++) {
				
				if (listOfFiles[j].isFile()) {
					
					if(listOfFiles[j].getName().endsWith(chromosomes+f_ext)){
						this.listexcelchrom.add(listOfFiles[j]);
					}else if(listOfFiles[j].getName().endsWith(mitochondrions+f_ext)){
						this.listexcelmito.add(listOfFiles[j]);
					}else if(listOfFiles[j].getName().endsWith(plasmids+f_ext)){
						this.listexcelplasm.add(listOfFiles[j]);
					}else if(listOfFiles[j].getName().endsWith(chloroplasts+f_ext)){
						this.listexcelchlor.add(listOfFiles[j]);
					}
				}
			}
		}
		
	

	}//writeDirectory
	
}
