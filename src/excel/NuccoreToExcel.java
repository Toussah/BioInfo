package excel;

import java.io.File;
import java.io.IOException;
import java.util.StringTokenizer;

import app.App;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;


public class NuccoreToExcel {
	
	private String name = "";
	private String type = "";
	private String path = "";
	private int size;
	private double nbr ;
	private double[] phase0, phase1, phase2;
	public static boolean sync_ok;

	public NuccoreToExcel(String pname, String ptype, String ppath, int psize) throws IOException {
		
		name = pname;
		type = ptype;
		path = ppath;
		size = psize;
		sync_ok = true;
		
	}
	
	public void setSequence(double pnbr, double[] pPhase0, double[] pPhase1, double[] pPhase2){
		this.nbr = pnbr;
		this.phase0 = pPhase0;
		this.phase1 = pPhase1;
		this.phase2 = pPhase2;
	}
	
	public void writtingName() throws IOException {
		
		String urlxl = path + App.FILE_SEP + type + ".xls";
		int i = 1;
		
		try {

			Workbook workbookread = Workbook.getWorkbook(new File("Genomes"+ App.FILE_SEP +"model.xls"));
			WritableWorkbook workbook = Workbook.createWorkbook(new File(urlxl), workbookread);
		
			WritableSheet sheet = workbook.getSheet(0);
			
			WritableFont arial16font = new WritableFont(WritableFont.ARIAL, 16); 
			WritableCellFormat arial16format = new WritableCellFormat (arial16font); 
			
			WritableCellFormat format = new WritableCellFormat();
			
			sheet.addCell(new Label(1, 0, name, arial16format)); 

			StringTokenizer token = new StringTokenizer(path, App.FILE_SEP);
			
			while(token.hasMoreTokens()) {
				
				sheet.addCell(new Label(i, 1, token.nextToken(), format));
				
				i++;
			}
			
			//�crire le nombre de s�quence
			sheet.addCell(new Number(10, 5, this.size, format));
			
			workbook.write(); 
			workbook.close();

		}
		catch(Exception e) { 
		
			e.printStackTrace();
		}
	}

	public void writtingSequence() {
		
		String urlxl = path + App.FILE_SEP + type + ".xls";

		try {
			
			Workbook workbookread = Workbook.getWorkbook(new File(urlxl));
			WritableWorkbook workbook = Workbook.createWorkbook(new File(urlxl), workbookread);
		
			WritableSheet sheet = workbook.getSheet(0);
			
			WritableCellFormat format = new WritableCellFormat();

			//on écrit le nombre de trinucléotides
			sheet.addCell(new Number(10, 7, nbr, format));

			//On écrit le nombre de génomes
			sheet.addCell(new Number(10, 9, 1, format));

			int posY = 6;
			int pos = 0;
			double frequence;
			
			//on parcours chaque élément du tableau
			for(int k = 0; k < 64; k++) {
				
				pos = posY + k;
				
				frequence = (double) (phase0[k]/nbr) * 100;
				//écrire phase0
				if(phase0[k] > 0) {
					sheet.addCell(new Number(1, pos, phase0[k], format));
					sheet.addCell(new Number(2, pos, frequence, format));
				}
				else {
					sheet.addCell(new Number(1, pos, 0, format));
					sheet.addCell(new Number(2, pos, 0, format));
				}
				
				frequence = (double) (phase1[k]/nbr) * 100;
				//écrire phase1
				if(phase1[k] > 0) {
					sheet.addCell(new Number(3, pos, phase1[k], format));
					sheet.addCell(new Number(4, pos, frequence, format));
				}
				else {
					sheet.addCell(new Number(3, pos, 0, format));
					sheet.addCell(new Number(4, pos, 0, format));
				}
				
				frequence = (double) (phase2[k]/nbr) * 100;
				//écrire phase2
				if(phase2[k] > 0) {
					sheet.addCell(new Number(5, pos, phase2[k], format));
					sheet.addCell(new Number(6, pos, frequence, format));
				}
				else {
					sheet.addCell(new Number(5, pos, 0, format));
					sheet.addCell(new Number(6, pos, 0, format));
				}
				
			}
			
			workbook.write(); 
			workbook.close();

		}
		catch(Exception e) { 
		
			e.printStackTrace();
		}
	}
}
