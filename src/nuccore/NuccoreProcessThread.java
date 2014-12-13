package nuccore;

import java.io.IOException;
import java.util.List;

import ui.MainWindow;

import erreur.Erreur;
import excel.NuccoreToExcel;

public class NuccoreProcessThread extends Thread {

	private List<String> listid;
	private String name;
	private String path;
	private List<Erreur> lister;
	private String type;


	public NuccoreProcessThread(List<String> plistid, String pname, String ppath, String ptype, List<Erreur> plister) {
		listid = plistid;
		name = pname;
		path = ppath;
		lister = plister;
		type = ptype;
	}
	
	public void run(){
		this.process();
	}
	
	private void process(){
		double[] tabtriphase0 = new double [64];
		double[] tabtriphase1 = new double [64];
		double[] tabtriphase2 = new double [64];
		double nombreBase = 0;
		int size = 0;
		boolean test = true;
		int tentative = 0;
	
		for(int k = 0; k < listid.size(); k++) {
			
			test = true;
			
			ConnectionNuccore cn = new ConnectionNuccore(listid.get(k), name, path);
			tentative = 0;
			
			while(test && (tentative < 3)) {
				
				try {
					
					cn.readURL();
					test = false;					
				} catch(Exception ioe) {
					if(tentative < 2){
						try {
							Thread.sleep(3000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					tentative++;
				}
			}
			if(test){
				MainWindow.getInstance().add_to_log("Error occured while fetching " + listid.get(k));
			}
			else{
	
				if(cn.getListCDS().size() > 0) {
					
					TraitementCDS tcds = new TraitementCDS(listid.get(k), name, path, cn.getListCDS(), cn.getListSequence(), cn.getListErreur());
					name = cn.getName();
					ComptageNuccore compte = new ComptageNuccore(tcds.getListFinal(), tcds.getListErreur());
					compte.comptage();
					
					tabtriphase0 = addTab(tabtriphase0, compte.getPhase0());
					tabtriphase1 = addTab(tabtriphase1, compte.getPhase1());
					tabtriphase2 = addTab(tabtriphase2, compte.getPhase2());
					nombreBase += compte.getnbrBase();
					size += tcds.getListFinal().size();
					
					compte = null;
		
					lister.addAll(tcds.getListErreur());
					
					tcds = null;
				}
				else {
					
					lister.addAll(cn.getListErreur());
				}
			}
			cn = null;
		}

		if(size > 0) {
		
			NuccoreToExcel write;
			try {
				write = new NuccoreToExcel(this.name, this.type, this.path, size);
				write.setSequence(nombreBase, tabtriphase0, tabtriphase1, tabtriphase2);
				write.writtingName();
				write.writtingSequence();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	

	private static double[] addTab(double[] tab, double[] tab2) {
		
		for(int i = 0; i < 64; i++) {
			
			tab[i] += tab2[i];
		}
		
		return tab;
	}

}
